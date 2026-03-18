import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../../store/UseAuthStore.js";
import { translations } from "../../locales/index.js";
import {useOrderStore} from "../../store/useOrderStore.js";

function frameColor(shippingMethod) {
    return shippingMethod === "ship"
        ? { border: "#3c7dca", glow: "rgba(60,125,202,0.4)", text: "#7ab8f5" }
        : { border: "#c9973a", glow: "rgba(201,151,58,0.35)", text: "#e8c06a" };
}

function OrderCard({ order, index, onSelect, t }) {
    const fc = frameColor(order.shippingMethod);
    const date = new Date(order.createdAt).toLocaleDateString("fr-CA", {
        year: "numeric", month: "long", day: "numeric",
    });

    return (
        <div
            className="rounded-xl transition-all duration-300 cursor-pointer group"
            style={{
                background: "rgba(13,17,23,0.8)",
                border: `1px solid ${fc.border}44`,
                borderLeft: `3px solid ${fc.border}`,
                padding: "1.1rem 1.25rem",
                animation: `fadeUp .35s ease ${index * 70}ms both`,
            }}
            onClick={() => onSelect(order.id)}
            onMouseEnter={e => e.currentTarget.style.boxShadow = `0 4px 24px ${fc.glow}`}
            onMouseLeave={e => e.currentTarget.style.boxShadow = "none"}
        >
            <div className="flex items-start justify-between gap-4 flex-wrap">
                {/* Infos gauche */}
                <div className="flex flex-col gap-1.5">
                    <p className="font-black text-base"
                       style={{ fontFamily: "Georgia,serif", color: "#e8dcc8" }}>
                        {t.orderNumber(order.id)}
                    </p>
                    <p className="text-xs italic" style={{ color: "#7a6f5e" }}>{date}</p>

                    <div className="flex items-center gap-2 mt-1">
                        <span className="text-[10px] px-2.5 py-1 rounded-full font-bold tracking-wide"
                              style={{
                                  background: `${fc.border}18`,
                                  border: `1px solid ${fc.border}55`,
                                  color: fc.text,
                              }}>
                            {order.shippingMethod === "ship" ? t.ship : t.pickup}
                        </span>
                        <span className="text-[10px] px-2.5 py-1 rounded-full"
                              style={{
                                  background: "rgba(201,151,58,0.08)",
                                  border: "1px solid rgba(201,151,58,0.2)",
                                  color: "#c9973a",
                              }}>
                            {t.cardCount(order.totalCards)}
                        </span>
                    </div>
                </div>

                {/* Total + CTA */}
                <div className="flex flex-col items-end gap-2">
                    <p className="text-xl font-black"
                       style={{ fontFamily: "Georgia,serif", color: "#e8c06a" }}>
                        ${order.totalPrice.toFixed(2)}
                        <span className="text-xs font-normal ml-1" style={{ color: "#7a6f5e" }}>
                            {t.cadLabel}
                        </span>
                    </p>
                    <span
                        className="text-[11px] tracking-widest transition-all duration-200 group-hover:text-[#e8c06a]"
                        style={{ color: "#c9973a" }}>
                        {t.seeDetails}
                    </span>
                </div>
            </div>
        </div>
    );
}

function EmptyOrders({ onBack, t }) {
    return (
        <div className="flex flex-col items-center justify-center min-h-[55vh] gap-5 text-center"
             style={{ animation: "fadeUp .4s ease" }}>
            <div style={{ fontSize: 52, opacity: 0.3 }}>📋</div>
            <div>
                <p className="font-bold text-lg" style={{ fontFamily: "Georgia,serif", color: "#e8dcc8" }}>
                    {t.emptyTitle}
                </p>
                <p className="text-sm italic mt-1" style={{ color: "#7a6f5e" }}>{t.emptySubtitle}</p>
            </div>
            <button
                onClick={onBack}
                className="text-xs tracking-widest px-6 py-2.5 rounded-lg border transition-all hover:bg-[#c9973a]/10 active:scale-95"
                style={{ color: "#c9973a", borderColor: "rgba(201,151,58,0.3)" }}
            >
                {t.emptyAction}
            </button>
        </div>
    );
}

function OrderError({ error, onRetry, t }) {
    return (
        <div className="border border-red-500/30 bg-red-500/5 rounded-xl p-8 text-center max-w-md mx-auto mt-20">
            <p className="text-red-400 tracking-widest text-sm mb-2">{t.errorTitle}</p>
            <p className="text-[#7a6f5e] italic text-sm">{t.errorNetwork}</p>
            <p className="text-[#4a3f2a] text-[11px] mt-3 font-mono break-all">{error}</p>
            <button
                onClick={onRetry}
                className="mt-4 text-xs tracking-widest px-5 py-2 rounded-lg border transition-all hover:bg-[#c9973a]/10"
                style={{ color: "#c9973a", borderColor: "rgba(201,151,58,0.3)" }}
            >
                {t.retry}
            </button>
        </div>
    );
}

export default function OrderHistory({ language = "fr" }) {
    const navigate = useNavigate();
    const t = translations[language]?.orderHistory ?? translations["fr"].orderHistory;

    const { orders, loading, error, fetchMyOrders } = useOrderStore();
    const user = useAuthStore(s => s.user);

    useEffect(() => {
        if (user?.id) fetchMyOrders();
    }, [user?.id]);

    return (
        <div className="min-h-screen bg-[#080a0f] text-[#e8dcc8] font-serif">

            <div style={{ height: 2, background: "linear-gradient(90deg, transparent, #c9973a, transparent)" }} />

            {/* Header */}
            <header className="px-8 pt-10 pb-6 border-b border-[#c9973a]/20 flex flex-wrap items-end justify-between gap-4">
                <div>
                    <p className="text-[10px] tracking-[0.4em] text-[#c9973a] uppercase mb-2 font-sans">
                        ⟡ {t.eyebrow}
                    </p>
                    <h1 className="text-4xl font-black tracking-tight bg-gradient-to-br from-[#e8c06a] via-[#c9973a] to-[#a07828] bg-clip-text text-transparent"
                        style={{ fontFamily: "Georgia,serif" }}>
                        {t.title}
                    </h1>
                    <p className="text-[#7a6f5e] italic mt-1 text-sm">{t.subtitle}</p>
                </div>

                <div className="flex items-center gap-3">
                    {orders.length > 0 && (
                        <span className="text-xs tracking-widest text-[#c9973a] border border-[#c9973a]/20 bg-[#131920] rounded-full px-4 py-1.5">
                            {t.orderCount(orders.length)}
                        </span>
                    )}
                    <button
                        onClick={() => navigate("/inventaire")}
                        className="text-xs tracking-widest px-4 py-1.5 rounded-full border transition-all duration-200 hover:bg-[#c9973a]/10"
                        style={{ color: "#9a8e7a", borderColor: "rgba(201,151,58,0.2)" }}
                    >
                        {t.emptyAction}
                    </button>
                </div>
            </header>

            {/* Body */}
            <main className="px-8 py-8 max-w-3xl">
                {loading && (
                    <div className="flex flex-col items-center justify-center min-h-[50vh] gap-4">
                        <div className="w-10 h-10 border-2 border-[#c9973a]/20 border-t-[#c9973a] rounded-full animate-spin" />
                        <p className="text-xs tracking-[0.3em] text-[#7a6f5e] uppercase">{t.loading}</p>
                    </div>
                )}

                {error && !loading && (
                    <OrderError error={error} onRetry={fetchMyOrders} t={t} />
                )}

                {!loading && !error && (
                    orders.length === 0
                        ? <EmptyOrders onBack={() => navigate("/inventaire")} t={t} />
                        : (
                            <div className="flex flex-col gap-3">
                                {orders.map((order, i) => (
                                    <OrderCard
                                        key={order.id}
                                        order={order}
                                        index={i}
                                        onSelect={(id) => navigate(`/orders/${id}`)}
                                        t={t}
                                    />
                                ))}
                            </div>
                        )
                )}
            </main>

            <div className="mx-8 mt-4 mb-8"
                 style={{ height: 1, background: "linear-gradient(90deg, transparent, rgba(201,151,58,0.3), transparent)" }} />

            <style>{`
                @keyframes fadeUp {
                    from { opacity: 0; transform: translateY(14px); }
                    to   { opacity: 1; transform: translateY(0); }
                }
            `}</style>
        </div>
    );
}
import React, { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useOrderStore } from "../store/OrderStore.js";
import { useAuthStore } from "../store/UseAuthStore.js";
import { translations } from "../locales/index.js";

function frameColor(cardType) {
    const ft = (cardType ?? "").toUpperCase();
    if (ft.includes("SPELL"))   return { border: "#1d7853", text: "#6ef0a8" };
    if (ft.includes("TRAP"))    return { border: "#8c2055", text: "#f06aab" };
    if (ft.includes("FUSION"))  return { border: "#7b4fa0", text: "#c99ef0" };
    if (ft.includes("SYNCHRO")) return { border: "#c0c0c0", text: "#e8e8e8" };
    if (ft.includes("XYZ"))     return { border: "#555",    text: "#aaa"    };
    if (ft.includes("RITUAL"))  return { border: "#3c7dca", text: "#7ab8f5" };
    if (ft.includes("LINK"))    return { border: "#00527d", text: "#4db8e8" };
    return { border: "#c9973a", text: "#e8c06a" };
}

function OrderItemRow({ item, index }) {
    const fc = frameColor(item.cardType ?? item.frameType);

    return (
        <div
            className="flex items-center gap-4 px-4 py-3"
            style={{
                background: "rgba(13,17,23,0.6)",
                borderLeft: `3px solid ${fc.border}`,
                borderBottom: "1px solid rgba(201,151,58,0.07)",
                animation: `fadeUp .3s ease ${index * 50}ms both`,
            }}
        >
            {/* Image */}
            <div className="shrink-0 rounded overflow-hidden"
                 style={{ width: 40, height: 56, background: `${fc.border}22` }}>
                {item.imageUrl
                    ? <img src={item.imageUrl} alt={item.cardName} className="w-full h-full object-cover" />
                    : <div className="w-full h-full flex items-center justify-center text-lg">🃏</div>
                }
            </div>

            {/* Nom + type */}
            <div className="flex-1 min-w-0">
                <p className="text-sm font-bold truncate"
                   style={{ fontFamily: "Georgia,serif", color: "#e8dcc8" }}>
                    {item.cardName}
                </p>
                <p className="text-[11px] italic truncate mt-0.5"
                   style={{ color: fc.text, opacity: 0.8 }}>
                    {item.cardType?.replaceAll("_", " ") ?? ""}
                </p>
            </div>

            {/* Qté */}
            {item.quantity > 1 && (
                <div className="shrink-0 px-2 py-0.5 rounded-md text-xs font-bold"
                     style={{
                         background: "rgba(201,151,58,0.1)",
                         border: "1px solid rgba(201,151,58,0.25)",
                         color: "#c9973a",
                     }}>
                    ×{item.quantity}
                </div>
            )}

            {/* Prix ligne */}
            <div className="shrink-0 text-right">
                <p className="text-sm font-bold"
                   style={{ color: "#e8c06a", fontFamily: "Georgia,serif" }}>
                    ${item.lineTotal.toFixed(2)}
                </p>
                {item.quantity > 1 && (
                    <p className="text-[10px]" style={{ color: "#7a6f5e" }}>
                        ${item.priceAtPurchase.toFixed(2)} × {item.quantity}
                    </p>
                )}
            </div>
        </div>
    );
}

export default function OrderDetail({ language = "fr" }) {
    const { id } = useParams();
    const navigate = useNavigate();
    const t = translations[language]?.orderHistory ?? translations["fr"].orderHistory;

    const { selectedOrder, loading, error, fetchOrderById, clearSelectedOrder } = useOrderStore();
    const user = useAuthStore(s => s.user);

    useEffect(() => {
        if (user?.id && id) fetchOrderById(id);
        return () => clearSelectedOrder();
    }, [user?.id, id]);

    const date = selectedOrder
        ? new Date(selectedOrder.createdAt).toLocaleDateString("fr-CA", {
            year: "numeric", month: "long", day: "numeric",
        })
        : "";

    const shippingCost = selectedOrder?.shippingMethod === "ship" ? 3.99 : 0;

    return (
        <div className="min-h-screen bg-[#080a0f] text-[#e8dcc8] font-serif">

            <div style={{ height: 2, background: "linear-gradient(90deg, transparent, #c9973a, transparent)" }} />

            {/* Header */}
            <header className="px-8 pt-10 pb-6 border-b border-[#c9973a]/20 flex flex-wrap items-end justify-between gap-4">
                <div>
                    <p className="text-[10px] tracking-[0.4em] text-[#c9973a] uppercase mb-2 font-sans">
                        ⟡ {t.detailEyebrow}
                    </p>
                    <h1 className="text-4xl font-black tracking-tight bg-gradient-to-br from-[#e8c06a] via-[#c9973a] to-[#a07828] bg-clip-text text-transparent"
                        style={{ fontFamily: "Georgia,serif" }}>
                        {selectedOrder ? t.orderNumber(selectedOrder.id) : "—"}
                    </h1>
                    {date && (
                        <p className="text-[#7a6f5e] italic mt-1 text-sm">{date}</p>
                    )}
                </div>

                <button
                    onClick={() => navigate("/orders")}
                    className="text-xs tracking-widest px-4 py-1.5 rounded-full border transition-all duration-200 hover:bg-[#c9973a]/10"
                    style={{ color: "#9a8e7a", borderColor: "rgba(201,151,58,0.2)" }}
                >
                    {t.backToOrders}
                </button>
            </header>

            {/* Body */}
            <main className="px-8 py-8 max-w-2xl mx-auto">

                {loading && (
                    <div className="flex flex-col items-center justify-center min-h-[50vh] gap-4">
                        <div className="w-10 h-10 border-2 border-[#c9973a]/20 border-t-[#c9973a] rounded-full animate-spin" />
                        <p className="text-xs tracking-[0.3em] text-[#7a6f5e] uppercase">{t.loading}</p>
                    </div>
                )}

                {error && !loading && (
                    <div className="border border-red-500/30 bg-red-500/5 rounded-xl p-8 text-center">
                        <p className="text-red-400 tracking-widest text-sm mb-2">{t.errorTitle}</p>
                        <p className="text-[#7a6f5e] italic text-sm">{error}</p>
                        <button
                            onClick={() => fetchOrderById(id)}
                            className="mt-4 text-xs tracking-widest px-5 py-2 rounded-lg border transition-all hover:bg-[#c9973a]/10"
                            style={{ color: "#c9973a", borderColor: "rgba(201,151,58,0.3)" }}
                        >
                            {t.retry}
                        </button>
                    </div>
                )}

                {!loading && !error && selectedOrder && (
                    <>
                        {/* Badge livraison */}
                        <div className="flex items-center gap-2.5 rounded-full px-5 py-2 w-fit mb-6"
                             style={{
                                 background: "rgba(201,151,58,0.07)",
                                 border: "1px solid rgba(201,151,58,0.25)",
                             }}>
                            <span style={{ fontSize: 16 }}>
                                {selectedOrder.shippingMethod === "ship" ? "📦" : "🏪"}
                            </span>
                            <span className="text-xs tracking-widest" style={{ color: "#c9973a" }}>
                                {selectedOrder.shippingMethod === "ship" ? t.ship : t.pickup}
                            </span>
                        </div>

                        {/* Liste des cartes */}
                        <div className="rounded-xl overflow-hidden mb-6"
                             style={{ border: "1px solid rgba(201,151,58,0.15)" }}>

                            <div className="px-4 py-3"
                                 style={{ background: "rgba(201,151,58,0.06)", borderBottom: "1px solid rgba(201,151,58,0.1)" }}>
                                <p className="text-[10px] tracking-[0.35em] uppercase" style={{ color: "#4a3f2a" }}>
                                    {t.detailCardCount(selectedOrder.totalCards)}
                                </p>
                            </div>

                            <div className="flex flex-col">
                                {selectedOrder.items.map((item, i) => (
                                    <OrderItemRow key={item.cardId} item={item} index={i} />
                                ))}
                            </div>

                            {/* Totaux */}
                            <div className="px-4 py-3 flex flex-col gap-1.5"
                                 style={{ background: "rgba(201,151,58,0.04)", borderTop: "1px solid rgba(201,151,58,0.1)" }}>
                                <div className="flex justify-between text-xs" style={{ color: "#7a6f5e" }}>
                                    <span>{t.subtotalLabel}</span>
                                    <span>${(selectedOrder.totalPrice - shippingCost).toFixed(2)}</span>
                                </div>
                                <div className="flex justify-between text-xs" style={{ color: "#7a6f5e" }}>
                                    <span>{t.shippingCostLabel}</span>
                                    <span>{shippingCost === 0 ? t.free : `$${shippingCost.toFixed(2)}`}</span>
                                </div>
                                <div className="flex justify-between text-sm font-bold mt-1"
                                     style={{ color: "#e8c06a", fontFamily: "Georgia,serif" }}>
                                    <span>{t.totalLabel}</span>
                                    <span>
                                        ${selectedOrder.totalPrice.toFixed(2)}{" "}
                                        <span className="text-xs font-normal" style={{ color: "#7a6f5e" }}>
                                            {t.cadLabel}
                                        </span>
                                    </span>
                                </div>
                            </div>
                        </div>

                        {/* Retour */}
                        <div className="flex justify-center">
                            <button
                                onClick={() => navigate("/orders")}
                                className="text-xs tracking-widest px-6 py-3 rounded-xl border transition-all duration-200"
                                style={{ color: "#9a8e7a", borderColor: "rgba(201,151,58,0.2)" }}
                                onMouseEnter={e => e.currentTarget.style.background = "rgba(201,151,58,0.06)"}
                                onMouseLeave={e => e.currentTarget.style.background = "transparent"}
                            >
                                {t.backToOrders}
                            </button>
                        </div>
                    </>
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
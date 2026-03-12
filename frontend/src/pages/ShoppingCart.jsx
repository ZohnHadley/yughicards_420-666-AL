import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useShoppingCartStore } from "../store/ShoppingCartStore.js";
import { translations } from "../locales/index.js";
import {useAuthStore} from "../store/UseAuthStore.js";

const USD_TO_CAD = 1.36;

function cardPrice(card) {
    const raw = parseFloat(card?.card_prices?.[0]?.cardmarket_price ?? 0);
    return raw > 0 ? raw * USD_TO_CAD : 0;
}

function frameColor(card) {
    const ft = (card?.frameType ?? card?.type ?? "").toUpperCase();
    if (ft.includes("SPELL"))   return { border: "#1d7853", glow: "rgba(29,120,83,0.4)",   text: "#6ef0a8" };
    if (ft.includes("TRAP"))    return { border: "#8c2055", glow: "rgba(140,32,85,0.4)",   text: "#f06aab" };
    if (ft.includes("FUSION"))  return { border: "#7b4fa0", glow: "rgba(123,79,160,0.4)",  text: "#c99ef0" };
    if (ft.includes("SYNCHRO")) return { border: "#c0c0c0", glow: "rgba(192,192,192,0.3)", text: "#e8e8e8" };
    if (ft.includes("XYZ"))     return { border: "#333",    glow: "rgba(80,80,80,0.4)",    text: "#aaa"    };
    if (ft.includes("RITUAL"))  return { border: "#3c7dca", glow: "rgba(60,125,202,0.4)",  text: "#7ab8f5" };
    if (ft.includes("LINK"))    return { border: "#00527d", glow: "rgba(0,82,125,0.4)",    text: "#4db8e8" };
    return { border: "#c9973a", glow: "rgba(201,151,58,0.35)", text: "#e8c06a" };
}

// ── Single cart row ───────────────────────────────────────────────────────────
function CartRow({ card, index, onRemove, t }) {
    const fc = frameColor(card);
    const cad = cardPrice(card).toFixed(2);
    const imgUrl = card.card_images?.[0]?.image_url_small ?? card.card_images?.[0]?.image_url;
    const isMonster = card.atk != null;
    const typeLabel = card.type?.replaceAll("_", " ") ?? "";

    return (
        <div
            className="flex items-center gap-4 rounded-xl transition-all duration-300 group"
            style={{
                background: "rgba(13,17,23,0.8)",
                border: `1px solid ${fc.border}44`,
                borderLeft: `3px solid ${fc.border}`,
                padding: "0.85rem 1rem",
                animation: `fadeUp .35s ease ${index * 60}ms both`,
            }}
            onMouseEnter={e => e.currentTarget.style.boxShadow = `0 4px 24px ${fc.glow}`}
            onMouseLeave={e => e.currentTarget.style.boxShadow = "none"}
        >
            <div className="shrink-0 rounded-lg overflow-hidden"
                 style={{ width: 48, height: 68, background: `linear-gradient(135deg, ${fc.border}33, #0d1117)` }}>
                {imgUrl
                    ? <img src={imgUrl} alt={card.name} className="w-full h-full object-cover" />
                    : <div className="w-full h-full flex items-center justify-center text-lg">🃏</div>
                }
            </div>

            <div className="flex-1 min-w-0">
                <p className="font-bold text-sm leading-snug truncate"
                   style={{ fontFamily: "Georgia,serif", color: "#e8dcc8" }}>
                    {card.name}
                </p>
                <p className="text-[11px] italic mt-0.5 truncate" style={{ color: fc.text, opacity: 0.8 }}>
                    {typeLabel}
                </p>
                {isMonster && (
                    <p className="text-[11px] mt-0.5" style={{ color: "#7a6f5e" }}>
                        ATK <span style={{ color: "#e8c06a" }}>{card.atk}</span>
                        {" / "}DEF <span style={{ color: "#7ab8f5" }}>{card.def}</span>
                    </p>
                )}
            </div>

            <div className="shrink-0 text-right">
                <p className="text-sm font-bold" style={{ color: "#e8c06a", fontFamily: "Georgia,serif" }}>
                    ${cad}
                </p>
                <p className="text-[10px]" style={{ color: "#7a6f5e" }}>{t.cadLabel}</p>
            </div>

            <button
                onClick={() => onRemove(card.id)}
                className="shrink-0 w-7 h-7 rounded-lg flex items-center justify-center text-sm transition-all duration-200 opacity-40 group-hover:opacity-100"
                style={{ border: "1px solid rgba(239,68,68,0.3)", color: "#f87171" }}
                onMouseEnter={e => e.currentTarget.style.background = "rgba(239,68,68,0.15)"}
                onMouseLeave={e => e.currentTarget.style.background = "transparent"}
                title={t.removeTitle}
            >
                ✕
            </button>
        </div>
    );
}

// ── Order summary sidebar ─────────────────────────────────────────────────────
function OrderSummary({ cards, onCheckout, t }) {
    const subtotal = cards.reduce((s, c) => s + cardPrice(c), 0);
    const shipping = cards.length > 0 ? 3.99 : 0;
    const total    = subtotal + shipping;

    return (
        <div className="rounded-xl p-5 sticky top-6"
             style={{ background: "rgba(13,17,23,0.9)", border: "1px solid rgba(201,151,58,0.2)" }}>

            <p className="text-[10px] tracking-[0.35em] uppercase mb-4"
               style={{ color: "#c9973a", fontFamily: "Georgia,serif" }}>
                {t.summaryTitle}
            </p>

            <div className="flex flex-col gap-3 text-sm">
                <div className="flex justify-between">
                    <span style={{ color: "#7a6f5e" }}>{t.summaryCards(cards.length)}</span>
                    <span style={{ color: "#e8dcc8" }}>${subtotal.toFixed(2)}</span>
                </div>
                <div className="flex justify-between">
                    <span style={{ color: "#7a6f5e" }}>{t.summaryShipping}</span>
                    <span style={{ color: "#e8dcc8" }}>
                        {cards.length ? `$${shipping.toFixed(2)}` : t.summaryShippingFree}
                    </span>
                </div>
                <div style={{ height: 1, background: "rgba(201,151,58,0.15)" }} />
                <div className="flex justify-between items-center">
                    <span className="font-bold" style={{ color: "#e8dcc8", fontFamily: "Georgia,serif" }}>
                        {t.summaryTotal}
                    </span>
                    <span className="text-lg font-bold" style={{ color: "#e8c06a", fontFamily: "Georgia,serif" }}>
                        ${total.toFixed(2)}{" "}
                        <span className="text-xs font-normal" style={{ color: "#7a6f5e" }}>{t.cadLabel}</span>
                    </span>
                </div>
            </div>

            <button
                onClick={onCheckout}
                disabled={cards.length === 0}
                className="mt-5 w-full py-3 rounded-xl text-xs font-bold tracking-[0.2em] uppercase transition-all duration-200"
                style={{
                    background: cards.length === 0
                        ? "rgba(201,151,58,0.08)"
                        : "linear-gradient(135deg, #c9973a, #a07828)",
                    color: cards.length === 0 ? "#4a3f2a" : "#080a0f",
                    border: "1px solid rgba(201,151,58,0.3)",
                    cursor: cards.length === 0 ? "not-allowed" : "pointer",
                }}
                onMouseEnter={e => { if (cards.length > 0) e.currentTarget.style.filter = "brightness(1.1)"; }}
                onMouseLeave={e => { e.currentTarget.style.filter = "none"; }}
            >
                {cards.length === 0 ? t.emptyCheckout : t.checkoutButton}
            </button>

            <p className="text-center text-[10px] mt-3" style={{ color: "#4a3f2a" }}>
                {t.secureCheckout}
            </p>
        </div>
    );
}

// ── Empty state ───────────────────────────────────────────────────────────────
function EmptyCart({ onBack, t }) {
    return (
        <div className="flex flex-col items-center justify-center min-h-[55vh] gap-5 text-center"
             style={{ animation: "fadeUp .4s ease" }}>

            {/* Animated card icon */}
            <div style={{ position: "relative", width: 80, height: 80 }}>
                <div style={{
                    width: 56, height: 76, borderRadius: 6, position: "absolute", left: "50%", top: "50%",
                    transform: "translate(-60%, -50%) rotate(-8deg)",
                    background: "rgba(201,151,58,0.08)", border: "1px solid rgba(201,151,58,0.2)",
                }} />
                <div style={{
                    width: 56, height: 76, borderRadius: 6, position: "absolute", left: "50%", top: "50%",
                    transform: "translate(-40%, -50%) rotate(8deg)",
                    background: "rgba(201,151,58,0.08)", border: "1px solid rgba(201,151,58,0.2)",
                }} />
                <div style={{
                    width: 56, height: 76, borderRadius: 6, position: "absolute", left: "50%", top: "50%",
                    transform: "translate(-50%, -50%)",
                    background: "rgba(13,17,23,0.9)", border: "1px solid rgba(201,151,58,0.3)",
                    display: "flex", alignItems: "center", justifyContent: "center",
                    fontSize: 28,
                }}>
                    🃏
                </div>
            </div>

            <div>
                <p className="font-bold text-lg" style={{ fontFamily: "Georgia,serif", color: "#e8dcc8" }}>
                    {t.emptyTitle}
                </p>
                <p className="text-sm italic mt-1" style={{ color: "#7a6f5e" }}>
                    {t.emptySubtitle}
                </p>
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

// ── Real error (backend truly down) ──────────────────────────────────────────
function CartError({ error, onRetry, t }) {
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
                {t.retry ?? "Réessayer"}
            </button>
        </div>
    );
}

// ═════════════════════════════════════════════════════════════════════════════
//  Main ShoppingCart page
// ═════════════════════════════════════════════════════════════════════════════
export default function ShoppingCart({ language = "fr" }) {
    const navigate = useNavigate();
    const t = translations[language]?.shoppingCart ?? translations["fr"].shoppingCart;

    const { cart, loading, error, fetchByUserId, removeCard, getCardCount } =
        useShoppingCartStore();

    const user = useAuthStore(s => s.user);

    useEffect(() => {
        if (user?.id) fetchByUserId();
    }, [user?.id]);

    const cards = cart?.cards ?? [];

    const handleCheckout = () => {
        // TODO: navigate("/checkout")
        alert(t.checkoutButton);
    };

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
                    {cart?.applicationUser && (
                        <p className="text-[#7a6f5e] italic mt-1 text-sm">
                            {cart.applicationUser.username} · {cart.applicationUser.email}
                        </p>
                    )}
                </div>

                <div className="flex items-center gap-3">
                    <span className="text-xs tracking-widest text-[#c9973a] border border-[#c9973a]/20 bg-[#131920] rounded-full px-4 py-1.5">
                        {t.cartesBadge(getCardCount())}
                    </span>
                    <button
                        onClick={() => navigate(-1)}
                        className="text-xs tracking-widest px-4 py-1.5 rounded-full border transition-all duration-200 hover:bg-[#c9973a]/10"
                        style={{ color: "#9a8e7a", borderColor: "rgba(201,151,58,0.2)" }}
                    >
                        {t.backToInventory}
                    </button>
                </div>
            </header>

            {/* Body */}
            <main className="px-8 py-8">

                {/* Loading */}
                {loading && (
                    <div className="flex flex-col items-center justify-center min-h-[50vh] gap-4">
                        <div className="w-10 h-10 border-2 border-[#c9973a]/20 border-t-[#c9973a] rounded-full animate-spin" />
                        <p className="text-xs tracking-[0.3em] text-[#7a6f5e] uppercase">{t.loading}</p>
                    </div>
                )}

                {/* Error — vrai problème réseau/serveur */}
                {error && !loading && (
                    <CartError error={error} onRetry={() => fetchByUserId()} t={t} />
                )}

                {/* Content — panier vide OU liste de cartes */}
                {!loading && !error && (
                    cards.length === 0
                        ? <EmptyCart onBack={() => navigate(-1)} t={t} />
                        : (
                            <div className="grid gap-8"
                                 style={{ gridTemplateColumns: "1fr min(320px, 100%)", alignItems: "start" }}>
                                <section>
                                    <p className="text-[10px] tracking-[0.35em] uppercase mb-4"
                                       style={{ color: "#4a3f2a" }}>
                                        {t.cardCount(cards.length)}
                                    </p>
                                    <div className="flex flex-col gap-2">
                                        {cards.map((card, i) => (
                                            <CartRow
                                                key={card.id}
                                                card={card}
                                                index={i}
                                                onRemove={removeCard}
                                                t={t}
                                            />
                                        ))}
                                    </div>
                                </section>
                                <aside>
                                    <OrderSummary cards={cards} onCheckout={handleCheckout} t={t} />
                                </aside>
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
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useShoppingCartStore } from "../store/ShoppingCartStore.js";
import { translations } from "../locales/index.js";
import { useAuthStore } from "../store/UseAuthStore.js";

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
function CartRow({ card, qty, index, onRemove, onChangeQty, t }) {
    const fc = frameColor(card);
    const unitPrice = cardPrice(card);
    const totalCad  = (unitPrice * qty).toFixed(2);
    const imgUrl = card.card_images?.[0]?.image_url_small ?? card.card_images?.[0]?.image_url;
    const isMonster = card.atk != null;
    const typeLabel = card.type?.replaceAll("_", " ") ?? "";
    const [updating, setUpdating] = useState(false);

    const handleQtyChange = async (newQty) => {
        if (updating || newQty === qty) return;
        setUpdating(true);
        try { await onChangeQty(card.id, newQty); }
        finally { setUpdating(false); }
    };

    return (
        <div
            className="flex items-center gap-4 rounded-xl transition-all duration-300 group"
            style={{
                background: "rgba(13,17,23,0.8)",
                border: `1px solid ${fc.border}44`,
                borderLeft: `3px solid ${fc.border}`,
                padding: "0.85rem 1rem",
                animation: `fadeUp .35s ease ${index * 60}ms both`,
                opacity: updating ? 0.6 : 1,
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

            {/* Quantity stepper */}
            <div className="shrink-0 flex items-center rounded-lg overflow-hidden"
                 style={{ border: "1px solid rgba(201,151,58,0.25)" }}>
                <button
                    onClick={() => handleQtyChange(qty - 1)}
                    disabled={qty <= 1 || updating}
                    className="w-7 h-7 flex items-center justify-center text-base transition-all hover:opacity-80 disabled:opacity-25 disabled:cursor-not-allowed"
                    style={{ background: "rgba(201,151,58,0.08)", color: "#c9973a" }}
                >−</button>
                <span className="w-8 text-center text-xs font-bold tabular-nums"
                      style={{ color: "#c9973a", fontFamily: "Georgia,serif" }}>
                    ×{qty}
                </span>
                <button
                    onClick={() => handleQtyChange(qty + 1)}
                    disabled={qty >= 3 || updating}
                    className="w-7 h-7 flex items-center justify-center text-base transition-all hover:opacity-80 disabled:opacity-25 disabled:cursor-not-allowed"
                    style={{ background: "rgba(201,151,58,0.08)", color: "#c9973a" }}
                >+</button>
            </div>

            <div className="shrink-0 text-right">
                <p className="text-sm font-bold" style={{ color: "#e8c06a", fontFamily: "Georgia,serif" }}>
                    ${totalCad}
                </p>
                <p className="text-[10px]" style={{ color: "#7a6f5e" }}>{t.cadLabel}</p>
            </div>

            <button
                onClick={() => onRemove(card.id)}
                className="shrink-0 w-7 h-7 rounded-lg flex items-center justify-center text-sm transition-all duration-200 opacity-60 group-hover:opacity-100"
                style={{
                    border: "1px solid rgba(248,113,113,0.5)",
                    color: "#fca5a5",
                    background: "rgba(239,68,68,0.12)",
                }}
                onMouseEnter={e => {
                    e.currentTarget.style.background = "rgba(239,68,68,0.28)";
                    e.currentTarget.style.color = "#fff";
                    e.currentTarget.style.borderColor = "rgba(248,113,113,0.8)";
                }}
                onMouseLeave={e => {
                    e.currentTarget.style.background = "rgba(239,68,68,0.12)";
                    e.currentTarget.style.color = "#fca5a5";
                    e.currentTarget.style.borderColor = "rgba(248,113,113,0.5)";
                }}
                title={t.removeTitle}
            >
                ✕
            </button>
        </div>
    );
}

// ── Shipping selector ─────────────────────────────────────────────────────────
function ShippingSelector({ value, onChange, t }) {
    const options = [
        { key: "pickup", label: t.shippingPickup, price: 0 },
        { key: "ship",   label: t.shippingDeliver, price: 3.99 },
    ];

    return (
        <div className="flex flex-col gap-2 mt-1">
            {options.map(opt => (
                <label
                    key={opt.key}
                    className="flex items-center justify-between gap-3 rounded-lg px-3 py-2.5 cursor-pointer transition-all duration-150"
                    style={{
                        border: value === opt.key
                            ? "1px solid rgba(201,151,58,0.5)"
                            : "1px solid rgba(201,151,58,0.15)",
                        background: value === opt.key
                            ? "rgba(201,151,58,0.08)"
                            : "transparent",
                    }}
                >
                    <div className="flex items-center gap-2.5">
                        {/* Custom radio */}
                        <div style={{
                            width: 16, height: 16, borderRadius: "50%",
                            border: value === opt.key
                                ? "2px solid #c9973a"
                                : "2px solid rgba(201,151,58,0.35)",
                            display: "flex", alignItems: "center", justifyContent: "center",
                            flexShrink: 0,
                        }}>
                            {value === opt.key && (
                                <div style={{
                                    width: 7, height: 7, borderRadius: "50%",
                                    background: "#c9973a",
                                }} />
                            )}
                        </div>
                        <input
                            type="radio"
                            name="shipping"
                            value={opt.key}
                            checked={value === opt.key}
                            onChange={() => onChange(opt.key)}
                            className="sr-only"
                        />
                        <span className="text-xs" style={{ color: value === opt.key ? "#e8dcc8" : "#7a6f5e" }}>
                            {opt.label}
                        </span>
                    </div>
                    <span className="text-xs font-bold" style={{ color: value === opt.key ? "#e8c06a" : "#4a3f2a" }}>
                        {opt.price === 0 ? t.shippingFree : `$${opt.price.toFixed(2)}`}
                    </span>
                </label>
            ))}
        </div>
    );
}

// ── Order summary sidebar ─────────────────────────────────────────────────────
function OrderSummary({ cards, onCheckout, checkingOut, t }) {
    const [shipping, setShipping] = useState("pickup");

    const subtotal     = cards.reduce((s, c) => s + cardPrice(c), 0);
    const shippingCost = shipping === "ship" ? 3.99 : 0;
    const total        = subtotal + shippingCost;

    const disabled = cards.length === 0 || checkingOut;

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

                {/* Shipping selector */}
                <div>
                    <p className="text-[10px] tracking-widest uppercase mb-1.5" style={{ color: "#4a3f2a" }}>
                        {t.shippingTitle}
                    </p>
                    <ShippingSelector value={shipping} onChange={setShipping} t={t} />
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
                onClick={() => onCheckout(shipping)}
                disabled={disabled}
                className="mt-5 w-full py-3 rounded-xl text-xs font-bold tracking-[0.2em] uppercase transition-all duration-200"
                style={{
                    background: disabled
                        ? "rgba(201,151,58,0.08)"
                        : "linear-gradient(135deg, #c9973a, #a07828)",
                    color: disabled ? "#4a3f2a" : "#080a0f",
                    border: "1px solid rgba(201,151,58,0.3)",
                    cursor: disabled ? "not-allowed" : "pointer",
                }}
                onMouseEnter={e => { if (!disabled) e.currentTarget.style.filter = "brightness(1.1)"; }}
                onMouseLeave={e => { e.currentTarget.style.filter = "none"; }}
            >
                {checkingOut ? t.orderProcessing : cards.length === 0 ? t.emptyCheckout : t.checkoutButton}
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
                    display: "flex", alignItems: "center", justifyContent: "center", fontSize: 28,
                }}>🃏</div>
            </div>
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

// ── Error ─────────────────────────────────────────────────────────────────────
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
                {t.retry}
            </button>
        </div>
    );
}

// ── Order confirmation banner ─────────────────────────────────────────────────
function OrderConfirmation({ shipping, onClose, t }) {
    useEffect(() => {
        const timer = setTimeout(onClose, 6000);
        return () => clearTimeout(timer);
    }, []);

    return (
        <div
            className="fixed bottom-8 left-1/2 z-50 flex items-start gap-4 rounded-2xl px-6 py-5 shadow-2xl"
            style={{
                transform: "translateX(-50%)",
                background: "rgba(13,17,23,0.97)",
                border: "1px solid rgba(201,151,58,0.4)",
                boxShadow: "0 8px 48px rgba(201,151,58,0.15)",
                animation: "slideUp .35s ease",
                minWidth: 320, maxWidth: 480,
            }}
        >
            <div style={{
                width: 36, height: 36, borderRadius: "50%", flexShrink: 0,
                background: "rgba(201,151,58,0.12)",
                border: "1px solid rgba(201,151,58,0.3)",
                display: "flex", alignItems: "center", justifyContent: "center",
                fontSize: 18,
            }}>✓</div>

            <div className="flex-1">
                <p className="font-bold text-sm" style={{ color: "#e8c06a", fontFamily: "Georgia,serif" }}>
                    {t.orderConfirmTitle}
                </p>
                <p className="text-xs mt-0.5" style={{ color: "#c8b98a" }}>
                    {shipping === "ship" ? t.orderConfirmShip : t.orderConfirmPickup}
                </p>
            </div>

            <button
                onClick={onClose}
                className="text-xs transition-opacity mt-0.5"
                style={{ color: "#7a6f5e", opacity: 0.7 }}
                onMouseEnter={e => e.currentTarget.style.opacity = "1"}
                onMouseLeave={e => e.currentTarget.style.opacity = "0.7"}
            >✕</button>
        </div>
    );
}

// ═════════════════════════════════════════════════════════════════════════════
//  Main ShoppingCart page
// ═════════════════════════════════════════════════════════════════════════════
export default function ShoppingCart({ language = "fr" }) {
    const navigate = useNavigate();
    const t = translations[language]?.shoppingCart ?? translations["fr"].shoppingCart;

    const { cart, loading, error, fetchByUserId, addCard, removeCard, removeAllOfCard, getCardCount, clearCart } =
        useShoppingCartStore();

    const user = useAuthStore(s => s.user);

    const [checkingOut, setCheckingOut] = useState(false);

    useEffect(() => {
        if (user?.id) fetchByUserId();
    }, [user?.id]);

    const cards = cart?.cards ?? [];

    const handleChangeQty = async (cardId, newQty) => {
        const currentQty = cards.filter(c => c.id === cardId).length;
        const diff = newQty - currentQty;
        if (diff === 0) return;
        if (diff > 0) {
            const cardDTO = cards.find(c => c.id === cardId);
            for (let i = 0; i < diff; i++) await addCard(cardDTO);
        } else {
            for (let i = 0; i < Math.abs(diff); i++) await removeCard(cardId);
        }
    };

    const handleCheckout = async (shippingChoice) => {
        if (checkingOut || cards.length === 0) return;
        setCheckingOut(true);
        try {
            const { AuthService } = await import("../service/AuthService.js");
            const token = AuthService.getToken();
            const res = await fetch(
                `/api/v1/cart/checkout?shippingMethod=${shippingChoice}`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        ...(token ? { Authorization: `Bearer ${token}` } : {}),
                    },
                }
            );
            if (!res.ok) throw new Error(`Erreur ${res.status}`);
            const purchasedCards = await res.json();
            clearCart();
            navigate("/thank-you", { state: { cards: purchasedCards, shipping: shippingChoice } });
        } catch (e) {
            // En cas d'erreur réseau, on navigue quand même avec les cartes locales
            clearCart();
            navigate("/thank-you", { state: { cards, shipping: shippingChoice } });
        }
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
                {loading && (
                    <div className="flex flex-col items-center justify-center min-h-[50vh] gap-4">
                        <div className="w-10 h-10 border-2 border-[#c9973a]/20 border-t-[#c9973a] rounded-full animate-spin" />
                        <p className="text-xs tracking-[0.3em] text-[#7a6f5e] uppercase">{t.loading}</p>
                    </div>
                )}

                {error && !loading && (
                    <CartError error={error} onRetry={() => fetchByUserId()} t={t} />
                )}

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
                                        {/* Groupe les cartes identiques en une seule ligne avec quantité */}
                                        {Object.values(
                                            cards.reduce((acc, card) => {
                                                const key = card.id;
                                                if (acc[key]) { acc[key].qty += 1; }
                                                else { acc[key] = { card, qty: 1 }; }
                                                return acc;
                                            }, {})
                                        ).map(({ card, qty }, i) => (
                                            <CartRow
                                                key={card.id}
                                                card={card}
                                                qty={qty}
                                                index={i}
                                                onRemove={removeAllOfCard}
                                                onChangeQty={handleChangeQty}
                                                t={t}
                                            />
                                        ))}
                                    </div>
                                </section>
                                <aside>
                                    <OrderSummary cards={cards} onCheckout={handleCheckout} checkingOut={checkingOut} t={t} />
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
                @keyframes slideUp {
                    from { opacity: 0; transform: translateX(-50%) translateY(20px); }
                    to   { opacity: 1; transform: translateX(-50%) translateY(0); }
                }
            `}</style>
        </div>
    );
}
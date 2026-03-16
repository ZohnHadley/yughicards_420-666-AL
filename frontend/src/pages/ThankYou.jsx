import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { translations } from "../locales/index.js";

const USD_TO_CAD = 1.36;

function cardPrice(card) {
    const raw = parseFloat(card?.card_prices?.[0]?.cardmarket_price ?? 0);
    return raw > 0 ? raw * USD_TO_CAD : 0;
}

function frameColor(card) {
    const ft = (card?.frameType ?? card?.type ?? "").toUpperCase();
    if (ft.includes("SPELL"))   return "#1d7853";
    if (ft.includes("TRAP"))    return "#8c2055";
    if (ft.includes("FUSION"))  return "#7b4fa0";
    if (ft.includes("SYNCHRO")) return "#c0c0c0";
    if (ft.includes("XYZ"))     return "#555";
    if (ft.includes("RITUAL"))  return "#3c7dca";
    if (ft.includes("LINK"))    return "#00527d";
    return "#c9973a";
}

export default function ThankYou({ language = "fr" }) {
    const navigate  = useNavigate();
    const location  = useLocation();
    const t = translations[language]?.thankYou ?? translations["fr"].thankYou;

    // Données passées via navigate("/thank-you", { state: { cards, shipping } })
    const cards    = location.state?.cards    ?? [];
    const shipping = location.state?.shipping ?? "pickup";

    // Groupe les cartes identiques (même id)
    const grouped = Object.values(
        cards.reduce((acc, card) => {
            if (acc[card.id]) { acc[card.id].qty += 1; }
            else { acc[card.id] = { card, qty: 1 }; }
            return acc;
        }, {})
    );

    const subtotal     = cards.reduce((s, c) => s + cardPrice(c), 0);
    const shippingCost = shipping === "ship" ? 3.99 : 0;
    const total        = subtotal + shippingCost;

    return (
        <div className="min-h-screen bg-[#080a0f] text-[#e8dcc8] font-serif">

            <div style={{ height: 2, background: "linear-gradient(90deg, transparent, #c9973a, transparent)" }} />

            <main className="max-w-2xl mx-auto px-6 py-16" style={{ animation: "fadeUp .45s ease" }}>

                {/* Icon */}
                <div className="flex justify-center mb-8">
                    <div style={{
                        width: 72, height: 72, borderRadius: "50%",
                        background: "rgba(201,151,58,0.1)",
                        border: "1px solid rgba(201,151,58,0.35)",
                        display: "flex", alignItems: "center", justifyContent: "center",
                        fontSize: 32,
                        boxShadow: "0 0 32px rgba(201,151,58,0.15)",
                    }}>
                        ✓
                    </div>
                </div>

                {/* Title */}
                <div className="text-center mb-10">
                    <p className="text-[10px] tracking-[0.4em] text-[#c9973a] uppercase mb-3 font-sans">
                        ⟡ Yughi Store
                    </p>
                    <h1 className="text-4xl font-black bg-gradient-to-br from-[#e8c06a] via-[#c9973a] to-[#a07828] bg-clip-text text-transparent mb-3"
                        style={{ fontFamily: "Georgia,serif" }}>
                        {t.title}
                    </h1>
                    <p className="text-sm italic" style={{ color: "#7a6f5e" }}>
                        {shipping === "ship" ? t.subtitleShip : t.subtitlePickup}
                    </p>
                </div>

                {/* Delivery badge */}
                <div className="flex justify-center mb-8">
                    <div className="flex items-center gap-2.5 rounded-full px-5 py-2"
                         style={{
                             background: "rgba(201,151,58,0.07)",
                             border: "1px solid rgba(201,151,58,0.25)",
                         }}>
                        <span style={{ fontSize: 16 }}>{shipping === "ship" ? "📦" : "🏪"}</span>
                        <span className="text-xs tracking-widest" style={{ color: "#c9973a" }}>
                            {shipping === "ship" ? t.shippingLabel : t.pickupLabel}
                        </span>
                    </div>
                </div>

                {/* Cards list */}
                {cards.length > 0 && (
                    <div className="rounded-xl overflow-hidden mb-6"
                         style={{ border: "1px solid rgba(201,151,58,0.15)" }}>

                        <div className="px-4 py-3"
                             style={{ background: "rgba(201,151,58,0.06)", borderBottom: "1px solid rgba(201,151,58,0.1)" }}>
                            <p className="text-[10px] tracking-[0.35em] uppercase" style={{ color: "#4a3f2a" }}>
                                {t.orderSummary} · {cards.length} {cards.length > 1 ? t.cards : t.card}
                            </p>
                        </div>

                        <div className="flex flex-col divide-y" style={{ divideColor: "rgba(201,151,58,0.08)" }}>
                            {grouped.map(({ card, qty }, i) => {
                                const imgUrl     = card.card_images?.[0]?.image_url_small ?? card.card_images?.[0]?.image_url;
                                const color      = frameColor(card);
                                const unitPrice  = cardPrice(card);
                                const totalPrice = (unitPrice * qty).toFixed(2);

                                return (
                                    <div key={card.id ?? i}
                                         className="flex items-center gap-3 px-4 py-3"
                                         style={{
                                             background: "rgba(13,17,23,0.6)",
                                             borderLeft: `3px solid ${color}`,
                                             borderBottom: i < grouped.length - 1 ? "1px solid rgba(201,151,58,0.08)" : "none",
                                             animation: `fadeUp .3s ease ${i * 50}ms both`,
                                         }}>
                                        <div className="shrink-0 rounded overflow-hidden"
                                             style={{ width: 36, height: 50, background: `${color}22` }}>
                                            {imgUrl
                                                ? <img src={imgUrl} alt={card.name} className="w-full h-full object-cover" />
                                                : <div className="w-full h-full flex items-center justify-center text-base">🃏</div>
                                            }
                                        </div>
                                        <div className="flex-1 min-w-0">
                                            <p className="text-sm font-bold truncate"
                                               style={{ fontFamily: "Georgia,serif", color: "#e8dcc8" }}>
                                                {card.name}
                                            </p>
                                            <p className="text-[11px] italic truncate"
                                               style={{ color, opacity: 0.8 }}>
                                                {card.type?.replaceAll("_", " ") ?? ""}
                                            </p>
                                        </div>

                                        {/* Qty badge */}
                                        {qty > 1 && (
                                            <div className="shrink-0 px-2 py-0.5 rounded-md text-xs font-bold"
                                                 style={{
                                                     background: "rgba(201,151,58,0.1)",
                                                     border: "1px solid rgba(201,151,58,0.25)",
                                                     color: "#c9973a",
                                                 }}>
                                                ×{qty}
                                            </div>
                                        )}

                                        <p className="text-sm font-bold shrink-0"
                                           style={{ color: "#e8c06a", fontFamily: "Georgia,serif" }}>
                                            ${totalPrice}
                                        </p>
                                    </div>
                                );
                            })}
                        </div>

                        {/* Totals */}
                        <div className="px-4 py-3 flex flex-col gap-1.5"
                             style={{ background: "rgba(201,151,58,0.04)", borderTop: "1px solid rgba(201,151,58,0.1)" }}>
                            <div className="flex justify-between text-xs" style={{ color: "#7a6f5e" }}>
                                <span>{t.subtotalLabel}</span>
                                <span>${subtotal.toFixed(2)}</span>
                            </div>
                            <div className="flex justify-between text-xs" style={{ color: "#7a6f5e" }}>
                                <span>{t.shippingCostLabel}</span>
                                <span>{shippingCost === 0 ? t.free : `$${shippingCost.toFixed(2)}`}</span>
                            </div>
                            <div className="flex justify-between text-sm font-bold mt-1"
                                 style={{ color: "#e8c06a", fontFamily: "Georgia,serif" }}>
                                <span>{t.totalLabel}</span>
                                <span>${total.toFixed(2)} <span className="text-xs font-normal" style={{ color: "#7a6f5e" }}>CAD</span></span>
                            </div>
                        </div>
                    </div>
                )}

                {/* Actions */}
                <div className="flex flex-col sm:flex-row gap-3 justify-center">
                    <button
                        onClick={() => navigate("/inventaire")}
                        className="text-xs tracking-widest px-6 py-3 rounded-xl transition-all duration-200"
                        style={{
                            background: "linear-gradient(135deg, #c9973a, #a07828)",
                            color: "#080a0f",
                            fontWeight: "bold",
                        }}
                        onMouseEnter={e => e.currentTarget.style.filter = "brightness(1.1)"}
                        onMouseLeave={e => e.currentTarget.style.filter = "none"}
                    >
                        {t.backToInventory}
                    </button>
                    <button
                        onClick={() => navigate("/")}
                        className="text-xs tracking-widest px-6 py-3 rounded-xl border transition-all duration-200"
                        style={{ color: "#9a8e7a", borderColor: "rgba(201,151,58,0.2)" }}
                        onMouseEnter={e => e.currentTarget.style.background = "rgba(201,151,58,0.06)"}
                        onMouseLeave={e => e.currentTarget.style.background = "transparent"}
                    >
                        {t.backHome}
                    </button>
                </div>

                <p className="text-center text-[10px] mt-8" style={{ color: "#2a2318" }}>
                    {t.footer}
                </p>
            </main>

            <style>{`
                @keyframes fadeUp {
                    from { opacity: 0; transform: translateY(14px); }
                    to   { opacity: 1; transform: translateY(0); }
                }
            `}</style>
        </div>
    );
}
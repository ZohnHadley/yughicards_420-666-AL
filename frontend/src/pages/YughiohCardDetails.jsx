import React, { useState, useMemo } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import {translations} from "../locales/index.js";
import {RARITY_PALETTE} from "../theme/rarityPalette.js";

const USD_TO_CAD = 1.36;

// Build rarity→style map for a single card's sets
function getRarityStyle(rarity) {
    if (!rarity) return RARITY_PALETTE[0];

    let hash = 0;
    for (let i = 0; i < rarity.length; i++) {
        hash = (hash * 31 + rarity.charCodeAt(i)) % RARITY_PALETTE.length;
    }

    return RARITY_PALETTE[hash];
}

// ── StatBar ────────────────────────────────────────────────────────────────
function StatBar({ label, value, max = 5000, color = "#c9973a" }) {
    const pct = Math.min(100, Math.round((value / max) * 100));
    return (
        <div className="flex items-center gap-3">
            <span className="text-[11px] font-mono font-bold w-10 shrink-0 tracking-widest" style={{ color }}>
                {label}
            </span>
            <div className="flex-1 h-1.5 rounded-full bg-white/5 overflow-hidden">
                <div
                    className="h-full rounded-full transition-all duration-700"
                    style={{ width: `${pct}%`, background: `linear-gradient(90deg, ${color}60, ${color})` }}
                />
            </div>
            <span className="text-xs font-mono font-bold tabular-nums w-12 text-right" style={{ color }}>
                {value}
            </span>
        </div>
    );
}

// ── InfoRow ────────────────────────────────────────────────────────────────
function InfoRow({ label, value, color = "#e8dcc8" }) {
    if (!value && value !== 0) return null;
    return (
        <div className="flex items-start gap-3 py-2 border-b border-white/5 last:border-0">
            <span className="text-[10px] tracking-[0.25em] uppercase text-[#7a6f5e] w-32 shrink-0 pt-0.5 font-sans">
                {label}
            </span>
            <span className="text-sm font-semibold" style={{ color, fontFamily: "Georgia,serif" }}>
                {value}
            </span>
        </div>
    );
}

// ── SetRow ─────────────────────────────────────────────────────────────────
function SetRow({ s, isSelected, onClick }) {
    const rStyle = getRarityStyle(s.set_rarity);
    const usd = parseFloat(s.set_price || 0);
    const cad = usd > 0 ? (usd * USD_TO_CAD).toFixed(2) : null;

    return (
        <button
            onClick={onClick}
            className="w-full text-left flex items-center justify-between gap-3 px-4 py-3 rounded-xl transition-all duration-200"
            style={{
                background: isSelected ? rStyle.b : "rgba(255,255,255,0.02)",
                border: `1px solid ${isSelected ? rStyle.e : "rgba(255,255,255,0.06)"}`,
                boxShadow: isSelected ? `0 0 12px ${rStyle.e}` : "none",
            }}
        >
            <div className="flex items-center gap-3 min-w-0">
                {/* Rarity dot */}
                <span className="w-2 h-2 rounded-full shrink-0" style={{ background: rStyle.c }} />
                <div className="min-w-0">
                    <p className="text-[11px] font-bold truncate" style={{ color: isSelected ? rStyle.c : "#e8dcc8" }}>
                        {s.set_name}
                    </p>
                    <p className="text-[10px] font-mono tracking-widest mt-0.5" style={{ color: rStyle.c, opacity: 0.7 }}>
                        {s.set_code}
                    </p>
                </div>
            </div>
            <div className="flex items-center gap-2 shrink-0">
                <span
                    className="text-[9px] font-bold px-2 py-[3px] rounded-full tracking-wide"
                    style={{ color: rStyle.c, background: rStyle.b, border: `1px solid ${rStyle.e}` }}
                >
                    {s.set_rarity}
                </span>
                {cad && (
                    <span className="text-xs font-bold text-[#e8c06a]">${cad}</span>
                )}
            </div>
        </button>
    );
}

// ── Main Page ──────────────────────────────────────────────────────────────
export default function YughiohCardDetails({ language = "fr" }) {
    const t = translations[language].yughiohCardDetails;
    const { state } = useLocation();
    const navigate = useNavigate();

    const card = state?.card;

    // Selected set — default to the one passed from inventory, or first available
    const allSets = card?.card_sets ?? [];
    const initialSet = state?.set ?? allSets[0] ?? null;
    const [selectedSet, setSelectedSet] = useState(initialSet);
    const [qty, setQty] = useState(1);
    const [toast, setToast] = useState(null);

    // Image — use full-res when available
    const allImages = card?.card_images ?? [];
    const selectedSetIdx = allSets.findIndex(s => s.set_code === selectedSet?.set_code);
    const img = allImages[selectedSetIdx >= 0 ? selectedSetIdx : 0];
    const imgUrl = img?.image_url ?? img?.image_url_small ?? state?.img?.image_url ?? state?.img?.image_url_small;

    const rStyle = getRarityStyle(selectedSet?.set_rarity);

    // Price from selected set or fallback
    const rawPrice = selectedSet?.set_price && parseFloat(selectedSet.set_price) > 0
        ? parseFloat(selectedSet.set_price)
        : parseFloat(card?.card_prices?.[0]?.cardmarket_price || 0);
    const cad = rawPrice > 0 ? (rawPrice * USD_TO_CAD).toFixed(2) : null;

    const oos = !card?.stock || card.stock <= 0;
    const maxQty = Math.min(3, card?.stock ?? 3);

    const isMonster = card?.type?.toUpperCase().includes("MONSTER");

    const handleAdd = () => {
        const label = [card.name, selectedSet?.set_code, selectedSet?.set_rarity].filter(Boolean).join(" · ");
        setToast(`✦ ${qty}× ${label} ${qty > 1 ? t.toastAddedPlural : t.toastAdded}`);
        setTimeout(() => setToast(null), 2500);
        setQty(1);
    };

    // ── Not found ─────────────────────────────────────────────────────────
    if (!card) {
        return (
            <div className="min-h-screen bg-[#080a0f] flex flex-col items-center justify-center gap-4">
                <p className="text-3xl text-[#e8c06a]" style={{ fontFamily: "Georgia,serif" }}>⚠</p>
                <p className="text-lg font-bold text-[#e8dcc8]">{t.notFound}</p>
                <p className="text-sm text-[#7a6f5e] italic">{t.notFoundSub}</p>
                <button
                    onClick={() => navigate(-1)}
                    className="mt-4 px-6 py-2 rounded-xl text-xs tracking-widest uppercase border border-[#c9973a]/30 text-[#c9973a] hover:bg-[#c9973a]/10 transition"
                >
                    {t.backToInventory}
                </button>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-[#080a0f] text-[#e8dcc8]" style={{ fontFamily: "Georgia,serif" }}>

            {/* ── Ambient glow ── */}
            <div
                className="fixed top-0 left-0 w-[700px] h-[700px] pointer-events-none opacity-20"
                style={{ background: `radial-gradient(circle, ${rStyle.c}22 0%, transparent 65%)`, transform: "translate(-20%, -20%)", transition: "background 0.6s ease" }}
            />

            {/* ── Header bar ── */}
            <header className="sticky top-0 z-40 px-8 py-4 border-b border-[#c9973a]/15 flex items-center gap-4"
                    style={{ background: "rgba(8,10,15,0.92)", backdropFilter: "blur(16px)" }}>
                <button
                    onClick={() => navigate(-1)}
                    className="text-xs tracking-widest uppercase text-[#9a8e7a] hover:text-[#e8c06a] transition-colors duration-200 flex items-center gap-2"
                >
                    <span className="text-base leading-none">←</span>
                    <span>{t.backToInventory}</span>
                </button>
                <div className="h-4 w-px bg-[#c9973a]/20" />
                <p className="text-xs tracking-[0.3em] text-[#c9973a] uppercase truncate">{card.name}</p>
            </header>

            <main className="px-8 py-10 max-w-6xl mx-auto">
                <div className="grid grid-cols-[320px_1fr] gap-12 items-start">

                    {/* ── LEFT: Card image + buy box ── */}
                    <div className="flex flex-col gap-5 sticky top-24">

                        {/* Card image */}
                        <div
                            className="relative rounded-2xl overflow-hidden"
                            style={{
                                background: `linear-gradient(145deg, #0c1420, #130e00)`,
                                border: `1px solid ${rStyle.e}`,
                                boxShadow: `0 0 40px ${rStyle.e}, 0 24px 48px rgba(0,0,0,0.7)`,
                                aspectRatio: "0.717",
                            }}
                        >
                            {imgUrl
                                ? <img
                                    src={imgUrl}
                                    alt={card.name}
                                    className="w-full h-full object-cover"
                                    style={{ animation: "fadeUp .4s ease both" }}
                                    onError={e => e.target.style.display = "none"}
                                />
                                : <div className="w-full h-full flex items-center justify-center text-[#7a6f5e] text-sm italic">
                                    no image
                                </div>
                            }
                            {/* Stock badge */}
                            <div className={`absolute top-3 right-3 text-[10px] font-bold px-2.5 py-1 rounded-lg leading-none
                                ${oos ? "bg-red-950/90 text-red-400" : "bg-black/75 text-emerald-400"}`}>
                                {oos ? t.outOfStock : `×${card.stock} ${t.inStock}`}
                            </div>
                        </div>

                        {/* ── Buy box ── */}
                        <div
                            className="rounded-2xl p-5 flex flex-col gap-4"
                            style={{ background: "rgba(13,17,23,0.9)", border: `1px solid ${rStyle.e}` }}
                        >
                            {/* Price */}
                            <div className="flex items-end justify-between">
                                <div>
                                    <p className="text-[10px] tracking-[0.3em] uppercase text-[#7a6f5e] mb-1 font-sans">{t.price}</p>
                                    {cad
                                        ? <p className="text-3xl font-black text-[#e8c06a]">
                                            ${cad}
                                            <span className="text-sm text-[#7a6f5e] font-normal ml-1">{t.cadCurrency}</span>
                                        </p>
                                        : <p className="text-2xl text-[#7a6f5e]">—</p>
                                    }
                                </div>
                                {cad && qty > 1 && (
                                    <div className="text-right">
                                        <p className="text-[10px] tracking-widest text-[#7a6f5e] uppercase font-sans">{t.subtotal}</p>
                                        <p className="text-lg font-bold text-[#c9a96e]">${(parseFloat(cad) * qty).toFixed(2)}</p>
                                    </div>
                                )}
                            </div>

                            {/* Qty + Add */}
                            {!oos ? (
                                <div className="flex flex-col gap-3">
                                    {/* Quantity selector */}
                                    <div className="flex items-center gap-3">
                                        <p className="text-[10px] tracking-[0.25em] uppercase text-[#7a6f5e] font-sans">{t.quantity}</p>
                                        <div className="flex items-center rounded-xl overflow-hidden border ml-auto" style={{ borderColor: rStyle.e }}>
                                            <button
                                                onClick={() => setQty(q => Math.max(1, q - 1))}
                                                disabled={qty <= 1}
                                                className="w-9 h-9 text-lg flex items-center justify-center transition hover:opacity-80 disabled:opacity-25 disabled:cursor-not-allowed"
                                                style={{ color: rStyle.c, background: rStyle.b }}
                                            >−</button>
                                            <span className="w-10 text-center text-sm font-bold tabular-nums" style={{ color: rStyle.c }}>
                                                {qty}
                                            </span>
                                            <button
                                                onClick={() => setQty(q => Math.min(maxQty, q + 1))}
                                                disabled={qty >= maxQty}
                                                className="w-9 h-9 text-lg flex items-center justify-center transition hover:opacity-80 disabled:opacity-25 disabled:cursor-not-allowed"
                                                style={{ color: rStyle.c, background: rStyle.b }}
                                            >+</button>
                                        </div>
                                    </div>

                                    {/* Add to cart button */}
                                    <button
                                        onClick={handleAdd}
                                        className="w-full py-3 rounded-xl text-sm font-black tracking-widest uppercase transition-all duration-200 hover:brightness-110 active:scale-95"
                                        style={{
                                            background: `linear-gradient(135deg, ${rStyle.c}dd, ${rStyle.c})`,
                                            color: "#080a0f",
                                            boxShadow: `0 4px 20px ${rStyle.c}40`,
                                        }}
                                    >
                                        {t.addToCart} ✦
                                    </button>
                                </div>
                            ) : (
                                <div className="w-full py-3 rounded-xl flex items-center justify-center text-sm font-bold tracking-widest uppercase text-red-400/50 border border-red-500/20 cursor-not-allowed">
                                    {t.outOfStock}
                                </div>
                            )}
                        </div>
                    </div>

                    {/* ── RIGHT: Card details ── */}
                    <div className="flex flex-col gap-8">

                        {/* Title + rarity */}
                        <div>
                            <div className="flex items-center gap-3 mb-2">
                                {selectedSet?.set_rarity && (
                                    <span
                                        className="text-[10px] font-bold px-3 py-1 rounded-full tracking-widest uppercase"
                                        style={{ color: rStyle.c, background: rStyle.b, border: `1px solid ${rStyle.e}` }}
                                    >
                                        {selectedSet.set_rarity}
                                    </span>
                                )}
                                {card.attribute && (
                                    <span className="text-[10px] tracking-widest uppercase text-[#7a6f5e] font-sans">{card.attribute}</span>
                                )}
                            </div>
                            <h1
                                className="text-4xl font-black leading-tight"
                                style={{
                                    background: `linear-gradient(135deg, #e8dcc8 0%, ${rStyle.c} 60%)`,
                                    WebkitBackgroundClip: "text",
                                    WebkitTextFillColor: "transparent",
                                }}
                            >
                                {card.name}
                            </h1>
                            {card.type && (
                                <p className="text-sm italic text-[#c9973a] mt-1 opacity-80">
                                    {card.type.replaceAll("_", " ")}
                                </p>
                            )}
                        </div>

                        {/* ── Stats (monsters only) ── */}
                        {isMonster && (card.atk !== undefined || card.def !== undefined || card.level || card.linkval || card.scale) && (
                            <div
                                className="rounded-2xl p-5 flex flex-col gap-3"
                                style={{ background: "rgba(13,17,23,0.7)", border: "1px solid rgba(255,255,255,0.06)" }}
                            >
                                {card.level != null && (
                                    <div className="flex items-center gap-2 mb-1">
                                        <span className="text-[10px] tracking-[0.3em] uppercase text-[#7a6f5e] font-sans">{t.level}</span>
                                        <div className="flex gap-1 ml-2">
                                            {Array.from({ length: Math.min(card.level, 12) }).map((_, i) => (
                                                <span key={i} style={{ color: rStyle.c }} className="text-sm leading-none">★</span>
                                            ))}
                                        </div>
                                        <span className="text-sm font-bold ml-1" style={{ color: rStyle.c }}>{card.level}</span>
                                    </div>
                                )}
                                {card.linkval != null && (
                                    <div className="flex items-center gap-2 mb-1">
                                        <span className="text-[10px] tracking-[0.3em] uppercase text-[#7a6f5e] font-sans">{t.linkVal}</span>
                                        <span className="text-sm font-bold ml-2" style={{ color: rStyle.c }}>⬡ {card.linkval}</span>
                                    </div>
                                )}
                                {card.scale != null && (
                                    <div className="flex items-center gap-2 mb-1">
                                        <span className="text-[10px] tracking-[0.3em] uppercase text-[#7a6f5e] font-sans">{t.scale}</span>
                                        <span className="text-sm font-bold ml-2" style={{ color: rStyle.c }}>{card.scale}</span>
                                    </div>
                                )}
                                {card.atk != null && <StatBar label={t.atk} value={card.atk} max={5000} color={rStyle.c} />}
                                {card.def != null && <StatBar label={t.def} value={card.def} max={5000} color="#9ca3af" />}
                            </div>
                        )}

                        {/* ── Info fields ── */}
                        <div
                            className="rounded-2xl p-5"
                            style={{ background: "rgba(13,17,23,0.7)", border: "1px solid rgba(255,255,255,0.06)" }}
                        >
                            <InfoRow label={t.type}      value={card.type?.replaceAll("_", " ")} color="#c9973a" />
                            <InfoRow label={t.race}      value={card.race} />
                            <InfoRow label={t.attribute} value={card.attribute} />
                            <InfoRow label={t.archetype} value={card.archetype} />
                        </div>

                        {/* ── Description ── */}
                        <div
                            className="rounded-2xl p-5"
                            style={{ background: "rgba(13,17,23,0.7)", border: "1px solid rgba(255,255,255,0.06)" }}
                        >
                            <p className="text-[10px] tracking-[0.35em] uppercase text-[#7a6f5e] mb-3 font-sans">{t.cardDesc}</p>
                            <p className="text-sm leading-relaxed text-[#b8a99a] italic" style={{ fontFamily: "Georgia,serif" }}>
                                {card.desc || card.card_desc || t.noDescription}
                            </p>
                        </div>

                        {/* ── Editions disponibles ── */}
                        <div>
                            <p className="text-[10px] tracking-[0.35em] uppercase text-[#7a6f5e] mb-3 font-sans">{t.cardSets}</p>
                            {allSets.length === 0 ? (
                                <p className="text-sm text-[#7a6f5e] italic">{t.noSets}</p>
                            ) : (
                                <div className="flex flex-col gap-2">
                                    {allSets.map((s, i) => (
                                        <SetRow
                                            key={`${s.set_code}-${i}`}
                                            s={s}
                                            isSelected={selectedSet?.set_code === s.set_code && selectedSet?.set_rarity === s.set_rarity}
                                            onClick={() => setSelectedSet(s)}
                                        />
                                    ))}
                                </div>
                            )}
                        </div>

                    </div>
                </div>
            </main>

            {/* Toast */}
            {toast && (
                <div
                    className="fixed bottom-8 right-8 bg-[#131920] border border-[#c9973a]/40 rounded-xl px-5 py-3 text-xs tracking-widest text-[#e8c06a] shadow-2xl z-50"
                    style={{ animation: "fadeUp .3s ease both" }}
                >
                    {toast}
                </div>
            )}

            <style>{`
                @keyframes fadeUp {
                    from { opacity:0; transform:translateY(10px) }
                    to   { opacity:1; transform:translateY(0) }
                }
            `}</style>
        </div>
    );
}
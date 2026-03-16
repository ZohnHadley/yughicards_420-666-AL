// AdminCardTile.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const DEFAULT_STYLE = {
    c: "#9ca3af",
    b: "rgba(156,163,175,0.08)",
    e: "rgba(156,163,175,0.25)"
};

export default function AdminCardTile({ card, set, img, rarityMap, onStockChange, onDelete, delay, t }) {
    const rStyle = (set?.set_rarity && rarityMap.get(set.set_rarity)) ?? DEFAULT_STYLE;
    const [delta, setDelta] = useState(1);
    const [loading, setLoading] = useState(false);
    const [confirmDelete, setConfirmDelete] = useState(false);
    const navigate = useNavigate();

    const rawPrice = set?.set_price && parseFloat(set.set_price) > 0
        ? parseFloat(set.set_price)
        : parseFloat(card.card_prices?.[0]?.cardmarket_price || 0);
    const cad = rawPrice > 0 ? (rawPrice * 1.36).toFixed(2) : null;

    const qty = card.quantity ?? 0;
    const oos = qty <= 0;
    const lowStock = qty > 0 && qty <= 3;
    const imgUrl = img?.image_url_small ?? img?.image_url;

    const stockColor = oos ? "#ef4444" : lowStock ? "#f59e0b" : "#34d399";

    const handle = async (fn) => {
        setLoading(true);
        try { await fn(); }
        finally { setLoading(false); }
    };

    const handleIncrement = (e) => {
        e.stopPropagation();
        handle(() => onStockChange(card.id, delta, "increment"));
    };

    const handleDecrement = (e) => {
        e.stopPropagation();
        handle(() => onStockChange(card.id, delta, "decrement"));
    };

    const handleDelete = (e) => {
        e.stopPropagation();
        handle(() => onDelete(card.id));
    };

    const handleClick = () => {
        navigate("/cardDetails", { state: { card, set, img } });
    };

    return (
        <div
            className="flex flex-col bg-[#0d1117] rounded-xl overflow-hidden group transition-all duration-300 hover:-translate-y-1.5 cursor-pointer"
            style={{
                border: `1px solid ${rStyle.e}`,
                animation: `fadeUp .3s ease ${delay}ms both`,
                opacity: loading ? 0.6 : 1,
                transition: "opacity 0.2s, transform 0.3s",
            }}
            onMouseEnter={e => e.currentTarget.style.boxShadow = `0 8px 28px rgba(0,0,0,.6), 0 0 14px ${rStyle.e}`}
            onMouseLeave={e => e.currentTarget.style.boxShadow = "none"}
            onClick={handleClick}
        >
            {/* Image */}
            <div className="relative overflow-hidden aspect-[0.71] bg-gradient-to-br from-[#0c1420] to-[#130e00]">
                {imgUrl
                    ? <img src={imgUrl} alt={card.name}
                           className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                           onError={e => e.target.style.display = "none"} />
                    : <div className="w-full h-full flex items-center justify-center text-[#7a6f5e] text-xs italic">no image</div>
                }
                <div className="absolute inset-x-0 bottom-0 h-2/5 bg-gradient-to-t from-[#0d1117] to-transparent" />

                {/* Stock badge top-right */}
                <div
                    className="absolute top-2 right-2 text-[10px] font-bold px-2 py-[3px] rounded-md leading-none"
                    style={{
                        background: oos ? "rgba(127,29,29,0.9)" : lowStock ? "rgba(120,80,0,0.9)" : "rgba(0,0,0,0.7)",
                        color: stockColor,
                        border: `1px solid ${stockColor}55`,
                    }}
                >
                    ×{qty}
                </div>

                {/* Admin badge top-left */}
                <div className="absolute top-2 left-2 text-[9px] font-bold px-1.5 py-[3px] rounded-md leading-none tracking-widest uppercase"
                     style={{ background: "rgba(239,68,68,0.15)", color: "#fca5a5", border: "1px solid rgba(239,68,68,0.3)" }}>
                    ADM
                </div>
            </div>

            {/* Body */}
            <div className="p-3 flex flex-col gap-2 flex-1">
                <p className="text-xs font-bold leading-snug line-clamp-2"
                   style={{ fontFamily: "Georgia,serif", color: "#e8dcc8" }}>
                    {card.name}
                </p>

                <div className="flex items-center justify-between gap-1 min-w-0">
                    {card.type && (
                        <p className="text-[11px] italic text-[#c9973a] opacity-80 truncate flex-1">
                            {card.type?.toString().replaceAll("_", " ")}
                        </p>
                    )}
                    {set?.set_rarity && (
                        <span
                            className="shrink-0 text-[9px] font-bold px-2 py-[3px] rounded-full tracking-wide leading-none whitespace-nowrap"
                            style={{ color: rStyle.c, background: rStyle.b, border: `1px solid ${rStyle.e}` }}>
                            {set.set_rarity}
                        </span>
                    )}
                </div>

                {set && (
                    <div className="text-[11px] leading-snug space-y-0.5">
                        {set.set_name && (
                            <p className="text-[#9a8e7a] line-clamp-1">{set.set_name}</p>
                        )}
                        {set.set_code && (
                            <p className="font-mono tracking-wider font-semibold"
                               style={{ color: rStyle.c, opacity: 0.8 }}>
                                {set.set_code}
                            </p>
                        )}
                    </div>
                )}

                <div className="pt-2 border-t border-white/5 mt-auto flex flex-col gap-2">

                    {/* Price */}
                    {cad && (
                        <div className="flex items-center justify-between">
                            <span className="text-sm font-bold text-[#e8c06a]">
                                ${cad} <span className="text-[10px] text-[#7a6f5e] font-normal">CAD</span>
                            </span>
                        </div>
                    )}

                    {/* Delta stepper + stock controls */}
                    <div className="flex items-center gap-1.5" onClick={e => e.stopPropagation()}>

                        {/* Delta stepper */}
                        <div className="flex items-center rounded-lg overflow-hidden border"
                             style={{ borderColor: rStyle.e }}>
                            <button
                                onClick={e => { e.stopPropagation(); setDelta(d => Math.max(1, d - 1)); }}
                                disabled={delta <= 1 || loading}
                                className="w-6 h-7 text-sm flex items-center justify-center transition-all hover:opacity-80 disabled:opacity-25 disabled:cursor-not-allowed"
                                style={{ color: rStyle.c, background: rStyle.b }}
                            >−</button>
                            <span className="w-6 text-center text-xs font-bold tabular-nums"
                                  style={{ color: rStyle.c }}>
                                {delta}
                            </span>
                            <button
                                onClick={e => { e.stopPropagation(); setDelta(d => Math.min(99, d + 1)); }}
                                disabled={delta >= 99 || loading}
                                className="w-6 h-7 text-sm flex items-center justify-center transition-all hover:opacity-80 disabled:opacity-25 disabled:cursor-not-allowed"
                                style={{ color: rStyle.c, background: rStyle.b }}
                            >+</button>
                        </div>

                        {/* +stock */}
                        <button
                            onClick={handleIncrement}
                            disabled={loading}
                            className="flex-1 h-7 rounded-lg text-[10px] font-bold tracking-wide uppercase transition-all hover:brightness-110 active:scale-95 disabled:opacity-40"
                            style={{ background: "rgba(52,211,153,0.12)", color: "#34d399", border: "1px solid rgba(52,211,153,0.3)" }}
                        >+{delta}</button>

                        {/* -stock */}
                        <button
                            onClick={handleDecrement}
                            disabled={loading || oos}
                            className="flex-1 h-7 rounded-lg text-[10px] font-bold tracking-wide uppercase transition-all hover:brightness-110 active:scale-95 disabled:opacity-40 disabled:cursor-not-allowed"
                            style={{ background: "rgba(239,68,68,0.1)", color: "#fca5a5", border: "1px solid rgba(239,68,68,0.25)" }}
                        >−{delta}</button>
                    </div>

                    {/* Delete */}
                    <div onClick={e => e.stopPropagation()}>
                        {!confirmDelete ? (
                            <button
                                onClick={e => { e.stopPropagation(); setConfirmDelete(true); }}
                                disabled={loading}
                                className="w-full h-6 rounded-lg flex items-center justify-center text-[9px] font-bold tracking-widest uppercase transition-all opacity-30 hover:opacity-80"
                                style={{ background: "rgba(239,68,68,0.08)", color: "#fca5a5", border: "1px solid rgba(239,68,68,0.2)" }}
                            >
                                🗑 {t?.deleteTitle ?? "Supprimer"}
                            </button>
                        ) : (
                            <div className="flex gap-1">
                                <button
                                    onClick={handleDelete}
                                    className="flex-1 h-6 rounded-lg text-[9px] font-bold tracking-wide uppercase transition-all"
                                    style={{ background: "rgba(239,68,68,0.25)", color: "#fca5a5", border: "1px solid rgba(239,68,68,0.4)" }}
                                >{t?.confirmYes ?? "Oui"}</button>
                                <button
                                    onClick={e => { e.stopPropagation(); setConfirmDelete(false); }}
                                    className="flex-1 h-6 rounded-lg text-[9px] font-bold tracking-wide uppercase transition-all"
                                    style={{ background: "rgba(201,151,58,0.08)", color: "#9a8e7a", border: "1px solid rgba(201,151,58,0.2)" }}
                                >{t?.confirmNo ?? "Non"}</button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}
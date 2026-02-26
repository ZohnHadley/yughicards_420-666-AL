// CardTile.jsx
import React, { useState } from "react";

const DEFAULT_STYLE = {
    c: "#9ca3af",
    b: "rgba(156,163,175,0.08)",
    e: "rgba(156,163,175,0.25)"
};

export default function CardTile({ card, set, img, rarityMap, onAdd, delay, t }) {
    const rStyle = (set?.set_rarity && rarityMap.get(set.set_rarity)) ?? DEFAULT_STYLE;
    const [qty, setQty] = useState(1);

    const rawPrice = set?.set_price && parseFloat(set.set_price) > 0
        ? parseFloat(set.set_price)
        : parseFloat(card.card_prices?.[0]?.cardmarket_price || 0);
    const cad = rawPrice > 0 ? (rawPrice * 1.36).toFixed(2) : null; // USD_TO_CAD

    const oos = !card.stock || card.stock <= 0;
    const maxQty = Math.min(3, card.stock ?? 3);
    const imgUrl = img?.image_url_small ?? img?.image_url;

    const handleAdd = (e) => {
        onAdd({ card, set, qty }, e);
        setQty(1);
    };

    return (
        <div
            className="flex flex-col bg-[#0d1117] rounded-xl overflow-hidden group transition-all duration-300 hover:-translate-y-1.5"
            style={{ border: `1px solid ${rStyle.e}`, animation: `fadeUp .3s ease ${delay}ms both` }}
            onMouseEnter={e => e.currentTarget.style.boxShadow = `0 8px 28px rgba(0,0,0,.6), 0 0 14px ${rStyle.e}`}
            onMouseLeave={e => e.currentTarget.style.boxShadow = "none"}
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
                <div className={`absolute top-2 right-2 text-[10px] font-bold px-2 py-[3px] rounded-md leading-none
                    ${oos ? "bg-red-950/90 text-red-400" : "bg-black/70 text-emerald-400"}`}>
                    {oos ? t.outOfStock : `×${card.stock}`}
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
                    <div className="flex items-center justify-between">
                        {cad
                            ? <span className="text-sm font-bold text-[#e8c06a]">
                                ${cad} <span className="text-[10px] text-[#7a6f5e] font-normal">CAD</span>
                              </span>
                            : <span className="text-sm text-[#7a6f5e]">—</span>
                        }
                        {cad && qty > 1 && (
                            <span className="text-[10px] text-[#9a8e7a]">
                                = ${(parseFloat(cad) * qty).toFixed(2)}
                            </span>
                        )}
                    </div>

                    {!oos && (
                        <div className="flex items-center gap-2">
                            <div className="flex items-center rounded-lg overflow-hidden border"
                                 style={{ borderColor: rStyle.e }}>
                                <button
                                    onClick={() => setQty(q => Math.max(1, q - 1))}
                                    disabled={qty <= 1}
                                    className="w-7 h-7 text-base flex items-center justify-center transition-all duration-150 hover:opacity-80 disabled:opacity-25 disabled:cursor-not-allowed"
                                    style={{ color: rStyle.c, background: rStyle.b }}
                                >−
                                </button>
                                <span className="w-6 text-center text-xs font-bold tabular-nums"
                                      style={{ color: rStyle.c }}>
                                    {qty}
                                </span>
                                <button
                                    onClick={() => setQty(q => Math.min(maxQty, q + 1))}
                                    disabled={qty >= maxQty}
                                    className="w-7 h-7 text-base flex items-center justify-center transition-all duration-150 hover:opacity-80 disabled:opacity-25 disabled:cursor-not-allowed"
                                    style={{ color: rStyle.c, background: rStyle.b }}
                                >+
                                </button>
                            </div>

                            <button
                                onClick={handleAdd}
                                className="flex-1 h-7 rounded-lg text-[10px] font-bold tracking-widest uppercase transition-all duration-200 hover:brightness-110 active:scale-95"
                                style={{ background: rStyle.b, color: rStyle.c, border: `1px solid ${rStyle.e}` }}
                                onMouseEnter={e => {
                                    e.currentTarget.style.background = rStyle.c;
                                    e.currentTarget.style.color = "#080a0f";
                                }}
                                onMouseLeave={e => {
                                    e.currentTarget.style.background = rStyle.b;
                                    e.currentTarget.style.color = rStyle.c;
                                }}
                            >
                                {t.addButton ?? "Ajouter"}
                            </button>
                        </div>
                    )}

                    {oos && (
                        <div
                            className="w-full h-7 rounded-lg flex items-center justify-center text-[10px] font-bold tracking-widest uppercase text-red-400/50 border border-red-500/20 cursor-not-allowed">
                            {t.outOfStock}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}
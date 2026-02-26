import React from "react";
import {useState} from "react";

const PALETTE = [
    {c: "#9ca3af", b: "rgba(156,163,175,0.08)", e: "rgba(156,163,175,0.25)"},
    {c: "#60a5fa", b: "rgba(96,165,250,0.08)", e: "rgba(96,165,250,0.28)"},
    {c: "#a78bfa", b: "rgba(167,139,250,0.08)", e: "rgba(167,139,250,0.3)"},
    {c: "#fbbf24", b: "rgba(251,191,36,0.08)", e: "rgba(251,191,36,0.35)"},
    {c: "#f472b6", b: "rgba(244,114,182,0.1)", e: "rgba(244,114,182,0.4)"},
    {c: "#e879f9", b: "rgba(232,121,249,0.1)", e: "rgba(232,121,249,0.45)"},
    {c: "#34d399", b: "rgba(52,211,153,0.08)", e: "rgba(52,211,153,0.3)"},
    {c: "#fb923c", b: "rgba(251,146,60,0.08)", e: "rgba(251,146,60,0.3)"},
    {c: "#e2e8f0", b: "rgba(226,232,240,0.06)", e: "rgba(226,232,240,0.3)"},
    {c: "#bfdbfe", b: "rgba(191,219,254,0.08)", e: "rgba(191,219,254,0.35)"},
    {c: "#fde68a", b: "rgba(253,230,138,0.08)", e: "rgba(253,230,138,0.35)"},
    {c: "#c9973a", b: "rgba(201,151,58,0.1)", e: "rgba(201,151,58,0.4)"},
];

const CardTile = ({key, card, rarityMap}) => {
    const DEFAULT_STYLE = PALETTE[0];
    const USD_TO_CAD = 1.36;


    const rStyle = (card?.set_rarity && rarityMap.get(card.set_rarity)) ?? DEFAULT_STYLE;
    const [qty, setQty] = useState(1);

    const rawPrice = card?.set_price && parseFloat(card.set_price) > 0
        ? parseFloat(card.set_price)
        : parseFloat(card.card_prices?.[0]?.cardmarket_price || 0);
    const cad = rawPrice > 0 ? (rawPrice * USD_TO_CAD).toFixed(2) : null;

    const oos    = !card.quantity_in_stock || card.quantity_in_stock <= 0;
    // const maxQty = Math.min(3, card.stock ?? 3);

    const cardType = card.type;
    const cardQuantityInStock = card.quantity_in_stock
    const listCardImages = card.card_images;
    const rarityPrice = card.set_price;

    // const handleAdd = (e) => {
    //     onAdd({card, card, qty}, e);
    //     setQty(card.quantity_in_stock); // reset after confirm
    // };

    return (
        <div
            className="flex flex-col bg-[#0d1117] rounded-xl overflow-hidden group transition-all duration-300 hover:-translate-y-1.5"
            // style={{ border: `1px solid ${rStyle.e}`, animation: `fadeUp .3s ease ${delay}ms both` }}
            // onMouseEnter={e => e.currentTarget.style.boxShadow = `0 8px 28px rgba(0,0,0,.6), 0 0 14px ${rStyle.e}`}
            onMouseLeave={e => e.currentTarget.style.boxShadow = "none"}
        >
            {/* Image */}
            <div className="relative overflow-hidden aspect-[0.71] bg-gradient-to-br from-[#0c1420] to-[#130e00]">
                {listCardImages[0]
                    ? <img src={listCardImages[0].image_url} alt={card.name}
                           className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                           onError={e => e.target.style.display = "none"}/>
                    : <div className="w-full h-full flex items-center justify-center text-[#7a6f5e] text-xs italic">no
                        image</div>
                }
                <div className="absolute inset-x-0 bottom-0 h-2/5 bg-gradient-to-t from-[#0d1117] to-transparent"/>
                <div className={`absolute top-2 right-2 text-[10px] font-bold px-2 py-[3px] rounded-md leading-none
                    ${cardQuantityInStock ? "bg-red-950/90 text-red-400" : "bg-black/70 text-emerald-400"}`}>
                    {cardQuantityInStock ? "Épuisé" : `×${cardQuantityInStock}`}
                </div>
            </div>

            {/* Body */}
            <div className="p-3 flex flex-col gap-2 flex-1">

                {/* Nom */}
                <p className="text-xs font-bold leading-snug line-clamp-2"
                   style={{fontFamily: "Georgia,serif", color: "#e8dcc8"}}>
                    {card.name}
                </p>

                {/* Type + Rareté sur la même ligne */}
                <div className="flex items-center justify-between gap-1 min-w-0">
                    {cardType && (
                        <p className="text-[11px] italic text-[#c9973a] opacity-80 truncate flex-1">
                            {card.type?.toString().replaceAll("_", " ")}
                        </p>
                    )}
                    {card?.set_rarity && (
                        <span className="shrink-0 text-[9px] font-bold px-2 py-[3px] rounded-full tracking-wide leading-none whitespace-nowrap"
                              style={{ color: rStyle.c, background: rStyle.b, border: `1px solid ${rStyle.e}` }}>
                            {card.set_rarity}
                        </span>
                    )}
                </div>

                {/* Set name + code */}
                {card.set_name}
                {card && (
                    <div className="text-[11px] leading-snug space-y-0.5">
                        {card.set_rarity && (
                            <p className="text-[#9a8e7a] line-clamp-1">{card.set_rarity}</p>
                        )}
                        {card.set_code && (
                            <p className="font-mono tracking-wider font-semibold"
                               style={{ color: rStyle.c, opacity: 0.8 }}>
                                {card.set_code}
                            </p>
                        )}
                    </div>
                )}

                {/* Prix */}
                <div className="pt-2 border-t border-white/5 mt-auto flex flex-col gap-2">

                    <div className="flex items-center justify-between">
                        {cad
                            ? <span className="text-sm font-bold text-[#e8c06a]">
                                ${cad} <span className="text-[10px] text-[#7a6f5e] font-normal">CAD</span>
                              </span>
                            : <span className="text-sm text-[#7a6f5e]">—</span>
                        }
                        {/* Sous-total si qty > 1 */}
                        {cad && qty > 1 && (
                            <span className="text-[10px] text-[#9a8e7a]">
                                = ${(parseFloat(cad) * qty).toFixed(2)}
                            </span>
                        )}
                    </div>

                    {/* Sélecteur quantité + bouton confirmer */}
                    {!oos && (
                        <div className="flex items-center gap-2">
                            {/* − / qty / + */}
                            <div className="flex items-center rounded-lg overflow-hidden border"
                                 style={{ borderColor: rStyle.e }}>
                                <button
                                    onClick={() => setQty(q => Math.max(1, q - 1))}
                                    disabled={qty <= 1}
                                    className="w-7 h-7 text-base flex items-center justify-center transition-all duration-150 hover:opacity-80 disabled:opacity-25 disabled:cursor-not-allowed"
                                    style={{ color: rStyle.c, background: rStyle.b }}
                                >−</button>
                                <span className="w-6 text-center text-xs font-bold tabular-nums"
                                      style={{ color: rStyle.c }}>
                                    {qty}
                                </span>
                                <button
                                    onClick={() => setQty(q => Math.min(maxQty, q + 1))}
                                    disabled={qty >= maxQty}
                                    className="w-7 h-7 text-base flex items-center justify-center transition-all duration-150 hover:opacity-80 disabled:opacity-25 disabled:cursor-not-allowed"
                                    style={{ color: rStyle.c, background: rStyle.b }}
                                >+</button>
                            </div>

                            {/* Bouton confirmer */}
                            <button
                                onClick={handleAdd}
                                className="flex-1 h-7 rounded-lg text-[10px] font-bold tracking-widest uppercase transition-all duration-200 hover:brightness-110 active:scale-95"
                                style={{ background: rStyle.b, color: rStyle.c, border: `1px solid ${rStyle.e}` }}
                                onMouseEnter={e => { e.currentTarget.style.background = rStyle.c; e.currentTarget.style.color = "#080a0f"; }}
                                onMouseLeave={e => { e.currentTarget.style.background = rStyle.b; e.currentTarget.style.color = rStyle.c; }}
                            >
                                Ajouter
                            </button>
                        </div>
                    )}

                    {/*/!* Épuisé fallback *!/*/}
                    {oos && (
                        <div className="w-full h-7 rounded-lg flex items-center justify-center text-[10px] font-bold tracking-widest uppercase text-red-400/50 border border-red-500/20 cursor-not-allowed">
                            Épuisé
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default CardTile;


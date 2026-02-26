import React, { useEffect, useState, useMemo } from "react";
import { useYughioInventoryStore } from "../store/YughiohInventoryStore.js";
import { translations } from "../locales/index.js";

const PAGE_SIZE = 20;
const USD_TO_CAD = 1.36;

const PALETTE = [
    { c: "#9ca3af", b: "rgba(156,163,175,0.08)", e: "rgba(156,163,175,0.25)" },
    { c: "#60a5fa", b: "rgba(96,165,250,0.08)",  e: "rgba(96,165,250,0.28)"  },
    { c: "#a78bfa", b: "rgba(167,139,250,0.08)", e: "rgba(167,139,250,0.3)"  },
    { c: "#fbbf24", b: "rgba(251,191,36,0.08)",  e: "rgba(251,191,36,0.35)"  },
    { c: "#f472b6", b: "rgba(244,114,182,0.1)",  e: "rgba(244,114,182,0.4)"  },
    { c: "#e879f9", b: "rgba(232,121,249,0.1)",  e: "rgba(232,121,249,0.45)" },
    { c: "#34d399", b: "rgba(52,211,153,0.08)",  e: "rgba(52,211,153,0.3)"   },
    { c: "#fb923c", b: "rgba(251,146,60,0.08)",  e: "rgba(251,146,60,0.3)"   },
    { c: "#e2e8f0", b: "rgba(226,232,240,0.06)", e: "rgba(226,232,240,0.3)"  },
    { c: "#bfdbfe", b: "rgba(191,219,254,0.08)", e: "rgba(191,219,254,0.35)" },
    { c: "#fde68a", b: "rgba(253,230,138,0.08)", e: "rgba(253,230,138,0.35)" },
    { c: "#c9973a", b: "rgba(201,151,58,0.1)",   e: "rgba(201,151,58,0.4)"   },
];

function buildRarityMap(cards) {
    const map = new Map();
    let i = 0;
    for (const card of cards) {
        for (const s of (card.card_sets ?? [])) {
            if (s.set_rarity && !map.has(s.set_rarity)) {
                map.set(s.set_rarity, PALETTE[i % PALETTE.length]);
                i++;
            }
        }
    }
    return map;
}

const DEFAULT_STYLE = PALETTE[0];

// ── CardTile ───────────────────────────────────────────────────────────────
function CardTile({ card, set, img, rarityMap, onAdd, delay }) {
    const rStyle = (set?.set_rarity && rarityMap.get(set.set_rarity)) ?? DEFAULT_STYLE;

    // Prix : utilise set_price si dispo (propre à ce set), sinon cardmarket comme fallback
    const rawPrice = set?.set_price && parseFloat(set.set_price) > 0
        ? parseFloat(set.set_price)
        : parseFloat(card.card_prices?.[0]?.cardmarket_price || 0);
    const cad = rawPrice > 0 ? (rawPrice * USD_TO_CAD).toFixed(2) : null;

    const oos    = !card.stock || card.stock <= 0;
    const imgUrl = img?.image_url_small ?? img?.image_url;

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
                    {oos ? "Épuisé" : `×${card.stock}`}
                </div>
            </div>

            {/* Body */}
            <div className="p-3 flex flex-col gap-2 flex-1">

                {/* Nom */}
                <p className="text-xs font-bold leading-snug line-clamp-2"
                   style={{ fontFamily: "Georgia,serif", color: "#e8dcc8" }}>
                    {card.name}
                </p>

                {/* Type */}
                {card.type && (
                    <p className="text-[11px] italic text-[#6a6050] line-clamp-1">
                        {card.type?.toString().replaceAll("_", " ")}
                    </p>
                )}

                {/* Rareté */}
                {set?.set_rarity && (
                    <span className="self-start text-[10px] font-bold px-2 py-[3px] rounded-full tracking-wide leading-none"
                          style={{ color: rStyle.c, background: rStyle.b, border: `1px solid ${rStyle.e}` }}>
                        {set.set_rarity}
                    </span>
                )}

                {/* Set name + code */}
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

                {/* Prix + bouton */}
                <div className="flex items-center justify-between pt-2 border-t border-white/5 mt-auto">
                    {cad
                        ? <span className="text-sm font-bold text-[#e8c06a]">
                            ${cad} <span className="text-[10px] text-[#7a6f5e] font-normal">CAD</span>
                          </span>
                        : <span className="text-sm text-[#7a6f5e]">—</span>
                    }
                    <button
                        onClick={(e) => onAdd({ card, set }, e)}
                        disabled={oos}
                        className="w-7 h-7 rounded-full border text-lg flex items-center justify-center transition-all duration-200 hover:scale-110 active:scale-95 disabled:opacity-25 disabled:cursor-not-allowed"
                        style={{ borderColor: rStyle.e, color: rStyle.c }}
                        onMouseEnter={e => { if (!oos) { e.currentTarget.style.background = rStyle.c; e.currentTarget.style.color = "#080a0f"; }}}
                        onMouseLeave={e => { e.currentTarget.style.background = "transparent"; e.currentTarget.style.color = rStyle.c; }}
                    >
                        +
                    </button>
                </div>
            </div>
        </div>
    );
}

// ── Page ───────────────────────────────────────────────────────────────────
export default function YughiohInventory({ language = "fr" }) {
    const t = translations[language].yughiohInventory;
    const { cards, loading, error, fetchAllCards, searchCards } = useYughioInventoryStore();
    const [search, setSearch] = useState("");
    const [page,   setPage]   = useState(0);
    const [toast,  setToast]  = useState(null);

    useEffect(() => {
        if (search.trim().length > 1) searchCards(search.trim(), page, PAGE_SIZE);
        else fetchAllCards(page, PAGE_SIZE);
    }, [page]);

    const handleSearch = (e) => {
        const val = e.target.value;
        setSearch(val);
        setPage(0);
        if (val.trim().length > 1) searchCards(val.trim(), 0, PAGE_SIZE);
        else if (!val.trim()) fetchAllCards(0, PAGE_SIZE);
    };

    const rarityMap = useMemo(() => buildRarityMap(cards), [cards]);

    // Flatten: 1 tuile par (carte × set)
    const allVariants = useMemo(() =>
        cards.flatMap((card) => {
            const sets = card.card_sets ?? [];
            if (sets.length === 0) {
                return [{ card, set: null, img: card.card_images?.[0], key: `${card.id}-noset` }];
            }
            return sets.map((set, i) => ({
                card,
                set,
                img: card.card_images?.[i] ?? card.card_images?.[0],
                key: `${card.id}-${set.set_code}-${i}`,
            }));
        }), [cards]);

// 👇 pagination réelle sur les tuiles
    const variants = useMemo(() => {
        const start = page * PAGE_SIZE;
        const end = start + PAGE_SIZE;
        return allVariants.slice(start, end);
    }, [allVariants, page]);

    useEffect(() => {
        window.scrollTo({ top: 0, behavior: "smooth" });
    }, [page]);

    const addToCart = ({ card, set }, e) => {
        e.stopPropagation();
        const label = [card.name, set?.set_code, set?.set_rarity].filter(Boolean).join(" · ");
        setToast(`✦ ${label} ajoutée`);
        setTimeout(() => setToast(null), 2200);
    };

    return (
        <div className="min-h-screen bg-[#080a0f] text-[#e8dcc8] font-serif">

            {/* Header */}
            <header className="px-8 pt-10 pb-6 border-b border-[#c9973a]/20 flex flex-wrap items-end justify-between gap-4">
                <div>
                    <p className="text-[10px] tracking-[0.4em] text-[#c9973a] uppercase mb-2 font-sans">⟡ Yughistore Collection</p>
                    <h1 className="text-5xl font-black tracking-tight bg-gradient-to-br from-[#e8c06a] via-[#c9973a] to-[#a07828] bg-clip-text text-transparent"
                        style={{ fontFamily: "Georgia,serif" }}>{t.title}</h1>
                    <p className="text-[#7a6f5e] italic mt-1 text-sm">{t.subtitle}</p>
                </div>
                <div className="flex items-center gap-3 flex-wrap">
                    <div className="relative">
                        <span className="absolute left-3 top-1/2 -translate-y-1/2 text-[#c9973a]/60 text-sm">⌕</span>
                        <input type="text" placeholder={t.searchPlaceholder} value={search} onChange={handleSearch}
                               className="bg-[#131920] border border-[#c9973a]/20 rounded-lg py-2 pl-8 pr-4 text-sm text-[#e8dcc8] placeholder-[#7a6f5e] italic outline-none focus:border-[#c9973a]/50 focus:ring-2 focus:ring-[#c9973a]/20 w-56 transition" />
                    </div>
                    <span className="text-xs tracking-widest text-[#c9973a] border border-[#c9973a]/20 bg-[#131920] rounded-full px-4 py-1.5">
                        {variants.length} {t.cardsLabel}
                    </span>
                </div>
            </header>

            {/* Légende raretés — taille augmentée */}
            {rarityMap.size > 0 && (
                <div className="px-8 pt-4 pb-2 flex flex-wrap gap-2">
                    {[...rarityMap.entries()].map(([name, s]) => (
                        <span key={name} className="text-xs px-3 py-1 rounded-full font-bold tracking-wide"
                              style={{ color: s.c, background: s.b, border: `1px solid ${s.e}` }}>
                            {name}
                        </span>
                    ))}
                </div>
            )}

            {/* Contenu */}
            <main className="px-8 py-6">
                {loading ? (
                    <div className="flex flex-col items-center justify-center min-h-[50vh] gap-4">
                        <div className="w-10 h-10 border-2 border-[#c9973a]/20 border-t-[#c9973a] rounded-full animate-spin" />
                        <p className="text-xs tracking-[0.3em] text-[#7a6f5e] uppercase">{t.loadingText}</p>
                    </div>
                ) : error ? (
                    <div className="flex items-center justify-center min-h-[50vh]">
                        <div className="border border-red-500/30 bg-red-500/5 rounded-xl p-8 text-center">
                            <p className="text-red-400 tracking-widest text-sm mb-2">⚠ Connexion perdue</p>
                            <p className="text-[#7a6f5e] italic text-sm">{error}</p>
                        </div>
                    </div>
                ) : (
                    <>
                        {/* Grille fixe 5 colonnes */}
                        <div className="grid grid-cols-5 gap-4">
                            {variants.map(({ card, set, img, key }, i) => (
                                <CardTile
                                    key={key}
                                    card={card}
                                    set={set}
                                    img={img}
                                    rarityMap={rarityMap}
                                    onAdd={addToCart}
                                    delay={Math.min(i * 18, 280)}
                                />
                            ))}
                        </div>

                        {variants.length === 0 && (
                            <p className="text-center text-[#7a6f5e] italic mt-20">{t.noCards}</p>
                        )}

                        {/* Pagination */}
                        <div className="flex items-center justify-center gap-3 mt-12">
                            <button onClick={() => setPage(p => Math.max(0, p - 1))} disabled={page === 0}
                                    className="px-5 py-2 text-xs tracking-widest border border-[#c9973a]/30 rounded-lg text-[#c9973a] hover:bg-[#c9973a]/10 transition disabled:opacity-30 disabled:cursor-not-allowed">
                                {t.prevPage}
                            </button>
                            <span className="text-xs text-[#7a6f5e] tracking-widest tabular-nums">Page {page + 1}</span>
                            <button onClick={() => setPage(p => p + 1)} disabled={cards.length < PAGE_SIZE}
                                    className="px-5 py-2 text-xs tracking-widest border border-[#c9973a]/30 rounded-lg text-[#c9973a] hover:bg-[#c9973a]/10 transition disabled:opacity-30 disabled:cursor-not-allowed">
                                {t.nextPage}
                            </button>
                        </div>
                    </>
                )}
            </main>

            {/* Toast */}
            {toast && (
                <div className="fixed bottom-8 right-8 bg-[#131920] border border-[#c9973a]/40 rounded-xl px-5 py-3 text-xs tracking-widest text-[#e8c06a] shadow-2xl z-50"
                     style={{ animation: "fadeUp .3s ease both" }}>
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
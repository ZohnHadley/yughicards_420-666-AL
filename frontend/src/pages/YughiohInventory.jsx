import React, {useEffect, useState, useMemo} from "react";
import {useYughioInventoryStore} from "../store/YughiohInventoryStore.js";
import {translations} from "../locales/index.js";
import CardTile from "../components/CardTile.jsx";
import {RARITY_PALETTE} from "../theme/rarityPalette.js";

const PAGE_SIZE = 20;
const USD_TO_CAD = 1.36;

function buildRarityMap(cards) {
    const map = new Map();
    let i = 0;
    for (const card of cards) {
        for (const s of (card.card_sets ?? [])) {
            if (s.set_rarity && !map.has(s.set_rarity)) {
                map.set(s.set_rarity, RARITY_PALETTE[i % RARITY_PALETTE.length]);
                i++;
            }
        }
    }
    return map;
}

export default function YughiohInventory({language = "fr"}) {
    const t = translations[language].yughiohInventory;
    const {cards, loading, error, fetchAllCards, searchCards} = useYughioInventoryStore();

    const [search, setSearch] = useState("");
    const [page, setPage] = useState(0);
    const [toast, setToast] = useState(null);

    // activeFilters: Set of "Monster" | "Spell" | "Trap"
    // Empty set = show all
    const [activeFilters, setActiveFilters] = useState(new Set());
    const [sortBy, setSortBy] = useState("default");
    const [stockOnly, setStockOnly] = useState(false);

    const TYPE_TABS = [
        {key: "Monster", label: t.filterMonster ?? "Monstre"},
        {key: "Spell",   label: t.filterSpell   ?? "Magie"},
        {key: "Trap",    label: t.filterTrap     ?? "Piège"},
    ];

    // Toggle a filter key:
    // - if it's the only active one → deselect (back to All)
    // - if it's active with others → just remove it
    // - if it's inactive → add it
    const handleFilterToggle = (key) => {
        setActiveFilters(prev => {
            const next = new Set(prev);
            if (next.has(key)) {
                next.delete(key);
            } else {
                next.add(key);
            }
            return next;
        });
        setPage(0);
    };

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

    const handleSortChange  = (val) => { setSortBy(val); setPage(0); };
    const handleStockToggle = ()    => { setStockOnly(s => !s); setPage(0); };

    const rarityMap = useMemo(() => buildRarityMap(cards), [cards]);

    const allVariants = useMemo(() =>
        cards.flatMap((card) => {
            const sets = card.card_sets ?? [];
            if (sets.length === 0) {
                return [{card, set: null, img: card.card_images, key: `${card.id}-noset`}];
            }
            return sets.map((set, i) => ({
                card,
                set,
                img: card.card_images?.[i] ?? card.card_images,
                key: `${card.id}-${set.set_code}-${i}`,
            }));
        }), [cards]);

    const filteredVariants = useMemo(() => {
        let list = allVariants;

        // Multi-select type filter — empty set means "All"
        if (activeFilters.size > 0) {
            list = list.filter(({card}) => {
                const type = card.type?.toUpperCase() ?? "";
                return (
                    (activeFilters.has("Monster") && type.includes("MONSTER")) ||
                    (activeFilters.has("Spell")   && type.includes("SPELL"))   ||
                    (activeFilters.has("Trap")    && type.includes("TRAP"))
                );
            });
        }

        if (stockOnly) {
            list = list.filter(({card}) => card.quantity > 0);
        }

        if (sortBy === "priceLow") {
            list = [...list].sort((a, b) => {
                const pa = parseFloat(a.set?.set_price || a.card.card_prices?.[0]?.cardmarket_price || 0);
                const pb = parseFloat(b.set?.set_price || b.card.card_prices?.[0]?.cardmarket_price || 0);
                return pa - pb;
            });
        } else if (sortBy === "priceHigh") {
            list = [...list].sort((a, b) => {
                const pa = parseFloat(a.set?.set_price || a.card.card_prices?.[0]?.cardmarket_price || 0);
                const pb = parseFloat(b.set?.set_price || b.card.card_prices?.[0]?.cardmarket_price || 0);
                return pb - pa;
            });
        }

        return list;
    }, [allVariants, activeFilters, sortBy, stockOnly]);

    const variants = useMemo(() => {
        const start = page * PAGE_SIZE;
        return filteredVariants.slice(start, start + PAGE_SIZE);
    }, [filteredVariants, page]);

    useEffect(() => {
        window.scrollTo({top: 0, behavior: "smooth"});
    }, [page]);

    const addToCart = ({card, set, qty}, e) => {
        e.stopPropagation();
        const label = [card.name, set?.set_code, set?.set_rarity].filter(Boolean).join(" · ");
        setToast(`✦ ${qty}× ${label} ajoutée${qty > 1 ? "s" : ""}`);
        setTimeout(() => setToast(null), 2200);
    };

    const isAllActive = activeFilters.size === 0;

    return (
        <div className="min-h-screen bg-[#080a0f] text-[#e8dcc8] font-serif">

            {/* Header */}
            <header className="px-8 pt-10 pb-6 border-b border-[#c9973a]/20 flex flex-wrap items-end justify-between gap-4">
                <div>
                    <p className="text-[10px] tracking-[0.4em] text-[#c9973a] uppercase mb-2 font-sans">
                        ⟡ {t.eyebrow}
                    </p>
                    <h1 className="text-5xl font-black tracking-tight bg-gradient-to-br from-[#e8c06a] via-[#c9973a] to-[#a07828] bg-clip-text text-transparent"
                        style={{fontFamily: "Georgia,serif"}}>{t.title}</h1>
                    <p className="text-[#7a6f5e] italic mt-1 text-sm">{t.subtitle}</p>
                </div>
                <div className="flex items-center gap-3 flex-wrap">
                    <div className="relative">
                        <span className="absolute left-3 top-1/2 -translate-y-1/2 text-[#c9973a]/60 text-sm">⌕</span>
                        <input type="text" placeholder={t.searchPlaceholder} value={search} onChange={handleSearch}
                               className="bg-[#131920] border border-[#c9973a]/20 rounded-lg py-2 pl-8 pr-4 text-sm text-[#e8dcc8] placeholder-[#7a6f5e] italic outline-none focus:border-[#c9973a]/50 focus:ring-2 focus:ring-[#c9973a]/20 w-56 transition"/>
                    </div>
                    <span className="text-xs tracking-widest text-[#c9973a] border border-[#c9973a]/20 bg-[#131920] rounded-full px-4 py-1.5">
                        {variants.length} {t.cardsLabel}
                    </span>
                </div>
            </header>

            {/* ── Filter Bar ── */}
            <div className="px-8 pt-4 pb-4 border-b border-[#c9973a]/10 flex flex-wrap items-center gap-3">

                {/* Type tabs — individual toggles + "Tout" resets all */}
                <div className="flex rounded-xl overflow-hidden" style={{border: "1px solid rgba(201,151,58,0.25)"}}>

                    {/* Tout — always visible, resets filters */}
                    <button
                        onClick={() => { setActiveFilters(new Set()); setPage(0); }}
                        className="px-5 py-2 text-xs font-bold tracking-widest uppercase transition-all duration-200"
                        style={{
                            background: isAllActive ? "rgba(201,151,58,0.22)" : "rgba(255,255,255,0.02)",
                            color: isAllActive ? "#e8c06a" : "#9a8e7a",
                            borderRight: "1px solid rgba(201,151,58,0.15)",
                        }}
                    >
                        {t.filterAll ?? "Tout"}
                    </button>

                    {/* Monster / Spell / Trap — individually toggleable */}
                    {TYPE_TABS.map(({key, label}, idx) => {
                        const isActive = activeFilters.has(key);
                        return (
                            <button
                                key={key}
                                onClick={() => handleFilterToggle(key)}
                                className="px-5 py-2 text-xs font-bold tracking-widest uppercase transition-all duration-200"
                                style={{
                                    background: isActive ? "rgba(201,151,58,0.22)" : "rgba(255,255,255,0.02)",
                                    color: isActive ? "#e8c06a" : "#9a8e7a",
                                    borderRight: idx < TYPE_TABS.length - 1 ? "1px solid rgba(201,151,58,0.15)" : "none",
                                    // Subtle underline indicator when active
                                    boxShadow: isActive ? "inset 0 -2px 0 #c9973a" : "none",
                                }}
                            >
                                {label}
                            </button>
                        );
                    })}
                </div>

                {/* Separator */}
                <div className="w-px h-6 bg-[#c9973a]/20 mx-1" />

                {/* Sort select */}
                <select
                    value={sortBy}
                    onChange={e => handleSortChange(e.target.value)}
                    className="px-4 py-2 rounded-xl text-xs font-bold tracking-wide uppercase outline-none transition-all cursor-pointer"
                    style={{
                        background: "rgba(255,255,255,0.04)",
                        color: "#c9a96e",
                        border: "1px solid rgba(201,151,58,0.25)",
                    }}
                >
                    <option value="default">{t.sortBy ?? "Trier par…"}</option>
                    <option value="priceLow">{t.sortPriceLow  ?? "Prix ↑"}</option>
                    <option value="priceHigh">{t.sortPriceHigh ?? "Prix ↓"}</option>
                </select>

                {/* In Stock toggle */}
                <button
                    onClick={handleStockToggle}
                    className="flex items-center gap-2 px-4 py-2 rounded-xl text-xs font-bold tracking-wide uppercase transition-all duration-200"
                    style={{
                        background: stockOnly ? "rgba(52,211,153,0.15)" : "rgba(255,255,255,0.04)",
                        color: stockOnly ? "#34d399" : "#9a8e7a",
                        border: `1px solid ${stockOnly ? "rgba(52,211,153,0.45)" : "rgba(201,151,58,0.25)"}`,
                    }}
                >
                    <span
                        className="w-2.5 h-2.5 rounded-full transition-colors shrink-0"
                        style={{background: stockOnly ? "#34d399" : "rgba(255,255,255,0.15)"}}
                    />
                    {t.inStock ?? "En stock"}
                </button>
            </div>

            {/* Rarity legend */}
            {rarityMap.size > 0 && (
                <div className="px-8 pt-4 pb-2 flex flex-wrap gap-2">
                    {[...rarityMap.entries()].map(([name, s]) => (
                        <span key={name} className="text-xs px-3 py-1 rounded-full font-bold tracking-wide"
                              style={{color: s.c, background: s.b, border: `1px solid ${s.e}`}}>
                            {name}
                        </span>
                    ))}
                </div>
            )}

            {/* Content */}
            <main className="px-8 py-6">
                {loading ? (
                    <div className="flex flex-col items-center justify-center min-h-[50vh] gap-4">
                        <div className="w-10 h-10 border-2 border-[#c9973a]/20 border-t-[#c9973a] rounded-full animate-spin"/>
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
                        <div className="grid grid-cols-5 gap-4">
                            {variants.map(({card, set, img, key}, i) => (
                                <CardTile
                                    key={key}
                                    card={card}
                                    set={set}
                                    img={img}
                                    rarityMap={rarityMap}
                                    onAdd={addToCart}
                                    delay={Math.min(i * 18, 280)}
                                    t={t}
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
                            <button onClick={() => setPage(p => p + 1)} disabled={variants.length < PAGE_SIZE}
                                    className="px-5 py-2 text-xs tracking-widest border border-[#c9973a]/30 rounded-lg text-[#c9973a] hover:bg-[#c9973a]/10 transition disabled:opacity-30 disabled:cursor-not-allowed">
                                {t.nextPage}
                            </button>
                        </div>
                    </>
                )}
            </main>

            {/* Toast */}
            {toast && (
                <div
                    className="fixed bottom-8 right-8 bg-[#131920] border border-[#c9973a]/40 rounded-xl px-5 py-3 text-xs tracking-widest text-[#e8c06a] shadow-2xl z-50"
                    style={{animation: "fadeUp .3s ease both"}}>
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
import React, { useEffect, useState } from "react";
import { useYughioInventoryStore } from "../store/YughiohInventoryStore.js";
import {translations} from "../locales/index.js";


const PAGE_SIZE = 20; // 5x4

export default function YughiohInventory({ language = "fr" }) {
    const t = translations[language].yughiohInventory;

    const { cards, loading, error, fetchAllCards, searchCards } = useYughioInventoryStore();
    const [search, setSearch] = useState("");
    const [page, setPage] = useState(0);
    const [toast, setToast] = useState(null);

    useEffect(() => {
        if (search.trim().length > 1) searchCards(search.trim(), page, PAGE_SIZE);
        else fetchAllCards(page, PAGE_SIZE);
    }, [page]);

    const handleSearch = (e) => {
        const val = e.target.value;
        setSearch(val);
        setPage(0);
        if (val.trim().length > 1) searchCards(val.trim(), 0, PAGE_SIZE);
        else if (val.trim() === "") fetchAllCards(0, PAGE_SIZE);
    };

    const addToCart = (card, e) => {
        e.stopPropagation();
        setToast(`✦ ${card.name} ${t.toastAdd}`);
        setTimeout(() => setToast(null), 2000);
    };

    return (
        <div className="min-h-screen bg-[#080a0f] text-[#e8dcc8] font-serif">

            {/* Header */}
            <header className="px-10 pt-10 pb-6 border-b border-[#c9973a]/20 flex flex-wrap items-end justify-between gap-4">
                <div>
                    <p className="text-[10px] tracking-[0.4em] text-[#c9973a] uppercase mb-2 font-sans">⟡ Yughistore Collection</p>
                    <h1 className="text-5xl font-black tracking-tight bg-gradient-to-br from-[#e8c06a] via-[#c9973a] to-[#a07828] bg-clip-text text-transparent" style={{ fontFamily: "Georgia, serif" }}>
                        {t.title}
                    </h1>
                    <p className="text-gray-400 italic mt-1">{t.subtitle}</p>
                </div>
                <div className="flex items-center gap-3 flex-wrap">
                    <div className="relative">
                        <span className="absolute left-3 top-1/2 -translate-y-1/2 text-[#c9973a]/60 text-sm">⌕</span>
                        <input
                            type="text"
                            placeholder={t.searchPlaceholder}
                            value={search}
                            onChange={handleSearch}
                            className="bg-[#131920] border border-[#c9973a]/20 rounded-lg py-2 pl-8 pr-4 text-sm text-[#e8dcc8] placeholder-[#7a6f5e] italic outline-none focus:border-[#c9973a]/50 focus:ring-2 focus:ring-[#c9973a]/20 w-56 transition"
                        />
                    </div>
                    <span className="text-xs tracking-widest text-[#c9973a] border border-[#c9973a]/20 bg-[#131920] rounded-full px-4 py-1.5">
            {cards.length} {t.cardsLabel}
          </span>
                </div>
            </header>

            {/* Content */}
            <main className="px-10 py-8">
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
                        <div className="grid grid-cols-5 gap-5">
                            {cards.map((card) => (
                                <div key={card.id} className="bg-[#0d1117] border border-[#c9973a]/20 rounded-xl overflow-hidden cursor-pointer group transition-all duration-300 hover:-translate-y-1.5 hover:shadow-lg hover:border-[#c9973a]/50 flex flex-col">
                                    <div className="relative overflow-hidden aspect-[0.71] bg-gradient-to-br from-[#0d1520] to-[#1a1400]">
                                        <img
                                            src={`http://localhost:8080/${card.imageUrl}`}
                                            alt={card.name}
                                            className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                                            onError={e => e.target.style.display = "none"}
                                        />
                                        <div className="absolute inset-x-0 bottom-0 h-1/2 bg-gradient-to-t from-[#0d1117] to-transparent" />
                                    </div>
                                    <div className="p-3 flex flex-col gap-2 flex-1">
                                        <p className="text-xs font-bold tracking-wide leading-snug line-clamp-2" style={{ fontFamily: "Georgia, serif" }}>
                                            {card.name}
                                        </p>
                                        {card.type && <p className="text-[11px] italic text-[#7a6f5e]">{card.type}</p>}
                                        <div className="flex items-center justify-between pt-2 border-t border-[#c9973a]/10 mt-auto">
                      <span className="text-sm font-bold text-[#e8c06a]">
                        {card.price ? `${card.price} €` : "—"}
                      </span>
                                            <button
                                                onClick={(e) => addToCart(card, e)}
                                                className="w-7 h-7 rounded-full border border-[#c9973a]/40 text-[#c9973a] text-lg flex items-center justify-center hover:bg-[#c9973a] hover:text-[#080a0f] transition-all duration-200 hover:scale-110 active:scale-95"
                                            >
                                                +
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>

                        {cards.length === 0 && (
                            <p className="text-center text-[#7a6f5e] italic mt-20">{t.noCards}</p>
                        )}

                        {/* Pagination */}
                        <div className="flex items-center justify-center gap-3 mt-12">
                            <button
                                onClick={() => setPage(p => Math.max(0, p - 1))}
                                disabled={page === 0}
                                className="px-5 py-2 text-xs tracking-widest border border-[#c9973a]/30 rounded-lg text-[#c9973a] hover:bg-[#c9973a]/10 transition disabled:opacity-30 disabled:cursor-not-allowed"
                            >
                                {t.prevPage}
                            </button>
                            <span className="text-xs text-[#7a6f5e] tracking-widest tabular-nums">Page {page + 1}</span>
                            <button
                                onClick={() => setPage(p => p + 1)}
                                disabled={cards.length < PAGE_SIZE}
                                className="px-5 py-2 text-xs tracking-widest border border-[#c9973a]/30 rounded-lg text-[#c9973a] hover:bg-[#c9973a]/10 transition disabled:opacity-30 disabled:cursor-not-allowed"
                            >
                                {t.nextPage}
                            </button>
                        </div>
                    </>
                )}
            </main>

            {toast && (
                <div className="fixed bottom-8 right-8 bg-[#131920] border border-[#c9973a]/40 rounded-xl px-5 py-3 text-xs tracking-widest text-[#e8c06a] shadow-2xl z-50">
                    {toast}
                </div>
            )}
        </div>
    );
}
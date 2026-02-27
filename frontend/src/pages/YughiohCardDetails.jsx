import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { translations } from "../locales/index.js";
import { RARITY_PALETTE } from "../theme/rarityPalette.js";
import {YughioCardService} from "../service/YughioInventoryService.js";

const USD_TO_CAD = 1.36;

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
                <span className="w-2 h-2 rounded-full shrink-0" style={{ background: rStyle.c }} />
                <div className="min-w-0">
                    {/* FIX 1 — nom du set toujours crème, jamais influencé par rStyle */}
                    <p className="text-[11px] font-bold truncate text-[#e8dcc8]">
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
    const allSets = card?.card_sets ?? [];
    const initialSet = state?.set ?? allSets[0] ?? null;

    const [selectedSet, setSelectedSet] = useState(initialSet);
    const [qty, setQty] = useState(1);
    const [toast, setToast] = useState(null);

    // FIX 2 — fetch complet depuis le backend pour avoir card_desc (effet)
    // getCardById est déjà dans ton service, aucun endpoint manquant pour ça
    const [fullCard, setFullCard] = useState(card ?? null);
    useEffect(() => {
        if (!card?.id) return;
        YughioCardService.getCardById(card.id)
            .then(data => setFullCard(data))
            .catch(() => {});
    }, [card?.id]);

    const allImages = fullCard?.card_images ?? [];
    const selectedSetIdx = allSets.findIndex(s => s.set_code === selectedSet?.set_code);
    const img = allImages[selectedSetIdx >= 0 ? selectedSetIdx : 0];
    const imgUrl = img?.image_url ?? img?.image_url_small
        ?? state?.img?.image_url ?? state?.img?.image_url_small;

    // rStyle : uniquement pour décorations (bordures, glows, badges)
    // JAMAIS appliqué au texte du titre card.name
    const rStyle = getRarityStyle(selectedSet?.set_rarity);

    const rawPrice = selectedSet?.set_price && parseFloat(selectedSet.set_price) > 0
        ? parseFloat(selectedSet.set_price)
        : parseFloat(fullCard?.card_prices?.[0]?.cardmarket_price || 0);
    const cad = rawPrice > 0 ? (rawPrice * USD_TO_CAD).toFixed(2) : null;

    const oos = !fullCard?.stock || fullCard.stock <= 0;
    const maxQty = Math.min(3, fullCard?.stock ?? 3);

    const typeUpper  = fullCard?.type?.toString().toUpperCase() ?? "";
    const isMonster  = typeUpper.includes("MONSTER");
    const isXyz      = typeUpper.includes("XYZ");
    const isLink     = typeUpper.includes("LINK");
    const isPendulum = typeUpper.includes("PENDULUM");

    // Toutes les props monstre sont dans cardProperties (PropertiesMonsterCard)
    // Les enums (race, attribute) sont sérialisés comme { name: "DARK" } ou juste "DARK"
    const cp = fullCard?.cardProperties ?? {};
    const cardAtk       = cp.atk       ?? null;
    const cardDef       = cp.def       ?? null;
    const cardLevel     = cp.level     ?? null;
    const cardLinkval   = cp.linkval   ?? null;
    const cardScale     = cp.scale     ?? null;
    // Les enums Jackson sérialisent soit le string directement soit { name: "DARK" }
    const cardRace      = cp.race?.name      ?? cp.race      ?? null;
    const cardAttribute = cp.attribute?.name ?? cp.attribute ?? null;

    // Texte d'effet : champ 'description' dans YughioCard (mappé depuis 'desc' YGOPRODeck)
    const effectText = fullCard?.description ?? null;

    const handleAdd = () => {
        const label = [fullCard?.name, selectedSet?.set_code, selectedSet?.set_rarity].filter(Boolean).join(" · ");
        setToast(`✦ ${qty}× ${label} ${qty > 1 ? t.toastAddedPlural : t.toastAdded}`);
        setTimeout(() => setToast(null), 2500);
        setQty(1);
    };

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

            {/* Ambient glow */}
            <div
                className="fixed top-0 left-0 w-[700px] h-[700px] pointer-events-none opacity-20"
                style={{ background: `radial-gradient(circle, ${rStyle.c}22 0%, transparent 65%)`, transform: "translate(-20%, -20%)", transition: "background 0.6s ease" }}
            />

            {/* Header */}
            <header className="sticky top-0 z-40 px-8 py-4 border-b border-[#c9973a]/15 flex items-center gap-4"
                    style={{ background: "rgba(8,10,15,0.92)", backdropFilter: "blur(16px)" }}>
                <button
                    onClick={() => navigate(-1)}
                    className="text-xs tracking-widest uppercase text-[#9a8e7a] hover:text-[#e8c06a] transition-colors duration-200 flex items-center gap-2"
                >
                    <span>{t.backToInventory}</span>
                </button>
            </header>

            <main className="px-8 py-10 max-w-6xl mx-auto">
                <div className="grid grid-cols-[320px_1fr] gap-12 items-start">

                    {/* ── LEFT: image + buy box ── */}
                    <div className="flex flex-col gap-5 sticky top-24">

                        <div
                            className="relative rounded-2xl overflow-hidden"
                            style={{
                                background: "linear-gradient(145deg, #0c1420, #130e00)",
                                border: `1px solid ${rStyle.e}`,
                                boxShadow: `0 0 40px ${rStyle.e}, 0 24px 48px rgba(0,0,0,0.7)`,
                                aspectRatio: "0.717",
                            }}
                        >
                            {imgUrl
                                ? <img
                                    key={imgUrl}
                                    src={imgUrl}
                                    alt={fullCard?.name}
                                    className="w-full h-full object-cover"
                                    style={{ animation: "fadeUp .4s ease both" }}
                                    onError={e => e.target.style.display = "none"}
                                />
                                : <div className="w-full h-full flex items-center justify-center text-[#7a6f5e] text-sm italic">no image</div>
                            }
                            <div className={`absolute top-3 right-3 text-[10px] font-bold px-2.5 py-1 rounded-lg leading-none
                                ${oos ? "bg-red-950/90 text-red-400" : "bg-black/75 text-emerald-400"}`}>
                                {oos ? t.outOfStock : `×${fullCard?.stock} ${t.inStock}`}
                            </div>
                        </div>

                        {/* Buy box */}
                        <div className="rounded-2xl p-5 flex flex-col gap-4"
                             style={{ background: "rgba(13,17,23,0.9)", border: `1px solid ${rStyle.e}` }}>
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

                            {!oos ? (
                                <div className="flex flex-col gap-3">
                                    <div className="flex items-center gap-3">
                                        <p className="text-[10px] tracking-[0.25em] uppercase text-[#7a6f5e] font-sans">{t.quantity}</p>
                                        <div className="flex items-center rounded-xl overflow-hidden border ml-auto" style={{ borderColor: rStyle.e }}>
                                            <button onClick={() => setQty(q => Math.max(1, q - 1))} disabled={qty <= 1}
                                                    className="w-9 h-9 text-lg flex items-center justify-center transition hover:opacity-80 disabled:opacity-25 disabled:cursor-not-allowed"
                                                    style={{ color: rStyle.c, background: rStyle.b }}>−</button>
                                            <span className="w-10 text-center text-sm font-bold tabular-nums" style={{ color: rStyle.c }}>{qty}</span>
                                            <button onClick={() => setQty(q => Math.min(maxQty, q + 1))} disabled={qty >= maxQty}
                                                    className="w-9 h-9 text-lg flex items-center justify-center transition hover:opacity-80 disabled:opacity-25 disabled:cursor-not-allowed"
                                                    style={{ color: rStyle.c, background: rStyle.b }}>+</button>
                                        </div>
                                    </div>
                                    <button onClick={handleAdd}
                                            className="w-full py-3 rounded-xl text-sm font-black tracking-widest uppercase transition-all duration-200 hover:brightness-110 active:scale-95"
                                            style={{ background: `linear-gradient(135deg, ${rStyle.c}dd, ${rStyle.c})`, color: "#080a0f", boxShadow: `0 4px 20px ${rStyle.c}40` }}>
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

                    {/* ── RIGHT: details ── */}
                    <div className="flex flex-col gap-8">

                        {/* Title — FIX 1 : gradient FIXE doré, jamais lié à rStyle */}
                        <div>
                            <div className="flex items-center gap-3 mb-2">
                                {selectedSet?.set_rarity && (
                                    <span className="text-[10px] font-bold px-3 py-1 rounded-full tracking-widest uppercase"
                                          style={{ color: rStyle.c, background: rStyle.b, border: `1px solid ${rStyle.e}` }}>
                                        {selectedSet.set_rarity}
                                    </span>
                                )}
                            </div>
                            <h1
                                className="text-4xl font-black leading-tight"
                                style={{
                                    /* Gradient fixe — ne change JAMAIS quand on clique une édition */
                                    background: "linear-gradient(135deg, #e8dcc8 0%, #c9973a 70%)",
                                    WebkitBackgroundClip: "text",
                                    WebkitTextFillColor: "transparent",
                                }}
                            >
                                {fullCard?.name}
                            </h1>
                            {fullCard?.type && (
                                <p className="text-sm italic text-[#c9973a] mt-1 opacity-80">
                                    {fullCard.type.replaceAll("_", " ")}
                                </p>
                            )}
                        </div>

                        {/* Boîte monstre — toutes les lignes toujours visibles, "—" si données pas encore fetchées */}
                        {isMonster && (
                            <div className="rounded-2xl overflow-hidden"
                                 style={{ background: "rgba(13,17,23,0.7)", border: "1px solid rgba(255,255,255,0.06)" }}>

                                {/* ── 1. ATTRIBUTE ── */}
                                <div className="flex items-center gap-4 px-5 py-3 border-b border-white/5">
                                    <span className="text-[10px] tracking-[0.25em] uppercase text-[#7a6f5e] w-32 shrink-0 font-sans">{t.attribute}</span>
                                    <div className="flex items-center gap-2">
                                        {cardAttribute && (
                                            <img
                                                src={`/images/Attributes/${cardAttribute}.png`}
                                                alt={cardAttribute}
                                                className="w-6 h-6 object-contain"
                                                onError={e => e.target.style.display = "none"}
                                            />
                                        )}
                                        <span className="text-sm font-bold text-[#e8dcc8]">
                                            {cardAttribute ?? "—"}
                                        </span>
                                    </div>
                                </div>

                                {/* ── 2. LEVEL / RANK / LINK RATING ── toujours affiché */}
                                <div className="flex items-center gap-4 px-5 py-3 border-b border-white/5">
                                    <span
                                        className="text-[10px] tracking-[0.25em] uppercase text-[#7a6f5e] w-32 shrink-0 font-sans">
                                        {isLink ? t.linkVal : isXyz ? t.rank : isPendulum ? t.scale : t.level}
                                    </span>
                                    <div className="flex items-center gap-2">
                                        {/* Étoiles si valeur disponible */}
                                        {cardLevel != null && !isLink && (
                                            <div className="flex gap-0.5">
                                                {Array.from({ length: Math.min(cardLevel, 13) }).map((_, i) => (
                                                    <span key={i} className="text-sm leading-none"
                                                          style={{ color: isXyz ? "#a78bfa" : "#c9973a" }}>
                                                        {isXyz ? "✦" : "★"}
                                                    </span>
                                                ))}
                                            </div>
                                        )}
                                        {isLink && cardLinkval != null && (
                                            <span className="text-base text-[#34d399]">⬡</span>
                                        )}
                                        <span className="text-sm font-bold text-[#e8dcc8] ml-1">
                                            {isLink
                                                ? (cardLinkval ?? "—")
                                                : (cardLevel ?? "—")}
                                        </span>
                                        {isPendulum && cardScale != null && (
                                            <span className="text-xs text-[#7a6f5e] ml-2">({t.scale}: {cardScale})</span>
                                        )}
                                    </div>
                                </div>

                                {/* ── 4. Type ── */}
                                <div className="flex items-center gap-4 px-5 py-3 border-b border-white/5">
                                    <span className="text-[10px] tracking-[0.25em] uppercase text-[#7a6f5e] w-32 shrink-0 font-sans">{t.race}</span>
                                    <span className="text-sm font-semibold text-[#e8dcc8]">
                                        {cardRace ?? "—"}
                                    </span>
                                </div>

                                {/* ── 5. ATK ── */}
                                <div className="flex items-center gap-4 px-5 py-3 border-b border-white/5">
                                    <span className="text-[10px] tracking-[0.25em] uppercase text-[#7a6f5e] w-32 shrink-0 font-sans">{t.atk}</span>
                                    {cardAtk != null
                                        ? <div className="flex-1"><StatBar label="" value={cardAtk} max={5000} color="#c9973a" /></div>
                                        : <span className="text-sm font-bold text-[#7a6f5e]">—</span>
                                    }
                                </div>

                                {/* ── 6. DEF (pas affiché pour Link) ── */}
                                {!isLink && (
                                    <div className="flex items-center gap-4 px-5 py-4">
                                        <span className="text-[10px] tracking-[0.25em] uppercase text-[#7a6f5e] w-32 shrink-0 font-sans">{t.def}</span>
                                        {cardDef != null
                                            ? <div className="flex-1"><StatBar label="" value={cardDef} max={5000} color="#9ca3af" /></div>
                                            : <span className="text-sm font-bold text-[#7a6f5e]">—</span>
                                        }
                                    </div>
                                )}
                            </div>
                        )}

                        {/* Infos pour Spell/Trap (pas monstre) */}
                        {!isMonster && (
                            <div className="rounded-2xl p-5"
                                 style={{ background: "rgba(13,17,23,0.7)", border: "1px solid rgba(255,255,255,0.06)" }}>
                                {/* Spell/Trap subtype icon */}
                                <div className="flex items-center gap-3 py-2 border-b border-white/5 mb-1">
                                    <span className="text-[10px] tracking-[0.25em] uppercase text-[#7a6f5e] w-32 shrink-0 font-sans">{t.race}</span>
                                    <div className="flex items-center gap-2">
                                        {(() => {
                                            // Map race/subtype to filename
                                            const spellTrapMap = {
                                                "Normal":      typeUpper.includes("SPELL") ? "SPELL" : "TRAP",
                                                "Continuous":  "Continuous",
                                                "Counter":     "Counter",
                                                "Equip":       "Equip",
                                                "Field":       "Field",
                                                "Quick-Play":  "Quick-Play",
                                                "Ritual":      "Ritual",
                                            };
                                            const iconFile = spellTrapMap[cardRace] ?? (typeUpper.includes("SPELL") ? "SPELL" : "TRAP");
                                            return (
                                                <img
                                                    src={`/images/Attributes/${iconFile}.png`}
                                                    alt={cardRace}
                                                    className="w-6 h-6 object-contain"
                                                    onError={e => e.target.style.display = "none"}
                                                />
                                            );
                                        })()}
                                        <span className="text-sm font-semibold text-[#e8dcc8]">{cardRace ?? "—"}</span>
                                    </div>
                                </div>
                                <InfoRow label={t.archetype} value={fullCard?.archetype} />
                            </div>
                        )}

                        {/* FIX 2 — Description / Effet : texte fetchable via card_desc */}
                        <div className="rounded-2xl p-5"
                             style={{ background: "rgba(13,17,23,0.7)", border: "1px solid rgba(255,255,255,0.06)" }}>
                            <p className="text-[10px] tracking-[0.35em] uppercase text-[#7a6f5e] mb-3 font-sans">
                                {isMonster ? t.cardEffect : t.cardDesc}
                            </p>
                            <p className="text-sm leading-relaxed text-[#b8a99a] italic whitespace-pre-line"
                               style={{ fontFamily: "Georgia,serif" }}>
                                {effectText ?? t.noDescription}
                            </p>
                        </div>

                        {/* Editions */}
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
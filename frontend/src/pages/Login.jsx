import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore.js";
import { translations } from "../locales/index.js";

const GLYPHS = ["⟡", "✦", "◈", "⬡", "✧"];

function FloatingGlyph({ glyph, style }) {
    return (
        <span style={{
            position: "absolute",
            fontFamily: "Georgia, serif",
            color: "rgba(201,151,58,0.07)",
            userSelect: "none",
            pointerEvents: "none",
            animation: "floatGlyph 8s ease-in-out infinite",
            ...style,
        }}>
            {glyph}
        </span>
    );
}

function InputField({ label, type = "text", value, onChange, placeholder, error, autoComplete }) {
    const [focused, setFocused] = useState(false);
    return (
        <div style={{ display: "flex", flexDirection: "column", gap: 6 }}>
            <label style={{
                fontSize: "0.65rem", letterSpacing: "0.2em", textTransform: "uppercase",
                color: focused ? "#c9973a" : "#7a6f5e", fontFamily: "Georgia, serif", transition: "color 0.2s",
            }}>
                {label}
            </label>
            <input
                type={type} value={value} onChange={onChange} placeholder={placeholder}
                autoComplete={autoComplete}
                onFocus={() => setFocused(true)} onBlur={() => setFocused(false)}
                style={{
                    background: "rgba(13,17,23,0.8)",
                    border: `1px solid ${error ? "rgba(239,68,68,0.5)" : focused ? "rgba(201,151,58,0.6)" : "rgba(201,151,58,0.2)"}`,
                    borderRadius: 8, padding: "0.75rem 1rem", color: "#e8dcc8",
                    fontSize: "0.9rem", fontFamily: "Georgia, serif", outline: "none",
                    transition: "border-color 0.2s, box-shadow 0.2s",
                    boxShadow: focused ? "0 0 0 3px rgba(201,151,58,0.08)" : "none",
                    width: "100%", boxSizing: "border-box",
                }}
            />
            {error && <p style={{ color: "#f87171", fontSize: "0.72rem", margin: 0 }}>{error}</p>}
        </div>
    );
}

export default function Login({ language = "fr" }) {
    const t = translations[language]?.login ?? translations["fr"].login;
    const navigate = useNavigate();
    const { login, loading, error, clearError } = useAuthStore();

    const [email, setEmail]       = useState("");
    const [password, setPassword] = useState("");
    const [fieldErrors, setFieldErrors] = useState({});

    const validate = () => {
        const errs = {};
        if (!email.trim()) errs.email = t.errorRequired;
        else if (!/\S+@\S+\.\S+/.test(email)) errs.email = t.errorEmail;
        if (!password) errs.password = t.errorRequired;
        return errs;
    };

    const handleSubmit = async () => {
        const errs = validate();
        if (Object.keys(errs).length > 0) { setFieldErrors(errs); return; }
        setFieldErrors({});
        clearError();
        try {
            await login(email, password);
            navigate("/inventory");
        } catch {
            // erreur déjà dans le store
        }
    };

    return (
        <div style={{
            minHeight: "100vh", background: "#080a0f",
            display: "flex", alignItems: "center", justifyContent: "center",
            position: "relative", overflow: "hidden", fontFamily: "Georgia, serif",
        }}>
            <div style={{
                position: "absolute", inset: 0,
                background: "radial-gradient(ellipse at 30% 20%, rgba(201,151,58,0.06) 0%, transparent 60%), radial-gradient(ellipse at 70% 80%, rgba(120,60,20,0.08) 0%, transparent 50%)",
                pointerEvents: "none",
            }} />

            {GLYPHS.map((g, i) => (
                <FloatingGlyph key={i} glyph={g} style={{
                    fontSize: `${5 + i * 3}rem`,
                    top: `${10 + i * 18}%`,
                    left: i % 2 === 0 ? `${5 + i * 3}%` : undefined,
                    right: i % 2 !== 0 ? `${5 + i * 2}%` : undefined,
                    animationDelay: `${i * 1.3}s`,
                }} />
            ))}

            <div style={{ position: "absolute", top: 0, left: 0, right: 0, height: 2, background: "linear-gradient(90deg, transparent, #c9973a, transparent)" }} />

            {/* Card */}
            <div style={{
                position: "relative", width: "100%", maxWidth: 420, margin: "2rem",
                background: "rgba(13,17,23,0.92)", border: "1px solid rgba(201,151,58,0.2)",
                borderRadius: 16, padding: "2.5rem",
                boxShadow: "0 32px 80px rgba(0,0,0,0.6), 0 0 60px rgba(201,151,58,0.05)",
                animation: "cardReveal 0.5s ease both",
            }}>
                {/* Corner ornaments */}
                {[{t:12,l:12},{t:12,r:12},{b:12,l:12},{b:12,r:12}].map((pos, i) => (
                    <div key={i} style={{ position: "absolute", ...pos, color: "rgba(201,151,58,0.3)", fontSize: 14 }}>✦</div>
                ))}

                {/* Header */}
                <div style={{ textAlign: "center", marginBottom: "2rem" }}>
                    <p style={{ fontSize: "0.65rem", letterSpacing: "0.4em", textTransform: "uppercase", color: "#c9973a", marginBottom: 8 }}>
                        ⟡ Yughi Store
                    </p>
                    <h1 style={{
                        fontSize: "1.8rem", fontWeight: 700, margin: 0,
                        background: "linear-gradient(135deg, #e8c06a, #c9973a)",
                        WebkitBackgroundClip: "text", WebkitTextFillColor: "transparent",
                    }}>
                        {t.title}
                    </h1>
                    <p style={{ color: "#7a6f5e", fontSize: "0.82rem", marginTop: 6, fontStyle: "italic" }}>{t.subtitle}</p>
                </div>

                <div style={{ height: 1, background: "linear-gradient(90deg, transparent, rgba(201,151,58,0.3), transparent)", marginBottom: "1.8rem" }} />

                <div style={{ display: "flex", flexDirection: "column", gap: "1.2rem" }}>
                    <InputField label={t.emailLabel} type="email" value={email}
                                onChange={e => setEmail(e.target.value)} placeholder={t.emailPlaceholder}
                                error={fieldErrors.email} autoComplete="email" />
                    <InputField label={t.passwordLabel} type="password" value={password}
                                onChange={e => setPassword(e.target.value)} placeholder={t.passwordPlaceholder}
                                error={fieldErrors.password} autoComplete="current-password" />
                </div>

                {/* Store error (bad credentials, locked, etc.) */}
                {error && (
                    <div style={{
                        marginTop: "1rem", padding: "0.7rem 1rem",
                        background: "rgba(239,68,68,0.08)", border: "1px solid rgba(239,68,68,0.25)",
                        borderRadius: 8, color: "#f87171", fontSize: "0.8rem", textAlign: "center",
                    }}>
                        {error}
                    </div>
                )}

                <button
                    onClick={handleSubmit} disabled={loading}
                    style={{
                        marginTop: "1.6rem", width: "100%", padding: "0.85rem",
                        background: loading ? "rgba(201,151,58,0.2)" : "linear-gradient(135deg, #c9973a, #a07828)",
                        border: "none", borderRadius: 10,
                        color: loading ? "#7a6f5e" : "#080a0f",
                        fontFamily: "Georgia, serif", fontWeight: 700, fontSize: "0.85rem",
                        letterSpacing: "0.15em", textTransform: "uppercase",
                        cursor: loading ? "not-allowed" : "pointer", transition: "all 0.2s",
                        display: "flex", alignItems: "center", justifyContent: "center", gap: 8,
                    }}
                    onMouseEnter={e => { if (!loading) e.currentTarget.style.filter = "brightness(1.1)"; }}
                    onMouseLeave={e => { e.currentTarget.style.filter = "none"; }}
                >
                    {loading ? (
                        <>
                            <span style={{
                                width: 14, height: 14,
                                border: "2px solid rgba(201,151,58,0.3)", borderTopColor: "#c9973a",
                                borderRadius: "50%", display: "inline-block", animation: "spin 0.8s linear infinite",
                            }} />
                            {t.loadingText}
                        </>
                    ) : `⚔ ${t.submitButton}`}
                </button>

                <div style={{ height: 1, background: "linear-gradient(90deg, transparent, rgba(201,151,58,0.15), transparent)", margin: "1.5rem 0" }} />

                <p style={{ textAlign: "center", color: "#7a6f5e", fontSize: "0.82rem", margin: 0 }}>
                    {t.noAccount}{" "}
                    <button onClick={() => navigate("/register")} style={{
                        background: "none", border: "none", color: "#c9973a", cursor: "pointer",
                        fontFamily: "Georgia, serif", fontSize: "0.82rem",
                        textDecoration: "underline", textUnderlineOffset: 3, padding: 0,
                    }}>
                        {t.registerLink}
                    </button>
                </p>
            </div>

            <style>{`
                @keyframes cardReveal { from { opacity:0; transform:translateY(24px) scale(0.97); } to { opacity:1; transform:translateY(0) scale(1); } }
                @keyframes floatGlyph { 0%,100% { transform:translateY(0) rotate(0deg); } 50% { transform:translateY(-20px) rotate(5deg); } }
                @keyframes spin { to { transform:rotate(360deg); } }
            `}</style>
        </div>
    );
}
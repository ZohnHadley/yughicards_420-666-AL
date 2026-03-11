import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {useAuthStore} from "../store/useAuthStore.js";
import {translations} from "../locales/index.js";
import {Eye, EyeOff} from "lucide-react";

function InputField({label, type = "text", value, onChange, placeholder, error, autoComplete, hint, rightIcon}) {
    const [focused, setFocused] = useState(false);
    return (
        <div style={{display: "flex", flexDirection: "column", gap: 5}}>
            <label style={{
                fontSize: "0.65rem", letterSpacing: "0.2em", textTransform: "uppercase",
                color: focused ? "#c9973a" : "#7a6f5e", fontFamily: "Georgia, serif", transition: "color 0.2s",
            }}>
                {label}
            </label>
            <div style={{position: "relative"}}>
                <input
                    type={type} value={value} onChange={onChange}
                    placeholder={placeholder} autoComplete={autoComplete}
                    onFocus={() => setFocused(true)} onBlur={() => setFocused(false)}
                    style={{
                        background: "rgba(13,17,23,0.8)",
                        border: `1px solid ${error ? "rgba(239,68,68,0.5)" : focused ? "rgba(201,151,58,0.6)" : "rgba(201,151,58,0.2)"}`,
                        borderRadius: 8, padding: rightIcon ? "0.72rem 2.5rem 0.72rem 1rem" : "0.72rem 1rem",
                        color: "#e8dcc8", fontSize: "0.88rem", fontFamily: "Georgia, serif", outline: "none",
                        transition: "border-color 0.2s, box-shadow 0.2s",
                        boxShadow: focused ? "0 0 0 3px rgba(201,151,58,0.08)" : "none",
                        width: "100%", boxSizing: "border-box",
                    }}
                />
                {rightIcon && (
                    <div style={{
                        position: "absolute", right: 12, top: "50%", transform: "translateY(-50%)",
                        display: "flex", alignItems: "center",
                    }}>
                        {rightIcon}
                    </div>
                )}
            </div>
            {hint && !error && <p style={{color: "#4a3f2a", fontSize: "0.68rem", margin: 0, fontStyle: "italic"}}>{hint}</p>}
            {error && <p style={{color: "#f87171", fontSize: "0.7rem", margin: 0}}>{error}</p>}
        </div>
    );
}

export default function Register({language = "fr"}) {
    const t = translations[language]?.register ?? translations["fr"].register;
    const navigate = useNavigate();
    const {register, loading, error, clearError} = useAuthStore();

    const [form, setForm] = useState({
        email: "", password: "", confirmPassword: "", userName: "",
    });
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirm, setShowConfirm] = useState(false);
    const [fieldErrors, setFieldErrors] = useState({});
    const [success, setSuccess] = useState(false);

    const setField = (key) => (e) => setForm(f => ({...f, [key]: e.target.value}));

    const validate = () => {
        const errs = {};
        if (!form.email.trim()) errs.email = t.errorRequired;
        else if (!/\S+@\S+\.\S+/.test(form.email)) errs.email = t.errorEmail;

        if (!form.password) errs.password = t.errorRequired;
        else if (form.password.length < 8) errs.password = t.errorPasswordLength;
        else if (!/[A-Z]/.test(form.password)) errs.password = t.errorPasswordUpper;
        else if (!/[0-9]/.test(form.password)) errs.password = t.errorPasswordNumber;
        else if (!/[^A-Za-z0-9]/.test(form.password)) errs.password = t.errorPasswordSpecial;

        if (form.confirmPassword !== form.password) errs.confirmPassword = t.errorPasswordMatch;
        if (!form.userName.trim() || form.userName.trim().length < 4) errs.userName = t.errorUsernameLength;
        return errs;
    };

    const handleSubmit = async () => {
        const errs = validate();
        if (Object.keys(errs).length > 0) {
            setFieldErrors(errs);
            return;
        }
        setFieldErrors({});
        clearError();
        try {
            await register({
                email: form.email,
                password: form.password,
                userName: form.userName,
            });
            setSuccess(true);
            setTimeout(() => navigate("/login"), 2200);
        } catch {
        }
    };

    const eyeStyle = {
        position: "absolute", right: 12, top: "50%", transform: "translateY(-50%)",
        background: "none", border: "none", color: "#7a6f5e", cursor: "pointer",
        fontSize: "0.9rem", padding: 0,
    };

    return (
        <div style={{
            minHeight: "100vh", background: "#080a0f",
            display: "flex", alignItems: "center", justifyContent: "center",
            position: "relative", overflow: "hidden", fontFamily: "Georgia, serif", padding: "2rem 1rem",
        }}>
            <div style={{
                position: "absolute", inset: 0,
                background: "radial-gradient(ellipse at 70% 20%, rgba(201,151,58,0.05) 0%, transparent 55%)",
                pointerEvents: "none",
            }}/>
            <div style={{
                position: "absolute",
                top: 0,
                left: 0,
                right: 0,
                height: 2,
                background: "linear-gradient(90deg, transparent, #c9973a, transparent)"
            }}/>

            <div style={{
                position: "relative", width: "100%", maxWidth: 440,
                background: "rgba(13,17,23,0.92)", border: "1px solid rgba(201,151,58,0.2)",
                borderRadius: 16, padding: "2.5rem",
                boxShadow: "0 32px 80px rgba(0,0,0,0.6), 0 0 60px rgba(201,151,58,0.04)",
                animation: "cardReveal 0.5s ease both",
            }}>

                <button onClick={() => navigate(-1)} style={{
                    position: "absolute", top: 16, left: 16,
                    background: "none", border: "none", color: "#c9973a",
                    fontFamily: "Georgia, serif", fontWeight: 600, fontSize: "0.85rem",
                    cursor: "pointer", textDecoration: "underline", textUnderlineOffset: 2, padding: 0,
                }}>
                    {t.backButton}
                </button>

                {success ? (
                    <div style={{textAlign: "center", padding: "3rem 0"}}>
                        <div style={{fontSize: "2.5rem", marginBottom: "1rem", color: "#c9973a"}}>✦</div>
                        <h2 style={{
                            background: "linear-gradient(135deg, #e8c06a, #c9973a)",
                            WebkitBackgroundClip: "text", WebkitTextFillColor: "transparent", margin: "0 0 8px",
                        }}>{t.successTitle}</h2>
                        <p style={{color: "#7a6f5e", fontStyle: "italic", fontSize: "0.85rem"}}>{t.successSubtitle}</p>
                    </div>
                ) : (
                    <>
                        <div style={{textAlign: "center", marginBottom: "1.8rem"}}>
                            <p style={{
                                fontSize: "0.65rem",
                                letterSpacing: "0.4em",
                                textTransform: "uppercase",
                                color: "#c9973a",
                                marginBottom: 8
                            }}>
                                ⟡ Yughi Store
                            </p>
                            <h1 style={{
                                fontSize: "1.7rem", fontWeight: 700, margin: 0,
                                background: "linear-gradient(135deg, #e8c06a, #c9973a)",
                                WebkitBackgroundClip: "text", WebkitTextFillColor: "transparent",
                            }}>{t.title}</h1>
                            <p style={{
                                color: "#7a6f5e",
                                fontSize: "0.8rem",
                                marginTop: 6,
                                fontStyle: "italic"
                            }}>{t.subtitle}</p>
                        </div>

                        <div style={{
                            height: 1,
                            background: "linear-gradient(90deg, transparent, rgba(201,151,58,0.3), transparent)",
                            marginBottom: "1.6rem"
                        }}/>

                        <div style={{display: "flex", flexDirection: "column", gap: "1.1rem"}}>

                            {/* Username */}
                            <InputField
                                label={t.usernameLabel} value={form.userName}
                                onChange={setField("userName")} placeholder={t.usernamePlaceholder}
                                error={fieldErrors.userName} autoComplete="username" hint={t.usernameHint}
                            />

                            {/* Email */}
                            <InputField
                                label={t.emailLabel} type="email" value={form.email}
                                onChange={setField("email")} placeholder={t.emailPlaceholder}
                                error={fieldErrors.email} autoComplete="email"
                            />

                            {/* Password */}
                            <InputField
                                label={t.passwordLabel} type={showPassword ? "text" : "password"}
                                value={form.password} onChange={setField("password")}
                                placeholder={t.passwordPlaceholder} error={fieldErrors.password}
                                autoComplete="new-password" hint={t.passwordHint}
                                rightIcon={
                                    <button style={{background:"none",border:"none",color:"#7a6f5e",cursor:"pointer",padding:0,display:"flex"}}
                                            onClick={() => setShowPassword(p => !p)} tabIndex={-1}>
                                        {showPassword ? <EyeOff size={16}/> : <Eye size={16}/>}
                                    </button>
                                }
                            />

                            {/* Confirm Password */}
                            <InputField
                                label={t.confirmPasswordLabel} type={showConfirm ? "text" : "password"}
                                value={form.confirmPassword} onChange={setField("confirmPassword")}
                                placeholder={t.confirmPasswordPlaceholder} error={fieldErrors.confirmPassword}
                                autoComplete="new-password"
                                rightIcon={
                                    <button style={{background:"none",border:"none",color:"#7a6f5e",cursor:"pointer",padding:0,display:"flex"}}
                                            onClick={() => setShowConfirm(p => !p)} tabIndex={-1}>
                                        {showConfirm ? <EyeOff size={16}/> : <Eye size={16}/>}
                                    </button>
                                }
                            />
                        </div>

                        {error && (
                            <div style={{
                                marginTop: "1rem", padding: "0.7rem 1rem",
                                background: "rgba(239,68,68,0.08)", border: "1px solid rgba(239,68,68,0.25)",
                                borderRadius: 8, color: "#f87171", fontSize: "0.78rem", textAlign: "center",
                            }}>{error}</div>
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
                            onMouseEnter={e => {
                                if (!loading) e.currentTarget.style.filter = "brightness(1.1)";
                            }}
                            onMouseLeave={e => {
                                e.currentTarget.style.filter = "none";
                            }}
                        >
                            {loading ? (
                                <>
                                    <span style={{
                                        width: 14,
                                        height: 14,
                                        border: "2px solid rgba(201,151,58,0.3)",
                                        borderTopColor: "#c9973a",
                                        borderRadius: "50%",
                                        display: "inline-block",
                                        animation: "spin 0.8s linear infinite",
                                    }}/>
                                    {t.loadingText}
                                </>
                            ) : `✦ ${t.submitButton}`}
                        </button>

                        <div style={{
                            height: 1,
                            background: "linear-gradient(90deg, transparent, rgba(201,151,58,0.15), transparent)",
                            margin: "1.4rem 0"
                        }}/>

                        <p style={{textAlign: "center", color: "#7a6f5e", fontSize: "0.82rem", margin: 0}}>
                            {t.haveAccount}{" "}
                            <button onClick={() => navigate("/login")} style={{
                                background: "none", border: "none", color: "#c9973a", cursor: "pointer",
                                fontFamily: "Georgia, serif", fontSize: "0.82rem",
                                textDecoration: "underline", textUnderlineOffset: 3, padding: 0,
                            }}>{t.loginLink}</button>
                        </p>
                    </>
                )}
            </div>

            <style>{`
                @keyframes cardReveal { from { opacity:0; transform:translateY(24px) scale(0.97); } to { opacity:1; transform:translateY(0) scale(1); } }
                @keyframes spin { to { transform:rotate(360deg); } }
            `}</style>
        </div>
    );
}
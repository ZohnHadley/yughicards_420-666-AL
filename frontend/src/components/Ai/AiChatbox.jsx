import { useEffect, useRef } from "react";
import {useChatStore} from "../../store/ChatStore.js";
import {translations} from "../../locales/index.js";


export default function AiChatBox({ language }) {
    const t = translations[language]?.aiChatBox;

    const {
        isOpen, isLarge,
        messages, input, isTyping,
        openChat, closeChat, toggleSize,
        setInput, sendMessage,
    } = useChatStore();

    const messagesRef = useRef(null);

    useEffect(() => {
        if (messagesRef.current)
            messagesRef.current.scrollTop = messagesRef.current.scrollHeight;
    }, [messages, isTyping]);

    const handleKey = (e) => {
        if (e.key === "Enter" && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    };

    return (
        <div className="fixed bottom-6 right-6 z-[9999] font-sans">

            {/* Bulle minimisée */}
            {!isOpen && (
                <button
                    onClick={openChat}
                    className="relative w-14 h-14 rounded-full border-none cursor-pointer flex items-center justify-center shadow-lg shadow-yellow-800/40"
                    style={{ background: "linear-gradient(135deg, #C9A84C, #7A5510)" }}
                    title="YughiCards AI"
                >
                    <svg viewBox="0 0 24 24" width="26" height="26" fill="#1A1A2E">
                        <path d="M12 3C7.03 3 3 6.58 3 11c0 2.05.84 3.92 2.22 5.34L4 21l4.89-1.54A9.37 9.37 0 0012 19c4.97 0 9-3.58 9-8S16.97 3 12 3z"/>
                    </svg>
                    <span className="absolute top-0.5 right-0.5 w-3 h-3 bg-green-400 rounded-full border-2 border-[#0F0F1A] animate-pulse" />
                </button>
            )}

            {/* Panel */}
            {isOpen && (
                <div
                    className={`
            flex flex-col overflow-hidden rounded-2xl
            border border-yellow-800/25
            shadow-2xl shadow-black/60
            bg-[#1A1A2E]
            transition-all duration-300 ease-in-out
            ${isLarge ? "w-[500px] h-[660px]" : "w-[340px] h-[480px]"}
          `}
                >
                    {/* Header */}
                    <div className="flex items-center gap-2.5 px-4 py-3.5 border-b border-yellow-800/25 bg-yellow-900/10 shrink-0">
                        <div
                            className="w-[34px] h-[34px] rounded-full flex items-center justify-center text-[15px] shrink-0"
                            style={{ background: "linear-gradient(135deg, #C9A84C, #7A5510)" }}
                        >
                            ⚡
                        </div>
                        <div className="flex-1 min-w-0">
                            <div className="text-[13px] font-semibold text-yellow-200">{t?.title}</div>
                            <div className="flex items-center gap-1 text-[11px] text-green-400">
                                <span className="w-1.5 h-1.5 rounded-full bg-green-400 inline-block" />
                                {t?.status}
                            </div>
                        </div>
                        <div className="flex gap-1">
                            <button
                                onClick={toggleSize}
                                title={isLarge ? t?.reduceTitle : t?.expandTitle}
                                className="w-[30px] h-[30px] rounded-lg border border-yellow-800/25 bg-transparent text-yellow-100/50 hover:bg-yellow-900/20 hover:text-yellow-400 cursor-pointer flex items-center justify-center text-sm transition-colors"
                            >
                                {isLarge ? "⊡" : "⤢"}
                            </button>
                            <button
                                onClick={closeChat}
                                title={t?.closeTitle}
                                className="w-[30px] h-[30px] rounded-lg border border-yellow-800/25 bg-transparent text-yellow-100/50 hover:bg-yellow-900/20 hover:text-yellow-400 cursor-pointer flex items-center justify-center text-sm transition-colors"
                            >
                                —
                            </button>
                        </div>
                    </div>

                    {/* Messages */}
                    <div
                        ref={messagesRef}
                        className="flex-1 overflow-y-auto px-4 py-4 flex flex-col gap-3 scrollbar-thin scrollbar-thumb-yellow-900/30"
                    >
                        {messages.map((msg) => (
                            <div
                                key={msg.id}
                                className={`flex items-end gap-2 ${msg.isUser ? "flex-row-reverse" : ""}`}
                            >
                                {/* Avatar */}
                                <div
                                    className={`
                    w-[26px] h-[26px] rounded-full flex items-center justify-center text-[11px] font-semibold shrink-0
                    ${msg.isUser
                                        ? "bg-white/10 text-yellow-100/50 text-[12px]"
                                        : "text-[#1A1A2E]"
                                    }
                  `}
                                    style={!msg.isUser ? { background: "linear-gradient(135deg, #C9A84C, #7A5510)" } : {}}
                                >
                                    {msg.isUser ? "👤" : "⚡"}
                                </div>

                                {/* Bubble */}
                                <div>
                                    <div
                                        className={`
                      max-w-[75%] px-3.5 py-2.5 text-[13px] leading-relaxed text-[#E8E0D0]
                      ${msg.isUser
                                            ? "bg-yellow-900/20 border border-yellow-700/35 rounded-2xl rounded-br-sm"
                                            : "bg-white/[0.04] border border-yellow-800/25 rounded-2xl rounded-bl-sm"
                                        }
                    `}
                                    >
                                        {msg.text}
                                    </div>
                                    <div className="text-[10px] text-yellow-100/30 mt-1">
                                        {msg.time}
                                    </div>
                                </div>
                            </div>
                        ))}

                        {/* Typing indicator */}
                        {isTyping && (
                            <div className="flex items-end gap-2">
                                <div
                                    className="w-[26px] h-[26px] rounded-full flex items-center justify-center text-[11px] shrink-0 text-[#1A1A2E]"
                                    style={{ background: "linear-gradient(135deg, #C9A84C, #7A5510)" }}
                                >
                                    ⚡
                                </div>
                                <div className="bg-white/[0.04] border border-yellow-800/25 rounded-2xl rounded-bl-sm px-3.5 py-3">
                                    <div className="flex gap-1 items-center">
                                        {[0, 200, 400].map((delay) => (
                                            <span
                                                key={delay}
                                                className="w-1.5 h-1.5 rounded-full bg-yellow-500 animate-bounce inline-block"
                                                style={{ animationDelay: `${delay}ms` }}
                                            />
                                        ))}
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>

                    {/* Input */}
                    <div className="flex gap-2 items-end px-3.5 py-3 border-t border-yellow-800/25 bg-black/20 shrink-0">
            <textarea
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyDown={handleKey}
                placeholder={t?.placeholder}
                rows={1}
                className="flex-1 bg-white/5 border border-yellow-800/20 rounded-xl px-3 py-2 text-[13px] text-[#E8E0D0] placeholder-yellow-100/30 resize-none max-h-[90px] outline-none focus:border-yellow-700/50 transition-colors font-sans"
            />
                        <button
                            onClick={sendMessage}
                            className="w-9 h-9 rounded-xl border-none cursor-pointer flex items-center justify-center shrink-0 hover:scale-105 active:scale-95 transition-transform"
                            style={{ background: "linear-gradient(135deg, #C9A84C, #8B6914)" }}
                        >
                            <svg viewBox="0 0 24 24" width="16" height="16" fill="#1A1A2E">
                                <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/>
                            </svg>
                        </button>
                    </div>

                </div>
            )}
        </div>
    );
}
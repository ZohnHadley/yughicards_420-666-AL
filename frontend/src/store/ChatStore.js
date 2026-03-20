import { create } from "zustand";
import {chatService} from "../service/ChatService.js";

const getOrCreateUserName = () => {
    let name = localStorage.getItem("chat_username");
    if (!name) {
        name = "user-" + Math.random().toString(36).slice(2, 8);
        localStorage.setItem("chat_username", name);
    }
    return name;
};

const INITIAL_MESSAGE = {
    id: 1,
    text: "Bonjour ! Je suis l'assistant YughiCards. Comment puis-je vous aider ? 🃏",
    isUser: false,
    time: new Date().toLocaleTimeString("fr-FR", { hour: "2-digit", minute: "2-digit" }),
};

export const useChatStore = create((set, get) => ({
    // ui state
    isOpen: false,
    isLarge: false,

    // chat state
    messages: [INITIAL_MESSAGE],
    input: "",
    isTyping: false,
    userName: getOrCreateUserName(),

    // ui actions
    openChat: () => set({ isOpen: true }),
    closeChat: () => set({ isOpen: false }),
    toggleSize: () => set((s) => ({ isLarge: !s.isLarge })),

    // chat actions
    setInput: (input) => set({ input }),

    sendMessage: async () => {
        const { input, userName, messages } = get();
        const question = input.trim();
        if (!question) return;

        const userMsg = {
            id: Date.now(),
            text: question,
            isUser: true,
            time: new Date().toLocaleTimeString("fr-FR", { hour: "2-digit", minute: "2-digit" }),
        };

        const botMsgId = Date.now() + 1;

        set({ messages: [...messages, userMsg], input: "", isTyping: true }); // 👈 pas de botMsg ici

        try {
            let firstChunk = true;
            await chatService.ask(userName, question, (accumulated) => {
                if (firstChunk) {
                    // Ajoute le message bot seulement au premier token reçu
                    firstChunk = false;
                    const botMsg = {
                        id: botMsgId,
                        text: accumulated,
                        isUser: false,
                        time: new Date().toLocaleTimeString("fr-FR", { hour: "2-digit", minute: "2-digit" }),
                    };
                    set((s) => ({
                        isTyping: false,
                        messages: [...s.messages, botMsg],
                    }));
                } else {
                    set((s) => ({
                        messages: s.messages.map((m) =>
                            m.id === botMsgId ? { ...m, text: accumulated } : m
                        ),
                    }));
                }
            });
        } catch (e) {
            console.error("Chat error:", e);
            const errMsg = {
                id: botMsgId,
                text: "Service temporairement indisponible. Veuillez réessayer.",
                isUser: false,
                time: new Date().toLocaleTimeString("fr-FR", { hour: "2-digit", minute: "2-digit" }),
            };
            set((s) => ({
                messages: [...s.messages, errMsg],
                isTyping: false,
            }));
        } finally {
            set({ isTyping: false });
        }
    },
}));
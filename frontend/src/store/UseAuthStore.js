import { create } from "zustand";
import { AuthService } from "../service/AuthService.js";

export const useAuthStore = create((set, get) => ({
    // ── State ─────────────────────────────────────────────────────────────
    user: null,          // ApplicationUserDTO
    token: AuthService.getToken(), // hydrate depuis sessionStorage au démarrage
    loading: false,
    error: null,

    isAuthenticated: () => !!get().token,

    // ── Login ─────────────────────────────────────────────────────────────
    login: async (email, password) => {
        set({ loading: true, error: null });
        try {
            const token = await AuthService.login(email, password);
            AuthService.saveToken(token);

            // Récupère le profil utilisateur immédiatement après login
            const user = await AuthService.getMe();

            set({ token, user, loading: false });
            return user;
        } catch (e) {
            set({ error: e.message, loading: false });
            throw e;
        }
    },

    // ── Register ──────────────────────────────────────────────────────────
    register: async ({ email, password, userName, firstName, lastName }) => {
        set({ loading: true, error: null });
        try {
            const user = await AuthService.register({ email, password, userName, firstName, lastName });
            set({ loading: false });
            return user;
        } catch (e) {
            set({ error: e.message, loading: false });
            throw e;
        }
    },

    // ── Logout ────────────────────────────────────────────────────────────
    logout: () => {
        AuthService.removeToken();
        set({ token: null, user: null, error: null });
    },

    // ── Fetch current user (ex: page refresh) ─────────────────────────────
    fetchMe: async () => {
        const token = AuthService.getToken();
        if (!token) return;
        set({ loading: true });
        try {
            const user = await AuthService.getMe();
            set({ user, token, loading: false });
        } catch {
            // Token expiré ou invalide → logout silencieux
            AuthService.removeToken();
            set({ token: null, user: null, loading: false });
        }
    },

    // ── Clear error ───────────────────────────────────────────────────────
    clearError: () => set({ error: null }),
}));
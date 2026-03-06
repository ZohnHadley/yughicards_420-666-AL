const API_BASE = "/api/v1/user";

export const AuthService = {

    // POST /api/v1/user/signin → { accessToken: "..." }
    login: async (email, password) => {
        const res = await fetch(`${API_BASE}/signin`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password }),
        });

        const text = await res.text();
        const data = text ? JSON.parse(text) : {};

        if (!res.ok) {
            throw new Error(data.accessToken ?? data.message ?? `Erreur ${res.status}`);
        }

        return data.accessToken;
    },

    // POST /api/v1/user/signup → ApplicationUserDTO
    register: async ({ email, password, userName, firstName, lastName }) => {
        const res = await fetch(`${API_BASE}/signup`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password, userName, firstName, lastName }),
        });

        const text = await res.text();
        const data = text ? JSON.parse(text) : {};

        if (!res.ok) {
            throw new Error(data.message ?? `Erreur ${res.status}`);
        }

        return data; // ApplicationUserDTO
    },

    // GET /api/v1/user/me → ApplicationUserDTO
    getMe: async () => {
        const token = AuthService.getToken();
        if (!token) throw new Error("Non authentifié.");

        const res = await fetch(`${API_BASE}/me`, {
            headers: { "Authorization": `Bearer ${token}` },
        });

        const text = await res.text();
        const data = text ? JSON.parse(text) : {};

        if (!res.ok) throw new Error(data.message ?? `Erreur ${res.status}`);
        return data;
    },

    // ── Token storage ─────────────────────────────────────────────────────
    saveToken:       (token) => sessionStorage.setItem("jwt_token", token),
    getToken:        ()      => sessionStorage.getItem("jwt_token"),
    removeToken:     ()      => sessionStorage.removeItem("jwt_token"),
    isAuthenticated: ()      => !!sessionStorage.getItem("jwt_token"),
};
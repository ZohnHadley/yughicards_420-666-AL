const API_BASE = "/api/store/cart";

export const ShoppingCartService = {
    getByUserId: async (userId) => {
        const res = await fetch(`${API_BASE}/user/${userId}`);
        if (!res.ok) throw new Error(`Failed to fetch cart for userId=${userId}`);
        return res.json();
    },

    getByEmail: async (email) => {
        const res = await fetch(`${API_BASE}/email/${encodeURIComponent(email)}`);
        if (!res.ok) throw new Error(`Failed to fetch cart for email=${email}`);
        return res.json();
    },

    addCard: async (userId, cardId) => {
        const res = await fetch(`${API_BASE}/add?userId=${userId}&cardId=${cardId}`, {
            method: "POST",
        });
        if (!res.ok) throw new Error(`Failed to add card ${cardId} for userId=${userId}`);
    },

    removeCard: async (userId, cardId) => {
        const res = await fetch(`${API_BASE}/remove?userId=${userId}&cardId=${cardId}`, {
            method: "DELETE",
        });
        if (!res.ok) throw new Error(`Failed to remove card ${cardId} for userId=${userId}`);
    },
};
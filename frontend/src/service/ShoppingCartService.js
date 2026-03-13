import { AuthService } from "./AuthService.js";

const EMPTY_CART = { id: null, applicationUser: null, cards: [] };

const authHeaders = () => {
    const token = AuthService.getToken();
    return {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
    };
};

export const ShoppingCartService = {
    getByUserId: async () => {
        const res = await fetch(`/api/v1/cart/get`, { headers: authHeaders() });
        if (res.status === 404) return EMPTY_CART;
        if (!res.ok) throw new Error(`Erreur serveur (${res.status})`);
        const cards = await res.json();
        return { id: null, applicationUser: null, cards: cards ?? [] };
    },

    addCard: async (cardId, quantity = 1) => {
        const res = await fetch(`/api/v1/cart/add/card=${cardId}/quantity=${quantity}`, {
            headers: authHeaders(),
        });
        if (!res.ok) throw new Error(`Impossible d'ajouter la carte ${cardId}`);
    },

    removeCard: async (cardId) => {
        const res = await fetch(`/api/v1/cart/remove/card=${cardId}`, {
            headers: authHeaders(),
        });
        if (!res.ok) throw new Error(`Impossible de retirer la carte ${cardId}`);
    },
};
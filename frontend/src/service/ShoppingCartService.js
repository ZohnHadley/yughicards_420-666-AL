const API_BASE = "/api/store/cart";

// Panier vide par défaut quand le backend retourne null ou 404
const EMPTY_CART = { id: null, applicationUser: null, cards: [] };

export const ShoppingCartService = {

    getByUserId: async (userId) => {
        const res = await fetch(`${API_BASE}/user/${userId}`);

        // 404 = panier pas encore créé → retourner un panier vide silencieusement
        if (res.status === 404) return EMPTY_CART;

        if (!res.ok) throw new Error(`Erreur serveur (${res.status}) pour userId=${userId}`);

        const data = await res.json();
        // Le backend peut retourner null si le panier n'existe pas encore
        return data ?? EMPTY_CART;
    },

    getByEmail: async (email) => {
        const res = await fetch(`${API_BASE}/email/${encodeURIComponent(email)}`);

        if (res.status === 404) return EMPTY_CART;

        if (!res.ok) throw new Error(`Erreur serveur (${res.status}) pour email=${email}`);

        const data = await res.json();
        return data ?? EMPTY_CART;
    },

    addCard: async (userId, cardId) => {
        const res = await fetch(`${API_BASE}/add?userId=${userId}&cardId=${cardId}`, {
            method: "POST",
        });
        if (!res.ok) throw new Error(`Impossible d'ajouter la carte ${cardId} (userId=${userId})`);
    },

    removeCard: async (userId, cardId) => {
        const res = await fetch(`${API_BASE}/remove?userId=${userId}&cardId=${cardId}`, {
            method: "DELETE",
        });
        if (!res.ok) throw new Error(`Impossible de retirer la carte ${cardId} (userId=${userId})`);
    },
};
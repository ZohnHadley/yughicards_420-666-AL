import { AuthService } from "./AuthService.js";

const authHeaders = () => {
    const token = AuthService.getToken();
    return {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
    };
};

export const AdminService = {
    incrementStock: async (cardId, quantity) => {
        const res = await fetch(
            `/api/v1/admin/cards/cardId=${cardId}/quantity=${quantity}/increment`,
            { method: "PUT", headers: authHeaders() }
        );
        if (!res.ok) throw new Error(`Erreur ${res.status}`);
        return res.json();
    },

    decrementStock: async (cardId, quantity) => {
        const res = await fetch(
            `/api/v1/admin/cards/cardId=${cardId}/quantity=${quantity}/decrement`,
            { method: "PUT", headers: authHeaders() }
        );
        if (!res.ok) throw new Error(`Erreur ${res.status}`);
        return res.json();
    },

    setStock: async (cardId, quantity) => {
        const res = await fetch(
            `/api/v1/admin/cards/cardId=${cardId}/quantity=${quantity}`,
            { method: "POST", headers: authHeaders() }
        );
        if (!res.ok) throw new Error(`Erreur ${res.status}`);
        return res.json();
    },

    deleteCard: async (cardId) => {
        const res = await fetch(
            `/api/v1/admin/cards/cardId=${cardId}`,
            { method: "DELETE", headers: authHeaders() }
        );
        if (!res.ok) throw new Error(`Erreur ${res.status}`);
        return res.json();
    },
};
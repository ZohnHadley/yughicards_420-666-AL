import { AuthService } from "./AuthService.js";

const BASE_URL = "/api/v1/orders";

function authHeaders() {
    const token = AuthService.getToken();
    return {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
    };
}

export const OrderService = {

    // GET /api/v1/orders — liste des commandes du user connecté
    getMyOrders: async () => {
        const res = await fetch(BASE_URL, { headers: authHeaders() });
        if (!res.ok) throw new Error(`Erreur ${res.status}`);
        return res.json();
    },

    // GET /api/v1/orders/{id} — détail d'une commande
    getOrderById: async (orderId) => {
        const res = await fetch(`${BASE_URL}/${orderId}`, { headers: authHeaders() });
        if (!res.ok) throw new Error(`Erreur ${res.status}`);
        return res.json();
    },
};
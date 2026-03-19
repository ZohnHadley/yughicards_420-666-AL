// store/OrderStore.js
import { create } from "zustand";
import { OrderService } from "../service/OrderService.js";

export const useOrderStore = create((set, get) => ({
    // ── State ──────────────────────────────────────────────────────────────
    orders: [],          // liste résumée des commandes
    selectedOrder: null, // détail d'une commande
    loading: false,
    error: null,

    // ── Fetch liste ────────────────────────────────────────────────────────
    fetchMyOrders: async () => {
        set({ loading: true, error: null });
        try {
            const orders = await OrderService.getMyOrders();
            set({ orders, loading: false });
        } catch (e) {
            set({ error: e.message, loading: false });
        }
    },

    // ── Fetch détail ───────────────────────────────────────────────────────
    fetchOrderById: async (orderId) => {
        set({ loading: true, error: null, selectedOrder: null });
        try {
            const order = await OrderService.getOrderById(orderId);
            set({ selectedOrder: order, loading: false });
        } catch (e) {
            set({ error: e.message, loading: false });
        }
    },

    // ── Reset détail (quand on quitte la page détail) ──────────────────────
    clearSelectedOrder: () => set({ selectedOrder: null, error: null }),

    // ── Reset complet ──────────────────────────────────────────────────────
    reset: () => set({ orders: [], selectedOrder: null, error: null }),
}));
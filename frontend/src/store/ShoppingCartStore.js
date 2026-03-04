import { create } from "zustand";
import { ShoppingCartService } from "../service/ShoppingCartService.js";

// Garantit que cart.cards est toujours un tableau
function normalizeCart(cart) {
    if (!cart) return { id: null, applicationUser: null, cards: [] };
    return { ...cart, cards: Array.isArray(cart.cards) ? cart.cards : [] };
}

export const useShoppingCartStore = create((set, get) => ({
    // ── State ─────────────────────────────────────────────────────────────────
    cart: null,
    loading: false,
    error: null,

    // ── Fetch ─────────────────────────────────────────────────────────────────
    fetchByUserId: async (userId) => {
        if (!userId) return;
        set({ loading: true, error: null });
        try {
            const cart = await ShoppingCartService.getByUserId(userId);
            set({ cart: normalizeCart(cart), loading: false });
        } catch (e) {
            set({ error: e.message, loading: false });
        }
    },

    fetchByEmail: async (email) => {
        if (!email) return;
        set({ loading: true, error: null });
        try {
            const cart = await ShoppingCartService.getByEmail(email);
            set({ cart: normalizeCart(cart), loading: false });
        } catch (e) {
            set({ error: e.message, loading: false });
        }
    },

    // ── Add card (optimistic) ─────────────────────────────────────────────────
    addCard: async (cardDTO) => {
        const { cart } = get();
        if (!cart?.applicationUser?.id) throw new Error("Aucun utilisateur chargé dans le panier.");
        if (!cardDTO?.id) throw new Error("La carte doit avoir un id.");

        const userId = cart.applicationUser.id;
        const cardId = cardDTO.id;

        const alreadyIn = cart.cards.some((c) => c.id === cardId);
        if (alreadyIn) return;

        // Optimistic update
        set((state) => ({
            cart: { ...state.cart, cards: [...state.cart.cards, cardDTO] },
        }));

        try {
            await ShoppingCartService.addCard(userId, cardId);
        } catch (e) {
            // Rollback
            set((state) => ({
                cart: { ...state.cart, cards: state.cart.cards.filter((c) => c.id !== cardId) },
                error: e.message,
            }));
            throw e;
        }
    },

    // ── Remove card (optimistic) ──────────────────────────────────────────────
    removeCard: async (cardId) => {
        const { cart } = get();
        if (!cart?.applicationUser?.id) throw new Error("Aucun utilisateur chargé dans le panier.");

        const userId = cart.applicationUser.id;
        const previousCards = [...cart.cards];

        // Optimistic update
        set((state) => ({
            cart: { ...state.cart, cards: state.cart.cards.filter((c) => c.id !== cardId) },
        }));

        try {
            await ShoppingCartService.removeCard(userId, cardId);
        } catch (e) {
            // Rollback
            set((state) => ({
                cart: { ...state.cart, cards: previousCards },
                error: e.message,
            }));
            throw e;
        }
    },

    // ── Computed ──────────────────────────────────────────────────────────────
    getTotal: () => {
        const { cart } = get();
        if (!cart?.cards) return 0;
        return cart.cards.reduce((sum, card) => {
            const price = parseFloat(card.card_prices?.[0]?.cardmarket_price ?? 0);
            return sum + (price > 0 ? price * 1.36 : 0);
        }, 0);
    },

    getCardCount: () => {
        const { cart } = get();
        return cart?.cards?.length ?? 0;
    },

    // ── Reset ─────────────────────────────────────────────────────────────────
    clearCart: () => set({ cart: null, error: null }),
}));
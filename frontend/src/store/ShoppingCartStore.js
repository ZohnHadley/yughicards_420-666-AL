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
    fetchByUserId: async () => {
        set({ loading: true, error: null });
        try {
            const cart = await ShoppingCartService.getByUserId();
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
        if (!cardDTO?.id) throw new Error("La carte doit avoir un id.");

        const cardId = cardDTO.id;

        // Optimistic update — ajoute UNE occurrence de plus
        set((state) => ({
            cart: { ...state.cart, cards: [...(state.cart?.cards ?? []), cardDTO] },
        }));

        try {
            await ShoppingCartService.addCard(cardId, 1);
        } catch (e) {
            // Rollback — retire la dernière occurrence ajoutée
            set((state) => {
                const cards = [...(state.cart?.cards ?? [])];
                const idx = cards.findLastIndex(c => c.id === cardId);
                if (idx !== -1) cards.splice(idx, 1);
                return { cart: { ...state.cart, cards }, error: e.message };
            });
            throw e;
        }
    },
    // ── Remove card (optimistic) ──────────────────────────────────────────────
    removeCard: async (cardId) => {
        const { cart } = get();
        const previousCards = [...(cart?.cards ?? [])];

        // Optimistic update — retire UNE occurrence
        set((state) => {
            const cards = [...(state.cart?.cards ?? [])];
            const idx = cards.findIndex(c => c.id === cardId);
            if (idx !== -1) cards.splice(idx, 1);
            return { cart: { ...state.cart, cards } };
        });

        try {
            await ShoppingCartService.removeCard(cardId);
        } catch (e) {
            set((state) => ({
                cart: { ...state.cart, cards: previousCards },
                error: e.message,
            }));
            throw e;
        }
    },

    removeAllOfCard: async (cardId) => {
        const { cart } = get();
        const previousCards = [...(cart?.cards ?? [])];

        // Optimistic update — retire TOUTES les occurrences
        set((state) => ({
            cart: { ...state.cart, cards: (state.cart?.cards ?? []).filter(c => c.id !== cardId) },
        }));

        try {
            // Appelle remove autant de fois qu'il y avait de copies
            const count = previousCards.filter(c => c.id === cardId).length;
            for (let i = 0; i < count; i++) {
                await ShoppingCartService.removeCard(cardId);
            }
        } catch (e) {
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
export const ShoppingCartEn = {
    eyebrow: "Yughi Store",
    title: "My Cart",
    backToInventory: "← Back",
    cartesBadge: (n) => `${n} card${n !== 1 ? "s" : ""}`,
    cardCount: (n) => `${n} card${n !== 1 ? "s" : ""} selected`,

    loading: "Loading deck…",

    errorTitle: "⚠ Server Error",
    errorNetwork: "Could not reach the server. Make sure the backend is running.",
    retry: "Try again",

    emptyTitle: "No cards in your deck yet.",
    emptySubtitle: "Add cards from the inventory to get started.",
    emptyAction: "Browse the inventory →",

    removeTitle: "Remove",
    cadLabel: "CAD",

    summaryTitle: "⟡ Summary",
    summaryCards: (n) => `Cards (${n})`,
    summaryShipping: "Shipping",
    summaryShippingFree: "—",
    summaryTotal: "Total",
    checkoutButton: "⚔ Place Order",
    emptyCheckout: "Cart empty",
    secureCheckout: "Secure payment · Tracked delivery",
};
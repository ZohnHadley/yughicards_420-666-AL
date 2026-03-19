export const OrderHistoryEn = {
    // List
    eyebrow: "My Account",
    title: "My Orders",
    subtitle: "Your purchase history",
    emptyTitle: "No orders yet",
    emptySubtitle: "You haven't placed any orders yet.",
    emptyAction: "Browse inventory",
    errorTitle: "Loading error",
    errorNetwork: "Unable to retrieve your orders.",
    retry: "Try again",
    loading: "Loading...",
    orderCount: (n) => `${n} order${n > 1 ? "s" : ""}`,

    // Order card (list)
    orderNumber: (id) => `Order #${id}`,
    cardCount: (n) => `${n} card${n > 1 ? "s" : ""}`,
    seeDetails: "View details →",
    shipping: "Shipping",
    pickup: "In-store pickup",
    ship: "Home delivery",
    free: "Free",

    // Detail
    detailEyebrow: "Order detail",
    backToOrders: "← My orders",
    detailCardCount: (n) => `${n} item${n > 1 ? "s" : ""}`,
    subtotalLabel: "Subtotal",
    shippingCostLabel: "Shipping",
    totalLabel: "Total",
    cadLabel: "CAD",
    unitPrice: "Unit price",
    quantity: "Qty",
};
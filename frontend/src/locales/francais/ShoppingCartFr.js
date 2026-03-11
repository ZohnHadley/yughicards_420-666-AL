export const ShoppingCartFr = {
    eyebrow: "Yughi Store",
    title: "Mon Panier",
    backToInventory: "← Back",
    cartesBadge: (n) => `${n} carte${n !== 1 ? "s" : ""}`,
    cardCount: (n) => `${n} carte${n !== 1 ? "s" : ""} sélectionnée${n !== 1 ? "s" : ""}`,

    loading: "Chargement du deck…",

    errorTitle: "⚠ Erreur serveur",
    errorNetwork: "Impossible de contacter le serveur. Vérifie que le backend tourne.",
    retry: "Réessayer",

    emptyTitle: "Aucune carte dans ton deck.",
    emptySubtitle: "Ajoute des cartes depuis l'inventaire pour commencer.",
    emptyAction: "Parcourir l'inventaire →",

    removeTitle: "Retirer",
    cadLabel: "CAD",

    summaryTitle: "⟡ Résumé",
    summaryCards: (n) => `Cartes (${n})`,
    summaryShipping: "Livraison",
    summaryShippingFree: "—",
    summaryTotal: "Total",
    checkoutButton: "⚔ Passer la commande",
    emptyCheckout: "Panier vide",
    secureCheckout: "Paiement sécurisé · Livraison suivie",
};
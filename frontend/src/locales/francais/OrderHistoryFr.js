export const OrderHistoryFr = {
    // Liste
    eyebrow: "Mon Compte",
    title: "Mes Commandes",
    subtitle: "Historique de vos achats",
    emptyTitle: "Aucune commande",
    emptySubtitle: "Vous n'avez pas encore passé de commande.",
    emptyAction: "Parcourir l'inventaire",
    errorTitle: "Erreur de chargement",
    errorNetwork: "Impossible de récupérer vos commandes.",
    retry: "Réessayer",
    loading: "Chargement...",
    orderCount: (n) => `${n} commande${n > 1 ? "s" : ""}`,

    // Carte de commande (liste)
    orderNumber: (id) => `Commande #${id}`,
    cardCount: (n) => `${n} carte${n > 1 ? "s" : ""}`,
    seeDetails: "Voir le détail →",
    shipping: "Livraison",
    pickup: "Ramassage en magasin",
    ship: "Livraison à domicile",
    free: "Gratuit",

    // Détail
    detailEyebrow: "Détail de commande",
    backToOrders: "← Mes commandes",
    detailCardCount: (n) => `${n} article${n > 1 ? "s" : ""}`,
    subtotalLabel: "Sous-total",
    shippingCostLabel: "Livraison",
    totalLabel: "Total",
    cadLabel: "CAD",
    unitPrice: "Prix unitaire",
    quantity: "Qté",
};
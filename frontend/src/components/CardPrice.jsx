import React from "react";

// Exemple : taux fixe USD -> CAD (à mettre à jour selon le taux réel)
const USD_TO_CAD = 1.35;

export default function CardPrice({ price, currency = "CAD" }) {
    if (!price || price === "") return <span>—</span>;

    // On convertit le prix en nombre
    const priceNumber = parseFloat(price.replace(/[^0-9.]/g, ""));
    if (isNaN(priceNumber)) return <span>—</span>;

    let convertedPrice = priceNumber;
    let symbol = "$";

    if (currency === "CAD") {
        convertedPrice = (priceNumber * USD_TO_CAD).toFixed(2);
    }

    return <span>{symbol} {convertedPrice}</span>;
}
import React, { useEffect } from "react";
import { useYughioInventoryStore } from "../store/YughiohInventoryStore.js";

export default function YughiohInventory() {
    const { cards, loading, error, fetchAllCards } = useYughioInventoryStore();

    useEffect(() => {
        fetchAllCards(0, 20); // récupère 20 cartes pour la grille 4x5
    }, []);

    if (loading) return <div>Chargement...</div>;
    if (error) return <div>Erreur : {error}</div>;

    return (
        <div>
            <h1>Page de notre inventaire des cartes</h1>

            <div style={{
                display: "grid",
                gridTemplateColumns: "repeat(4, 1fr)",
                gap: "16px",
                marginTop: "20px"
            }}>
                {cards.map(card => (
                    <div key={card.id} style={{
                        border: "1px solid #ccc",
                        borderRadius: "8px",
                        padding: "8px",
                        textAlign: "center"
                    }}>
                        <img src={card.imageUrl} alt={card.name} style={{ width: "100%", height: "auto" }} />
                        <h3>{card.name}</h3>
                    </div>
                ))}
            </div>
        </div>
    );
}
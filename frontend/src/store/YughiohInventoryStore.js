import { useState } from "react";
import { YughioCardService } from "../service/YughioInventoryService.js";

export function useYughioInventoryStore() {
    const [cards, setCards] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchAllCards = async (page = 0, size = 10) => {
        setLoading(true);
        setError(null);
        try {
            const data = await YughioCardService.getAllCards(page, size);
            setCards(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const searchCards = async (name, page = 0, size = 10) => {
        setLoading(true);
        setError(null);
        try {
            const data = await YughioCardService.searchCardsByName(name, page, size);
            setCards(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return { cards, loading, error, fetchAllCards, searchCards };
}
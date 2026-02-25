// service qui parle au backend
export const YughioCardService = {
    getAllCards: async (page = 0, size = 10) => {
        const res = await fetch(`http://localhost:8080/api/v1/get-all-cards/page=${page}/num=${size}`);
        if (!res.ok) throw new Error("Erreur lors de la récupération des cartes");
        return res.json();
    },

    getCardById: async (id) => {
        const res = await fetch(`http://localhost:8080/api/v1/get-card/id=${id}`);
        if (!res.ok) throw new Error("Erreur lors de la récupération de la carte");
        return res.json();
    },

    searchCardsByName: async (name, page = 0, size = 10) => {
        const res = await fetch(`http://localhost:8080/api/v1/get-all-cards/search=${name}/page=${page}/num=${size}`);
        if (!res.ok) throw new Error("Erreur lors de la recherche de cartes");
        return res.json();
    }
};
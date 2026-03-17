const BASE_URL = "http://localhost:8080/api/v1/ai";

export const chatService = {
    ask: async (userName, question) => {
        const res = await fetch(
            `${BASE_URL}/${userName}/ask?question=${encodeURIComponent(question)}`
        );
        if (!res.ok) throw new Error("Erreur serveur");
        return await res.text();
    },
};
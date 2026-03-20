export const chatService = {
    ask: async (userName, question) => {
        console.log("📤 Envoi vers:", `http://localhost:8080/api/v1/ai/${userName}/ask`);
        console.log("📝 Question:", question);

        const res = await fetch(
            `http://localhost:8080/api/v1/ai/${userName}/ask?question=${encodeURIComponent(question)}`
        );

        console.log("📥 Status reçu:", res.status);

        if (!res.ok) throw new Error(`Erreur serveur: ${res.status}`);
        return (await res.text()).trim();
    },
};
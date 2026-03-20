export const chatService = {
    ask: async (userName, question, onChunk) => {
        const res = await fetch(
            `http://localhost:8080/api/v1/ai/${userName}/ask?question=${encodeURIComponent(question)}`,
            { headers: { Accept: "text/event-stream" } }
        );

        if (!res.ok) throw new Error(`Erreur serveur: ${res.status}`);

        const reader = res.body.getReader();
        const decoder = new TextDecoder();
        let fullText = "";
        let buffer = "";

        while (true) {
            const { done, value } = await reader.read();
            if (done) break;

            buffer += decoder.decode(value, { stream: true });
            const lines = buffer.split("\n");

            // Garde la dernière ligne incomplète dans le buffer
            buffer = lines.pop();

            for (const line of lines) {
                if (line.startsWith("data:")) {
                    const token = line.slice(5); // retire "data:"
                    fullText += token;
                    onChunk(fullText);
                }
            }
        }

        // Traite ce qui reste dans le buffer
        if (buffer.startsWith("data:")) {
            fullText += buffer.slice(5);
            onChunk(fullText);
        }

        return fullText;
    },
};
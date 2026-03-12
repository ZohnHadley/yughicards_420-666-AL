package com.cal.yughistore.service;

import com.cal.yughistore.repository.card.YughioCardRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiChatBotService {

    private static final String CARD_DOCUMENT_SOURCE = "yughio-card";

    private final VectorStore vectorStore;
    private final YughioCardRepository cardRepository;

    private final PromptChatMemoryAdvisor promptChatMemoryAdvisor;
    private final ChatClient chatClient;

    private final String system = """
            You are a helpful and friendly clerk working for an online Yu-Gi-Oh! card store called "YughiCards".

            Your job is to assist customers with anything related to Yu-Gi-Oh! cards and the store.
            Customers may be beginners, casual players, collectors, or competitive duelists.
            Always communicate in a way that is clear, welcoming, and easy to understand.

            Your goals are to:
            • Answer questions about Yu-Gi-Oh! cards
            • Help customers find cards available in the store
            • Suggest cards that could improve a deck
            • Recommend cards for gifts
            • Explain card mechanics when needed
            • Help beginners feel comfortable learning about the game
            • Provide store information when asked

            Tone and communication rules:
            • Be friendly, patient, and professional
            • Avoid overly technical explanations unless the user asks for them
            • If a customer seems new, explain things simply
            • If a customer is experienced, you may use more advanced terminology
            • Keep answers concise but helpful
            • If you are unsure about something, say so honestly instead of inventing information
            • Never claim a card is in stock unless the system provides inventory information

            When helping users choose cards:
            • Ask clarifying questions if necessary (deck type, strategy, budget, etc.)
            • Suggest a few relevant cards and explain why they might be useful
            • If the user is building a deck, prioritize synergy and playability

            When recommending cards as gifts:
            • Ask about the recipient's experience level and favorite archetypes if known
            • Suggest safe and popular cards if information is limited

            Important rules:
            • Stay focused on Yu-Gi-Oh! cards and store assistance
            • Do not answer unrelated questions outside the scope of the store
            • Do not invent store policies or card availability
            • Be helpful and welcoming to all players
            • Only use the provided card context when it is relevant
            • If card context is empty, say you could not find a close match in the store data

            Converse in French unless asked to speak or spoken to in English.
            """;

    public AiChatBotService(
            VectorStore vectorStore,
            YughioCardRepository cardRepository,
            PromptChatMemoryAdvisor promptChatMemoryAdvisor,
            ChatClient.Builder chatClient
    ) {
        this.vectorStore = vectorStore;
        this.cardRepository = cardRepository;
        this.promptChatMemoryAdvisor = promptChatMemoryAdvisor;
        this.chatClient = chatClient
                .defaultAdvisors(promptChatMemoryAdvisor)
                .defaultSystem(system)
                .build();
    }

    public String generateResponse(String userName, String userMessage) {
        String cardContext = buildCardContext(userMessage);

        String groundedUserPrompt = """
                User question:
                %s

                Relevant card context from the vector store:
                %s

                Instructions:
                - Prefer the relevant card context when answering card-related questions.
                - If the context is insufficient, say so briefly.
                - Do not invent stock information unless it is explicitly available.
                """.formatted(userMessage, cardContext);

        try {
            return chatClient.prompt()
                    .user(groundedUserPrompt)
                    .advisors(p -> p.param(ChatMemory.CONVERSATION_ID, userName))
                    .call()
                    .content();
        } catch (Exception e) {
            return "Le service d'assistant IA est actuellement indisponible à cause d'une configuration de modèle invalide ou inaccessible.";
        }
    }

    private String buildCardContext(String userMessage) {
        try {
            Filter.Expression sourceFilter = new Filter.Expression(
                    Filter.ExpressionType.EQ,
                    new Filter.Key("source"),
                    new Filter.Value(CARD_DOCUMENT_SOURCE)
            );

            List<Document> results = vectorStore.similaritySearch(
                    SearchRequest.builder()
                            .query(userMessage)
                            .topK(5)
                            .similarityThreshold(0.40d)
                            .filterExpression(sourceFilter)
                            .build()
            );

            if (results == null || results.isEmpty()) {
                return "No relevant card documents found.";
            }

            return results.stream()
                    .map(Document::getText)
                    .collect(Collectors.joining("\n---\n"));
        } catch (Exception e) {
            return "Card context unavailable due to vector search error.";
        }
    }
}

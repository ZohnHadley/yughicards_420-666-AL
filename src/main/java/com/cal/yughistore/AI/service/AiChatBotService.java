package com.cal.yughistore.AI.service;

import com.cal.yughistore.repository.card.YughioCardRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiChatBotService {

    private static final Logger logger = LoggerFactory.getLogger(AiChatBotService.class);
    private static final String CARD_DOCUMENT_SOURCE = "yughio-card";

    private final QuestionAnswerAdvisor questionAnswerAdvisor;
    private final PromptChatMemoryAdvisor promptChatMemoryAdvisor;
    private final VectorStore vectorStore;
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
            VectorStore vectorStore, YughioCardRepository cardRepository, QuestionAnswerAdvisor questionAnswerAdvisor,
            PromptChatMemoryAdvisor promptChatMemoryAdvisor,
            ChatClient.Builder chatClient
    ) {
        this.vectorStore = vectorStore;
        this.questionAnswerAdvisor = questionAnswerAdvisor;
        this.promptChatMemoryAdvisor = promptChatMemoryAdvisor;
        this.chatClient = chatClient
                .defaultAdvisors(questionAnswerAdvisor, promptChatMemoryAdvisor)
                .defaultSystem(system)
                .build();
    }

    public String generateResponse(String userName, String userMessage) {
        try {
            return chatClient.prompt()
                    .user(userMessage)
                    .advisors(p -> p.param(ChatMemory.CONVERSATION_ID, userName))
                    .call()
                    .content();
        } catch (Exception e) {
            return "Le service d'assistant IA est actuellement indisponible à cause d'une configuration de modèle invalide ou inaccessible.";
        }
    }
}

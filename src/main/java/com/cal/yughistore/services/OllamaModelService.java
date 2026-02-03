package com.cal.yughistore.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OllamaModelService {
    private static final Logger logger = LoggerFactory.getLogger(OllamaModelService.class);

    private final ChatClient chatClient;

    public OllamaModelService(ChatClient ollamaChatClient) {
        this.chatClient = ollamaChatClient;
    }

    public String prompt(String prompt) {
        try {
            String ollama_response = chatClient
                    .prompt(prompt).advisors(advisorSpec -> {

                    })
                    .call()
                    .content();

            logger.info("Ollama (llama3.1:8b) response: {}", ollama_response);
            return ollama_response;
        } catch (Exception e) {
            logger.error("Error in multi-client flow", e);
        }
        return "Ollama (llama3.1:8b) failed to respond";
    }


}

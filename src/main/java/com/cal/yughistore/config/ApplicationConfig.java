package com.cal.yughistore.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaModel) {
        return ChatClient.create(ollamaModel);
    }

}

package com.cal.yughistore.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestClient;

import javax.sql.DataSource;

@Configuration
public class AssistantConfig {

    @Bean
    PromptChatMemoryAdvisor promptChatMemoryAdvisor(DataSource dataSource) {
        JdbcChatMemoryRepository jdbc = JdbcChatMemoryRepository
                .builder()
                .dataSource(dataSource)
                .build();

        MessageWindowChatMemory mwa = MessageWindowChatMemory
                .builder()
                .chatMemoryRepository(jdbc)
                .build();

        return PromptChatMemoryAdvisor
                .builder(mwa).build();
    }

    @Bean
    ChatClient chatClient(OllamaChatModel chatModel) {
        return ChatClient.create(chatModel);
    }

}

package com.cal.yughistore.config;

import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import javax.sql.DataSource;

@Configuration
public class AssistantConfig {


    @Bean
    public OllamaApi ollamaApi(RestClient.Builder restClientBuilder) {
        // This adds the header Pinggy requires to skip the warning page
        return OllamaApi.builder().baseUrl("https://ksuik-34-125-140-86.a.free.pinggy.link").build();
    }

    @Bean
    PromptChatMemoryAdvisor promptChatMemoryAdvisor(DataSource dataSource) {
        var jdbc = JdbcChatMemoryRepository
                .builder()
                .dataSource(dataSource)
                .build();

        var mwa = MessageWindowChatMemory
                .builder()
                .chatMemoryRepository(jdbc)
                .build();

        return PromptChatMemoryAdvisor
                .builder(mwa).build();
    }
}

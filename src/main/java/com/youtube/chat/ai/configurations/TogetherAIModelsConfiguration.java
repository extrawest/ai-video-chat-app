package com.youtube.chat.ai.configurations;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static java.time.Duration.ofSeconds;

@Configuration
public class TogetherAIModelsConfiguration {
    @Value("${ai.api-key}")
    private String apiKey;
    @Value("${ai.base-url}")
    private String baseUrl;
    @Value("${ai.model-name}")
    private String modelName;
    @Value("${ai.embedding-model-name}")
    private String embeddingModelName;

    @Bean
    public OpenAiChatModel openAiChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .timeout(Duration.ofMinutes(2))
                .logRequests(true)
                .logResponses(true)
                .maxRetries(1)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

    }

    @Bean
    public OpenAiEmbeddingModel openAiEmbeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(embeddingModelName)
                .maxRetries(1)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}

package com.youtube.chat.ai.configurations;

import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class QdrantConfiguration {
    @Value("${qgrant.api-key}")
    private String qgrantApiKey;
    @Value("${qgrant.grpc-host}")
    private String qgrantGrpcHost;

    @Bean
    public QdrantClient qdrantClient() {
        return new QdrantClient(
                QdrantGrpcClient.newBuilder(qgrantGrpcHost, 6334, true)
                        .withApiKey(qgrantApiKey)
                        .build()
        );
    }

    @Bean
    @Scope("prototype")
    public QdrantEmbeddingStore.Builder qdrantEmbeddingStoreBuilder() {
        return QdrantEmbeddingStore.builder()
                .host(qgrantGrpcHost)
                .port(6334)
                .apiKey(qgrantApiKey)
                .useTls(true);
    }
}

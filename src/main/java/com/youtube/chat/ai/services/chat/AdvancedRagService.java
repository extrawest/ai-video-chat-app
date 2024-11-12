package com.youtube.chat.ai.services.chat;

import com.youtube.chat.ai.models.AiSourceType;
import com.youtube.chat.ai.utils.PromptUtil;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.tavily.TavilyWebSearchEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Slf4j
@Service
public class AdvancedRagService {
    private static final String YOUTUBE_VIDEO = "youtube_video_";

    @Value("${tavily.api-key}")
    private String tavilyApiKey;

    private final OpenAiChatModel openAiChatModel;
    private final OpenAiEmbeddingModel openAiEmbeddingModel;
    private final QdrantEmbeddingStore.Builder qdrantEmbeddingStoreBuilder;

    public String generateAnswer(ClientChatRequest chatRequest, AiSourceType sourceType) {
        try {
            QuestionAnsweringAgent agent = advancedQuestionAnsweringAgent(chatRequest);
            var userMessage = "";
            if (AiSourceType.WEB_SEARCH_SOURCE.equals(sourceType)) {
                userMessage = chatRequest.getVideoLink() + " " + chatRequest.getUserMsg();
            } else {
                userMessage = chatRequest.getUserMsg() + " Source type is " + sourceType.name();
            }

            String answer = agent.answer(userMessage);
            log.info("answer: {}", answer);

            return answer;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "RAG: Exception occurred while generating answer. " + e.getMessage();
        }
    }

    private QuestionAnsweringAgent advancedQuestionAnsweringAgent(ClientChatRequest chatRequest) throws IOException {
        String videoId = chatRequest.getVideoLink().split("v=")[1];
        ContentRetriever embeddingContentRetriever = getEmbeddingStoreContentRetriever(YOUTUBE_VIDEO + videoId);
        ContentRetriever webSearchContentRetriever = getWebSearchContentRetriever();

        ContentInjector contentInjector = DefaultContentInjector.builder()
                .promptTemplate(PromptUtil.loadPromptTemplate(this.getClass(), "system_prompt.txt"))
                .build();

        Map<ContentRetriever, String> retrieverToDescription = new HashMap<>();
        retrieverToDescription.put(embeddingContentRetriever, AiSourceType.EMBEDDING_SOURCE.name());
        retrieverToDescription.put(webSearchContentRetriever, AiSourceType.WEB_SEARCH_SOURCE.name());

        QueryRouter queryRouter = LanguageModelQueryRouter.builder()
                .chatLanguageModel(openAiChatModel)
                .retrieverToDescription(retrieverToDescription)
                .build();

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryRouter(queryRouter)
                .contentInjector(contentInjector)
                .build();

        return AiServices.builder(QuestionAnsweringAgent.class)
                .chatLanguageModel(openAiChatModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    private ContentRetriever getWebSearchContentRetriever() {
        WebSearchEngine webSearchEngine = TavilyWebSearchEngine.builder()
                .apiKey(tavilyApiKey)
                .includeAnswer(true)
                .build();

        return WebSearchContentRetriever.builder()
                .webSearchEngine(webSearchEngine)
                .maxResults(1)
                .build();
    }

    private ContentRetriever getEmbeddingStoreContentRetriever(String collectionName) {
        EmbeddingStore<TextSegment> embeddingStore = qdrantEmbeddingStoreBuilder
                .collectionName(collectionName)
                .build();

        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(openAiEmbeddingModel)
                .maxResults(1)
                .minScore(0.75)
                .build();
    }
}

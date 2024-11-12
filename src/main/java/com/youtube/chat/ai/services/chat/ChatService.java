package com.youtube.chat.ai.services.chat;

import com.youtube.chat.ai.models.AiAnswer;
import com.youtube.chat.ai.models.AiSourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatService {
    private final AdvancedRagService advancedRagService;

    public AiAnswer chat(ClientChatRequest chatRequest) {
        String textEmbedded = advancedRagService.generateAnswer(chatRequest, AiSourceType.EMBEDDING_SOURCE);
        if (textEmbedded.contains("NOT_FOUND") || textEmbedded.contains("NOT FOUND") || textEmbedded.contains("not found")) {
            String textWeb = advancedRagService.generateAnswer(chatRequest, AiSourceType.WEB_SEARCH_SOURCE);
            return new AiAnswer(textWeb, AiSourceType.WEB_SEARCH_SOURCE);
        } else {
            return new AiAnswer(textEmbedded, AiSourceType.EMBEDDING_SOURCE);
        }
    }
}

package com.youtube.chat.ai.services;

import com.youtube.chat.ai.exceptions.InvalidDataException;
import com.youtube.chat.ai.models.AiAnswer;
import com.youtube.chat.ai.models.YoutubeChatResponse;
import com.youtube.chat.ai.services.chat.ChatService;
import com.youtube.chat.ai.services.chat.ClientChatRequest;
import com.youtube.chat.ai.services.chat.DataIngestionService;
import io.github.thoroldvix.api.TranscriptContent;
import io.github.thoroldvix.api.YoutubeTranscriptApi;
import io.github.thoroldvix.internal.TranscriptApiFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeChatService {
    private final DataIngestionService dataIngestionService;
    private final ChatService chatService;

    public AiAnswer chat(ClientChatRequest request) {
        return chatService.chat(request);
    }

    public YoutubeChatResponse processing(String link) {
        try {
            String videoId = link.split("v=")[1];

            YoutubeTranscriptApi youtubeTranscriptApi = TranscriptApiFactory.createDefault();
            TranscriptContent transcriptContent = youtubeTranscriptApi.listTranscripts(videoId)
                    .findTranscript("en")
                    .fetch();

            if (Objects.nonNull(transcriptContent) && Objects.nonNull(transcriptContent.getContent()) && !transcriptContent.getContent().isEmpty()) {
                dataIngestionService.createCollection(videoId);

                String content = transcriptContent.getContent()
                        .stream()
                        .filter(c -> !c.getText().contains("[") && !c.getText().contains("]"))
                        .map(TranscriptContent.Fragment::getText)
                        .collect(Collectors.joining(" "));
                dataIngestionService.insertDocuments(content.replace("\n", " "), videoId);

                return new YoutubeChatResponse(String.format("Video %s is processed", link));
            } else {
                throw new InvalidDataException(String.format("Video %s doesn't have a transcript", link));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InvalidDataException(String.format("Video %s is not processed due to %s", link, e.getMessage()));
        }
    }
}

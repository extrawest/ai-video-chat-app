package com.youtube.chat.ai.controllers;

import com.youtube.chat.ai.models.AiAnswer;
import com.youtube.chat.ai.models.YoutubeChatResponse;
import com.youtube.chat.ai.services.YoutubeChatService;
import com.youtube.chat.ai.services.chat.ClientChatRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class YoutubeChatController {
    private final YoutubeChatService service;

    @Operation(summary = "Ready to use")
    @PostMapping( "/youtube/processing")
    public ResponseEntity<YoutubeChatResponse> chat(@RequestParam String link) {
        YoutubeChatResponse response = service.processing(link);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Ready to use")
    @PostMapping( "/youtube/chat")
    public ResponseEntity<AiAnswer> chat(@RequestBody ClientChatRequest request) {
        AiAnswer response = service.chat(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package com.youtube.chat.ai.services.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ClientChatRequest {
    private String videoLink;
    private String userMsg;
}
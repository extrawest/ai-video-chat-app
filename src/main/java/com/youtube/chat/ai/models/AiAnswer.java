package com.youtube.chat.ai.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiAnswer {
    private String text;
    private AiSourceType source;
}

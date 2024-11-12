package com.youtube.chat.ai.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "'Youtube Chat AI Service' REST API", version = "1.0",
                description = "'Youtube Chat AI Service' REST API endpoints",
                contact = @Contact(name = "'Youtube Chat AI Service' team")
        )
)
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        SpringDocUtils.getConfig().replaceWithClass(LocalDateTime.class, Long.class);
        SpringDocUtils.getConfig().replaceWithClass(LocalDate.class, Long.class);
        SpringDocUtils.getConfig().replaceWithClass(Date.class, Long.class);

        return GroupedOpenApi.builder()
                .group("YoutubeChatAIService")
                .packagesToScan("com.youtube.chat.ai")
                .build();
    }

}

package com.piligrimchick.domain;

import org.telegram.telegrambots.meta.api.objects.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class IncomingMessage {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("text")
    private String text;

    public IncomingMessage(Message message) {
        this.chatId = message.getChatId().toString();
        this.text = message.hasText() ? message.getText() : null;
    }

    public String toJson() {
        try {
            return OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON Serialization Error", e);
        }
    }

    public static IncomingMessage fromJson(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, IncomingMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON Deserialization Error", e);
        }
    }
}

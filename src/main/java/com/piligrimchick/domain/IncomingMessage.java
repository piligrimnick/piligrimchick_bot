package com.piligrimchick.domain;

import org.telegram.telegrambots.meta.api.objects.Message;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class IncomingMessage {
    private final String chatId;
    private final String text;

    public IncomingMessage(Message message) {
        this.chatId = message.getChatId().toString();
        this.text = message.hasText() ? message.getText() : null;
    }

    public byte[] getBytes() {
        return this.toString().getBytes();
    }
}

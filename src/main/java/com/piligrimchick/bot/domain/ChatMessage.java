package com.piligrimchick.bot.domain;

public class ChatMessage {
    private final String chatId;
    private final String text;

    public ChatMessage(String chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }

    public String getChatId() {
        return chatId;
    }

    public String getText() {
        return text;
    }
}

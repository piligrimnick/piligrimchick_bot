package com.piligrimchick.bot.application;

import com.piligrimchick.bot.application.message_processing.MessageProcessingStrategy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class TelegramMessageHandler {
    private final List<MessageProcessingStrategy> strategies;

    public TelegramMessageHandler(List<MessageProcessingStrategy> strategies) {
        this.strategies = strategies;
    }

    public void handleMessage(Message message) {
        System.out.println(message);

        for (MessageProcessingStrategy strategy : strategies) {
            if (strategy.supports(message)) {
                strategy.processMessage(message);
                return;
            }
        }
        System.out.println("No suitable strategy found for message");
    }
}

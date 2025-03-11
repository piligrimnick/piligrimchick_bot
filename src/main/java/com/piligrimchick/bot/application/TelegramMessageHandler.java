package com.piligrimchick.bot.application;

import com.piligrimchick.bot.application.message_processing.MessageProcessingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
@DependsOn("natsStreamsInitializer")
public class TelegramMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(TelegramMessageHandler.class);
    private final List<MessageProcessingStrategy> strategies;

    public TelegramMessageHandler(List<MessageProcessingStrategy> strategies) {
        this.strategies = strategies;
    }

    public void handleMessage(Message message) {
        for (MessageProcessingStrategy strategy : strategies) {
            if (strategy.supports(message)) {
                logger.info("Processing message with strategy: {}", strategy.getClass().getSimpleName());
                strategy.processMessage(message);
                return;
            }
        }
        logger.warn("No suitable strategy found for message: {}", message);
    }
}

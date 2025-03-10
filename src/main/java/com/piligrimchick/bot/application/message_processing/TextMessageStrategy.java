package com.piligrimchick.bot.application.message_processing;

import com.piligrimchick.domain.IncomingMessage;
import com.piligrimchick.shared.infrastructure.NatsPublisher;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.springframework.stereotype.Component;

@Component
public class TextMessageStrategy extends AbstractMessageProcessingStrategy {
    public TextMessageStrategy(NatsPublisher natsPublisher) {
        super(natsPublisher);
    }

    @Override
    public void processMessage(Message message) {
        IncomingMessage incomingMessage = new IncomingMessage(message);
        publishToNats("messages.text", incomingMessage);
    }

    @Override
    public boolean supports(Message message) {
        return message.hasText();
    }
}

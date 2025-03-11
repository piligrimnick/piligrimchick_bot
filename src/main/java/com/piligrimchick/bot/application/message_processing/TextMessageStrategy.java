package com.piligrimchick.bot.application.message_processing;

import com.piligrimchick.domain.IncomingMessage;
import com.piligrimchick.shared.infrastructure.NatsPublisher;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
@Component
@DependsOn("natsStreamsInitializer")
public class TextMessageStrategy extends AbstractMessageProcessingStrategy {
    private final String subject;

    public TextMessageStrategy(NatsPublisher natsPublisher, @Value("${nats.textPipeline.subject}") String subject) {
        super(natsPublisher);
        this.subject = subject;
    }

    @Override
    public void processMessage(Message message) {
        IncomingMessage incomingMessage = new IncomingMessage(message);
        publishToNats(subject, incomingMessage);
    }

    @Override
    public boolean supports(Message message) {
        return message.hasText();
    }
}

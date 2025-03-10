package com.piligrimchick.bot.application.message_processing;

import com.piligrimchick.shared.infrastructure.NatsPublisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class AbstractMessageProcessingStrategy implements MessageProcessingStrategy {
    protected final NatsPublisher natsPublisher;

    public AbstractMessageProcessingStrategy(@Qualifier("botNatsPublisher") NatsPublisher natsPublisher) {
        this.natsPublisher = natsPublisher;
    }

    protected void publishToNats(String queue, String payload) {
        System.out.println("Publishing to " + queue + ": " + payload);
        natsPublisher.publish(queue, payload);
    }

    public abstract boolean supports(Message message);
    public abstract void processMessage(Message message);
}

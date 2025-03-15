package com.piligrimchick.shared.infrastructure;

import io.nats.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.piligrimchick.bot.application.TelegramMessageHandler;
import com.piligrimchick.domain.IncomingMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NatsPublisher {
    private static final Logger logger = LoggerFactory.getLogger(TelegramMessageHandler.class);
    private final JetStream js;

    public NatsPublisher(String natsUrl) {
        try {
            this.js = NatsConnectionUtil.getJetStream(natsUrl);
            logger.info("Connected to NATS JetStream: {}", natsUrl);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to connect to NATS JetStream", e);
        }
    }

    public void publish(String subject, IncomingMessage message) {
        try {
            js.publish(subject, message.toJson().getBytes(StandardCharsets.UTF_8));
            logger.info("Published to {}: {}", subject, message.toString());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to publish message to JetStream", e);
        }
    }
}
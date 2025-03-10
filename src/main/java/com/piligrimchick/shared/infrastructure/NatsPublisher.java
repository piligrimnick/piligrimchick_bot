package com.piligrimchick.shared.infrastructure;

import io.nats.client.Connection;
import io.nats.client.Nats;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.piligrimchick.bot.application.TelegramMessageHandler;
import com.piligrimchick.domain.IncomingMessage;

public class NatsPublisher {
    private static final Logger logger = LoggerFactory.getLogger(TelegramMessageHandler.class);
    private final Connection connection;

    public NatsPublisher(String natsUrl) throws InterruptedException {
        this.connection = connectWithRetry(natsUrl, 3, 2000);
        logger.info("Connected to NATS: {}", natsUrl);
    }

    private Connection connectWithRetry(String natsUrl, int maxRetries, int delayMs) throws InterruptedException {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                return Nats.connect(natsUrl);
            } catch (IOException e) {
                attempt++;
                logger.error("Failed to connect to NATS (attempt {}/{})", attempt, maxRetries);

                if (attempt >= maxRetries) {
                    throw new IllegalStateException("Could not connect to NATS after " + maxRetries + " attempts", e);
                }
                Thread.sleep(delayMs);
            }
        }
        throw new IllegalStateException("Unexpected error: NATS connection logic exited loop");
    }

    public void publish(String queue, IncomingMessage message) {
        try {
            connection.publish(queue, message.getBytes());
            logger.info("Published to {}: {}", queue, message);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to publish message to NATS", e);
        }
    }
}

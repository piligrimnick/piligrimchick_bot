package com.piligrimchick.shared.infrastructure;

import io.nats.client.*;
import io.nats.client.api.ConsumerConfiguration;
import io.nats.client.api.AckPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractNatsConsumer implements NatsConsumer {
    private static final Logger logger = LoggerFactory.getLogger(AbstractNatsConsumer.class);

    private final String subject;
    private final String durableName;
    private final String queueGroup;
    private final Connection connection;
    private JetStream js;
    private JetStreamSubscription subscription;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public AbstractNatsConsumer(String subject, String natsUrl, String durableName, String queueGroup) {
        this.subject = subject;
        this.durableName = durableName;
        this.queueGroup = queueGroup;
        this.connection = NatsConnectionUtil.connectWithRetry(natsUrl);
    }

    @PostConstruct
    public void start() {
        try {
            js = connection.jetStream();
            subscribeToJetStream();
            processMessages();
        } catch (Exception e) {
            logger.error("Failed to initialize consumer for subject: {}", subject, e);
        }
    }

    private void subscribeToJetStream() throws Exception {
        ConsumerConfiguration config = ConsumerConfiguration.builder()
            .durable(durableName) // Гарантированная доставка сообщений
            .ackPolicy(AckPolicy.Explicit) // Явное подтверждение
            .build();

        PushSubscribeOptions options = PushSubscribeOptions.builder()
            .configuration(config)
            .build();

        subscription = js.subscribe(subject, queueGroup, options);
        logger.info("Subscribed to JetStream subject: {} with Durable Name: {} and Queue Group: {}", subject, durableName, queueGroup);
    }

    private void processMessages() {
        executorService.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Message message = subscription.nextMessage(Duration.ofSeconds(5));
                    if (message != null) {
                        String data = new String(message.getData(), StandardCharsets.UTF_8);
                        logger.info("Received JetStream message on [{}]: {}", subject, data);
                        processMessage(data);
                        message.ack(); // Подтверждаем обработку сообщения
                    }
                }
            } catch (Exception e) {
                logger.error("Error processing message from subject: {}", subject, e);
            }
        });
    }

    @PreDestroy
    public void stop() {
        unsubscribeFromJetStream();
        executorService.shutdownNow(); // Завершаем поток корректно
        closeConnection();
    }

    private void unsubscribeFromJetStream() {
        if (subscription != null) {
            subscription.unsubscribe();
            logger.info("Unsubscribed from subject: {}", subject);
        }
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                logger.error("Error while closing NATS connection", e);
            }
        }
    }

    protected abstract void processMessage(String data);
}

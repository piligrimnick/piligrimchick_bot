package com.piligrimchick.shared.infrastructure;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamManagement;
import io.nats.client.Nats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

public class NatsConnectionUtil {
    private static final Logger logger = LoggerFactory.getLogger(NatsConnectionUtil.class);
    private static final int MAX_RETRIES = 3;
    private static final Duration RETRY_DELAY = Duration.ofSeconds(2);

    public static Connection connectWithRetry(String natsUrl) {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                logger.info("Attempting to connect to NATS (attempt {}/{})", attempt, MAX_RETRIES);
                Connection connection =  Nats.connect(natsUrl);
                if (connection == null || !connection.getStatus().equals(Connection.Status.CONNECTED)) {
                    throw new IllegalStateException("❌ Failed to establish a connection to NATS!");
                }
                
                logger.info("✅ Successfully connected to NATS!");
                logger.info(connection.toString());
                logger.info("---");

                return connection;
            } catch (Exception e) {
                logger.error("Failed to connect to NATS (attempt {}/{}). Retrying...", attempt, MAX_RETRIES, e);
                try {
                    Thread.sleep(RETRY_DELAY.toMillis());
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrupted during NATS connection retry", interruptedException);
                }
            }
        }
        throw new RuntimeException("Unable to establish NATS connection after retries");
    }

    public static JetStream getJetStream(String natsUrl) throws IOException {
        Connection connection = connectWithRetry(natsUrl);
        JetStream js = connection.jetStream();
        if (js == null) {
            throw new IllegalStateException("❌ Failed to initialize JetStream!");
        }
        
        logger.info("✅ JetStream initialized successfully!");
        logger.info(js.toString());
        logger.info("---");

        return js;
    }


    public static JetStreamManagement getJetStreamManagement(String natsUrl) throws IOException {
        Connection connection = connectWithRetry(natsUrl);
        JetStreamManagement jsm = connection.jetStreamManagement();
        if (jsm == null) {
            throw new IllegalStateException("❌ Failed to initialize JetStreamManagement!");
        }
        
        logger.info("✅ JetStreamManagement initialized successfully!");
        logger.info(jsm.toString());
        logger.info("---");

        return jsm;
    }
}

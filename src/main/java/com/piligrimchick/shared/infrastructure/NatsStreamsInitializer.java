package com.piligrimchick.shared.infrastructure;

import io.nats.client.*;
import io.nats.client.api.StreamConfiguration;
import io.nats.client.api.StorageType;
import io.nats.client.api.RetentionPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Arrays;
import java.io.IOException;

@Component
public class NatsStreamsInitializer {
    private static final Logger logger = LoggerFactory.getLogger(NatsStreamsInitializer.class);
    private static final List<String> MODULE_KEYS = Arrays.asList("textPipeline");
    
    private final JetStreamManagement jsm;
    private final Environment env;


    public NatsStreamsInitializer(@Value("${nats.url}") String natsUrl, Environment env) throws Exception {
        this.jsm = NatsConnectionUtil.getJetStreamManagement(natsUrl);
        this.env = env;

    }

    @PostConstruct
    public void initializeStreams() throws IOException {
        for (String module : MODULE_KEYS) {
            String subject = getNatsSubject(module);
            logger.info("Attempt to create stream: {}", subject);
            createStreamIfNotExists(subject);
        }
    }

    private String getNatsSubject(String module) {
        return env.getProperty(String.format("nats.%s.subject", module), "default_subject");
    }

    private void createStreamIfNotExists(String stream) throws IOException {
        try {
            jsm.getStreamInfo(stream);
        } catch (JetStreamApiException | IOException e) {
            try {
                StreamConfiguration streamConfig = StreamConfiguration.builder()
                    .name(stream)
                    .subjects(stream)
                    .storageType(StorageType.File)
                    .retentionPolicy(RetentionPolicy.WorkQueue)
                    .build();
                jsm.addStream(streamConfig);
                logger.info("Stream `{}` created", stream);
            } catch (Exception ex) {
                logger.error("Failed to create stream: {}", stream, ex);
            }
        }
    }
}

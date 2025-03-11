package com.piligrimchick.text_pipeline.infrastructure;

import com.piligrimchick.shared.infrastructure.AbstractNatsConsumer;
import com.piligrimchick.text_pipeline.application.TextProcessingService;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;

@Component
@DependsOn("natsStreamsInitializer")
public class TextPipelineConsumer extends AbstractNatsConsumer {
    private static final Logger logger = LoggerFactory.getLogger(TextPipelineConsumer.class);
    private final TextProcessingService processor;
    
    public TextPipelineConsumer(
        @Value("${nats.textPipeline.subject}") String subject,
        @Value("${nats.url}") String natsUrl,
        @Value("${nats.textPipeline.durableName}") String durableName,
        @Value("${nats.textPipeline.queueGroup}") String queueGroup,
        TextProcessingService processor) {
        super(subject, natsUrl, durableName, queueGroup);
        this.processor = processor;
    }

    @PostConstruct
    public void init() {
        logger.info("TextPipelineConsumer initialized and ready to process messages.");
    }

    @Override
    protected void processMessage(String message) {
        processor.handleMessage(message);
    }
}

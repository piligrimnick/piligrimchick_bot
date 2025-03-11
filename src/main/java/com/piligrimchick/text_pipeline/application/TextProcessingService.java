package com.piligrimchick.text_pipeline.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TextProcessingService {
    private static final Logger logger = LoggerFactory.getLogger(TextProcessingService.class);

    public void handleMessage(String message) {
        logger.info("Processing text message: {}", message);
        // TODO: Реализовать обработку сообщения
    }
}

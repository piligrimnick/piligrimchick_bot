package com.piligrimchick.shared.config;

import com.piligrimchick.shared.infrastructure.NatsPublisher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Configuration
public class NatsConfig implements ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(NatsConfig.class);
    private String mainClass;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.mainClass = detectMainClass();
        logger.info("✅ Detected main application: {}", mainClass);
    }

    @Bean(name = "botNatsPublisher")
    public NatsPublisher botNatsPublisher(@Value("${nats.url}") String url) throws InterruptedException {
        if (!mainClass.equals("bot")) {
            return null;
        }
        return new NatsPublisher(url);
    }

    private String detectMainClass() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().contains("com.piligrimchick.bot.BotApplication")) {
                return "bot";
            }
            if (element.getClassName().contains("com.piligrimchick.text_pipeline.TextPipelineApplication")) {
                return "text_pipeline";
            }
        }
        return "unknown";
    }


    // @Bean(name = "transcriptorNatsPublisher")
    // public NatsPublisher transcriptorNatsPublisher(@Value("${transcriptor.nats.url}") String url) {
    //     return new NatsPublisher(url);
    // }
}

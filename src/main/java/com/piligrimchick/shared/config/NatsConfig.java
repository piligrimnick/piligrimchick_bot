package com.piligrimchick.shared.config;

import com.piligrimchick.shared.infrastructure.NatsPublisher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NatsConfig {

    @Bean(name = "botNatsPublisher")
    public NatsPublisher botNatsPublisher(@Value("${bot.nats.url}") String url) throws InterruptedException {
        return new NatsPublisher(url);
    }


    // @Bean(name = "transcriptorNatsPublisher")
    // public NatsPublisher transcriptorNatsPublisher(@Value("${transcriptor.nats.url}") String url) {
    //     return new NatsPublisher(url);
    // }
}

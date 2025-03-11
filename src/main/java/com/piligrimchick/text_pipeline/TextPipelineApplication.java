package com.piligrimchick.text_pipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.piligrimchick.text_pipeline", "com.piligrimchick.shared"})
public class TextPipelineApplication {
    public static void main(String[] args) {
        SpringApplication.run(TextPipelineApplication.class, args);
    }
}
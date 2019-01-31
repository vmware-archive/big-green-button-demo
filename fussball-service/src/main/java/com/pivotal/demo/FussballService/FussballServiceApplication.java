package com.pivotal.demo.FussballService;

import io.micrometer.core.instrument.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class FussballServiceApplication {
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final Logger logger = LoggerFactory.getLogger(FussballServiceApplication.class);

    @Bean
    ApplicationRunner runner() {
        // periodically update a metric and emit a log message for testing
        return args -> {
            this.executorService.scheduleAtFixedRate(() -> {
                Metrics.counter("app.fussball.count").increment();
                logger.info("Fussball service PING");
            }, 1, 10, TimeUnit.SECONDS);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(FussballServiceApplication.class, args);
    }
}

package com.flytrap.rssreader.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "alertThreadExecutor")
    public ThreadPoolTaskExecutor alertThreadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setThreadNamePrefix("alertThreadExecutor-");
        executor.initialize();
        return executor;
    }
}

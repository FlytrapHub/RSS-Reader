package com.flytrap.rssreader.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    private static final int THREADS_COUNT = 1;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(THREADS_COUNT);
        threadPoolTaskScheduler.setThreadNamePrefix("MY-SCHEDULER-");
        return threadPoolTaskScheduler;
    }
}

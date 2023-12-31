package com.flytrap.rssreader.global.config;

import com.flytrap.rssreader.domain.alert.q.SubscribeEventQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventQueueInitializer {
    @Bean
    public SubscribeEventQueue transactionEventQueue() {
        return SubscribeEventQueue.of(1_0);
    }
}

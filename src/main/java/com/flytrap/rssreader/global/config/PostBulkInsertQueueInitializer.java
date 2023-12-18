package com.flytrap.rssreader.global.config;

import com.flytrap.rssreader.domain.post.q.PostBulkInsertQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostBulkInsertQueueInitializer {

    @Bean
    public PostBulkInsertQueue transactionPostBulkInsertQueue() {
        return PostBulkInsertQueue.of(1_00);
    }
}

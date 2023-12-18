package com.flytrap.rssreader.global.config;

import com.flytrap.rssreader.domain.post.q.PostBulkInsertQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostBulkInsertQueueInitializer {


    private static final int QUEUE_SIZE = 1_000;

    private final PostBulkInsertQueue postBulkInsertQueue = PostBulkInsertQueue.of(QUEUE_SIZE);

    @Bean
    public PostBulkInsertQueue transactionPostBulkInsertQueue() {
        return postBulkInsertQueue;
    }
}

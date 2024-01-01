package com.flytrap.rssreader.domain.post.q;

import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostBulkInsertPublisher {

    private final ApplicationEventPublisher publisher;

    public void publish(PostEntity post) {
        log.info("Create new offer post! {}", post.toString());
        publisher.publishEvent(post);
    }
}

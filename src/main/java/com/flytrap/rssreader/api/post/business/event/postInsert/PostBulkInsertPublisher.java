package com.flytrap.rssreader.api.post.business.event.postInsert;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostBulkInsertPublisher {

    private final ApplicationEventPublisher publisher;

    public void publish(PostEntity post) { //TODO : entity -> domain
        log.info("Create new offer post! {}", post.toString());
        publisher.publishEvent(post);
    }
}

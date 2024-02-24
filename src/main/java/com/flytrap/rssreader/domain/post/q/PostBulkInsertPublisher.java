package com.flytrap.rssreader.domain.post.q;

import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Deprecated(since = "이제는 사용하지 않는 도메인입니다.")
public class PostBulkInsertPublisher {

    private final ApplicationEventPublisher publisher;

    public void publish(PostEntity post) { //TODO : entity -> domain
        log.info("Create new offer post! {}", post.toString());
        publisher.publishEvent(post);
    }
}

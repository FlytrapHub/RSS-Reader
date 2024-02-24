package com.flytrap.rssreader.api.post.business.event.postInsert;

import com.flytrap.rssreader.domain.post.q.PostBulkInsertQueue;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostBulkInsertListener {

    private final PostBulkInsertQueue eventQueue;

    @EventListener
    public void onEvent(PostEntity post) { //TODO : entity -> domain
        if (eventQueue.isFull()) {
            log.info("eventQueue full ");
            return;
        }
        eventQueue.offer(post);
    }
}

package com.flytrap.rssreader.domain.post.q;

import com.flytrap.rssreader.domain.alert.SubscribeEvent;
import com.flytrap.rssreader.domain.alert.q.SubscribeEventQueue;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Deprecated(since = "이제는 사용하지 않는 도메인입니다.")
public class PostBulkInsertListener {

    private final PostBulkInsertQueue eventQueue;

    @EventListener
    public void onEvent(PostEntity post) {
        if (eventQueue.isFull()) {
            log.info("eventQueue full ");
            return;
        }
        eventQueue.offer(post);
    }
}

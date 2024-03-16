package com.flytrap.rssreader.domain.alert.q;

import com.flytrap.rssreader.domain.alert.SubscribeEvent;
import com.flytrap.rssreader.domain.subscribe.Subscribe;
import com.flytrap.rssreader.infrastructure.repository.AlertEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Deprecated
public class SubscribeEventListener {

    private final SubscribeEventQueue eventQueue;

    @EventListener
    public void onEvent(SubscribeEvent event) {
        if (eventQueue.isFull()) {
            log.info("eventQueue full ");
            return;
        }
        eventQueue.offer(event);
    }
}

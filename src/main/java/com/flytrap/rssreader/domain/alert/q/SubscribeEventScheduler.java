package com.flytrap.rssreader.domain.alert.q;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeEventScheduler {

    private final SubscribeEventQueue eventQueue;

    @Async("taskScheduler")
    @Scheduled(fixedRate = 100)
    public void schedule() {
        new SubscribeEventWorker(eventQueue)
                .run();
    }
}

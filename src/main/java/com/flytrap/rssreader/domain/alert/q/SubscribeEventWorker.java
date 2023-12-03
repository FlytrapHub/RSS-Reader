package com.flytrap.rssreader.domain.alert.q;

import com.flytrap.rssreader.domain.subscribe.Subscribe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class SubscribeEventWorker implements Runnable {

    private final SubscribeEventQueue eventQueue;

    @Override
    @Transactional
    public void run() {
        if (eventQueue.isRemaining()) {
            Subscribe subscribe = eventQueue.poll();
            log.info("eventQueue poll 의 상태 = {}  ", subscribe.toString());
        }
    }

    private void processing(int processingTimeInMs) {
        try {
            Thread.sleep(processingTimeInMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

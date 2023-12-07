package com.flytrap.rssreader.domain.alert.q;

import com.flytrap.rssreader.domain.alert.SubscribeEvent;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscribeEventQueue {

    private final Queue<SubscribeEvent> queue;
    private final int queueSize;

    private SubscribeEventQueue(int size) {
        this.queueSize = size;
        this.queue = new LinkedBlockingQueue<>(queueSize);
    }

    public static SubscribeEventQueue of(int size) {
        return new SubscribeEventQueue(size);
    }

    public boolean offer(SubscribeEvent event) {
        boolean returnValue = queue.offer(event);
        healthCheck();
        return returnValue;
    }

    public SubscribeEvent peek() {
        return queue.peek();
    }


    public SubscribeEvent poll() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("No events in the queue !");
        }
        SubscribeEvent event = queue.poll();
        healthCheck();
        return event;
    }

    private int size() {
        return queue.size();
    }

    public boolean isFull() {
        return size() == queueSize;
    }

    public boolean isRemaining() {
        return size() > 0;
    }

    private void healthCheck() {
        log.info("{\"totalQueueSize\":{}, \"currentQueueSize\":{}}", size());
    }
}

package com.flytrap.rssreader.domain.alert.q;

import com.flytrap.rssreader.domain.subscribe.Subscribe;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscribeEventQueue {

    private final Queue<Subscribe> queue;
    private final int queueSize;

    private SubscribeEventQueue(int size) {
        this.queueSize = size;
        this.queue = new LinkedBlockingQueue<>(queueSize);
    }

    public static SubscribeEventQueue of(int size) {
        return new SubscribeEventQueue(size);
    }

    public boolean offer(Subscribe subscribe) {
        boolean returnValue = queue.offer(subscribe);
        healthCheck();
        return returnValue;
    }

    public Subscribe peek() {
        return queue.peek();
    }


    public Subscribe poll() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("No events in the queue !");
        }
        Subscribe subscribe = queue.poll();
        healthCheck();
        return subscribe;
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

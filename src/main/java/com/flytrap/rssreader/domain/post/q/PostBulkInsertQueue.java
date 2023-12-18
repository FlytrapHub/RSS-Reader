package com.flytrap.rssreader.domain.post.q;

import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostBulkInsertQueue {

    private final Queue<PostEntity> queue;
    private final int space;

    private PostBulkInsertQueue(int size) {
        this.space = size;
        this.queue = new LinkedBlockingQueue<>(space);
    }

    public static PostBulkInsertQueue of(int size) {
        return new PostBulkInsertQueue(size);
    }

    public boolean offer(PostEntity event) {
        boolean returnValue = queue.offer(event);
        return returnValue;
    }

    public PostEntity peek() {
        return queue.peek();
    }


    public PostEntity poll() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("No events in the queue !");
        }
        PostEntity event = queue.poll();
        return event;
    }

    public int size() {
        return queue.size();
    }

    public boolean isFull() {
        return size() == space;
    }

    public boolean isRemaining() {
        return size() > 0;
    }

    public List<PostEntity> pollBatch(int batchSize) {
        List<PostEntity> postsToInsert = new ArrayList<>();
        for (int i = 0; i < batchSize && !queue.isEmpty(); i++) {
            log.info("======================================");
            log.info("bulkInsertQueue.size = {}", queue.size());
            log.info("bulkInsertQueue.hashCode = {}", queue.hashCode());
            postsToInsert.add(queue.poll());
        }
        return postsToInsert;
    }
}

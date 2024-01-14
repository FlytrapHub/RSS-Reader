package com.flytrap.rssreader.domain.post.q;

import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostBulkInsertQueue {

    private final BlockingQueue<PostEntity> queue;
    private final int space;

    private PostBulkInsertQueue(int size) {
        this.space = size;
        this.queue = new LinkedBlockingQueue<>(size);
    }

    public static PostBulkInsertQueue of(int size) {
        return new PostBulkInsertQueue(size);
    }

    public boolean offer(PostEntity event) {
        return queue.offer(event);
    }

    public PostEntity peek() {
        return queue.peek();
    }

    public PostEntity poll() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("No events in the queue!");
        }
        return queue.poll();
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

    public List<PostEntity> pollAll() {
        List<PostEntity> polledPosts = new ArrayList<>();
        queue.drainTo(polledPosts);
        return polledPosts;
    }
}

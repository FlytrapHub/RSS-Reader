package com.flytrap.rssreader.api.post.infrastructure.output;

import com.flytrap.rssreader.infrastructure.entity.post.SubscribePostCount;

public class EmptySubscribePostCountOutput implements SubscribePostCount {

    @Override
    public long getSubscribeId() {
        return 0;
    }

    @Override
    public int getPostCount() {
        return 0;
    }
}

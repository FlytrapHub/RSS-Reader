package com.flytrap.rssreader.infrastructure.entity.post;

@Deprecated
public class EmptySubscribePostCount implements SubscribePostCount {

    @Override
    public long getSubscribeId() {
        return 0;
    }

    @Override
    public int getPostCount() {
        return 0;
    }
}

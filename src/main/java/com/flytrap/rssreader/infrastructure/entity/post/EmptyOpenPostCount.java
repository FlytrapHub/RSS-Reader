package com.flytrap.rssreader.infrastructure.entity.post;

public class EmptyOpenPostCount implements OpenPostCount {

    @Override
    public long getSubscribeId() {
        return 0;
    }

    @Override
    public int getPostCount() {
        return 0;
    }
}

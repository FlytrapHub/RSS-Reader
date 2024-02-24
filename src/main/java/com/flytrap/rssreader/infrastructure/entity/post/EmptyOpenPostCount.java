package com.flytrap.rssreader.infrastructure.entity.post;

/**
 * 빈 OpenPostCount
 * 테이블에 레코드가 없을때 사용합니다.
 */
@Deprecated
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

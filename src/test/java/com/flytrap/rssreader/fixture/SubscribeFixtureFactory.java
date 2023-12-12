package com.flytrap.rssreader.fixture;

import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.domain.subscribe.Subscribe;

public class SubscribeFixtureFactory {

    public static Subscribe generateSubscribe() {
        return Subscribe.builder()
                .id(1L)
                .title("에이프의 블로그")
                .url("https://blog.naver.com/PostList.nhn?blogId=ape")
                .description("에이프의 블로그")
                .build();
    }

    public static Subscribe generateSubscribe(long id) {
        return Subscribe.builder()
                .id(id)
                .title("??의 블로그")
                .url("https://blog.naver.com/PostList.nhn?blogId=??")
                .description("누군가의 블로그")
                .build();
    }
}

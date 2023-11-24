package com.flytrap.rssreader.infrastructure.api;

import com.flytrap.rssreader.domain.subscribe.BlogPlatform;
import com.flytrap.rssreader.presentation.dto.RssFeedData;
import com.flytrap.rssreader.presentation.dto.SubscribeRequest.CreateRequest;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.HttpClientFeedFetcher;
import com.sun.syndication.io.FeedException;
import java.io.IOException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RssChecker {

    /**
     * * rss URL을 입력하면 rss/interesting 으로 리다이렉션됩니다. rome-fetcher 모듈 의 HttpClientFeedFetcher를 사용합니다.
     * 이 * 모듈은 리디렉션을 처리하고 기타 고급 기능(캐싱, 조건부 GET, 압축)을 제공합니다.
     *
     * @param request
     * @return
     */
    public RssFeedData checker(CreateRequest request) {
        HttpClientFeedFetcher feedFetcher = new HttpClientFeedFetcher();
        try {
            SyndFeed feed = feedFetcher.retrieveFeed(new URL(request.blogUrl()));
            BlogPlatform blogPlatform = BlogPlatform.parseLink(feed.getLink());
            return new RssFeedData(feed.getTitle(), request.blogUrl(), blogPlatform,
                    feed.getDescription());
        } catch (IllegalArgumentException | IOException | FeedException | FetcherException e) {
            e.printStackTrace();
        }
        //TODO: null일경우 예외
        return null;
    }
}

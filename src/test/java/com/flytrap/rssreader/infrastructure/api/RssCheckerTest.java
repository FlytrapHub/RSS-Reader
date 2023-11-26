package com.flytrap.rssreader.infrastructure.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.flytrap.rssreader.domain.subscribe.BlogPlatform;
import com.flytrap.rssreader.presentation.dto.RssFeedData;
import com.flytrap.rssreader.presentation.dto.SubscribeRequest.CreateRequest;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("RssChecker테스트 -Subscribe")
class RssCheckerTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "https://v2.velog.io/rss/jinny-l",
            "https://jinney.tistory.com/rss"
    })
    void parseRssDocuments_Success(String rssUrl) {
        // given
        CreateRequest createRequest = new CreateRequest(rssUrl);
        RssChecker rssChecker = new RssChecker();

        // when
        Optional<RssFeedData> result = rssChecker.parseRssDocuments(createRequest);
        BlogPlatform blogPlatform = BlogPlatform.parseLink(createRequest.blogUrl());

        // then

        SoftAssertions.assertSoftly(softAssertions -> {
            assertTrue(true);
            assertEquals(createRequest.blogUrl(), result.orElseThrow().url());
            Assertions.assertNotNull(result.get().title());
            Assertions.assertNotNull(result.get().description());
            Assertions.assertNotNull(result.get().platform());
            Assertions.assertEquals(blogPlatform, result.get().platform());
        });
    }
}

package com.flytrap.rssreader.service;

import static com.flytrap.rssreader.fixture.FixtureFactory.generateSubscribeEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.domain.subscribe.Subscribe;
import com.flytrap.rssreader.fixture.FixtureFactory;
import com.flytrap.rssreader.infrastructure.api.RssChecker;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.SubscribeEntityJpaRepository;
import com.flytrap.rssreader.presentation.dto.RssFeedData;
import com.flytrap.rssreader.presentation.dto.SubscribeRequest.CreateRequest;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Subscribe 서비스 로직 - SubscribeSerbvice")
@ExtendWith(MockitoExtension.class)
public class SubscribeServiceTest {

    @Mock
    SubscribeEntityJpaRepository subscribeRepository;

    @Mock
    RssChecker rssChecker;

    @InjectMocks
    SubscribeService subscribeService;

    CreateRequest request = new CreateRequest("https://v2.velog.io/rss/jinny-l");

    @Nested
    @DisplayName("회원은 url을 통해 폴더에 rss url을 구독한다.")
    class subscribe {

        @Test
        @DisplayName("URL이 없으면 DB에 저장한다.")
        void subscribe_save_success() {
            // given
            Optional<RssFeedData> rssFeedData = FixtureFactory.generateRssData();
            SubscribeEntity subscribeEntity = generateSubscribeEntity();

            when(rssChecker.parseRssDocuments(request)).thenReturn(rssFeedData);
            when(subscribeRepository.existsByUrl(request.blogUrl())).thenReturn(false);
            when(subscribeRepository.save(Mockito.any(SubscribeEntity.class)))
                    .thenAnswer(i -> i.getArguments()[0]);

            // when
            subscribeService.subscribe(request);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                verify(subscribeRepository, times(1)).existsByUrl(request.blogUrl());
                verify(subscribeRepository, times(1)).save(any());
            });
        }

        @Test
        @DisplayName("URL이 있으면 DB에서 꺼내온다.")
        void subscribe_select_success() {
            // given
            Optional<RssFeedData> rssFeedData = FixtureFactory.generateRssData();
            when(rssChecker.parseRssDocuments(request)).thenReturn(rssFeedData);
            when(subscribeRepository.existsByUrl(request.blogUrl())).thenReturn(true);
            when(subscribeRepository.findByUrl(request.blogUrl())).thenReturn(
                    Optional.of(generateSubscribeEntity()));

            // when
            subscribeService.subscribe(request);
            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                verify(subscribeRepository, times(1)).existsByUrl(request.blogUrl());
                verify(subscribeRepository, times(1)).findByUrl(request.blogUrl());
            });
        }
    }

    @Test
    @DisplayName("URL폴더 구독을 취소한다.")
    void unsubscribe() {
        // given
        Optional<RssFeedData> rssFeedData = FixtureFactory.generateRssData();
        SubscribeEntity subscribeEntity = generateSubscribeEntity();
        Subscribe subscribe = subscribeEntity.toDomain(rssFeedData.orElseThrow());
        when(subscribeRepository.findById(subscribe.getId())).thenReturn(
                Optional.of(subscribeEntity));

        // when
        subscribeService.unsubscribe(subscribe.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            verify(subscribeRepository, times(1)).delete(subscribeEntity);
            verify(subscribeRepository, times(1)).findById(subscribe.getId());
        });
    }
}

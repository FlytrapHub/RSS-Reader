package com.flytrap.rssreader.service;

import static com.flytrap.rssreader.fixture.FixtureFactory.*;
import static com.flytrap.rssreader.fixture.FixtureFactory.generate50RssItemResourceList;
import static io.lettuce.core.internal.Futures.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import com.flytrap.rssreader.infrastructure.api.RssPostParser;
import com.flytrap.rssreader.infrastructure.api.dto.RssItemResource;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.SubscribeEntityJpaRepository;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@DisplayName("Post 서비스 로직 - PostCollector")
@ExtendWith(MockitoExtension.class)
class PostCollectServiceTest {

    @Mock
    RssPostParser postParser;

    @Mock
    SubscribeEntityJpaRepository subscribeEntityJpaRepository;

    @Mock
    PostEntityJpaRepository postEntityJpaRepository;

    @Spy
    ThreadPoolTaskExecutor taskExecutor;

    @InjectMocks
    PostCollectService postCollectService;

    @BeforeEach
    void init() {
        List<SubscribeEntity> subscribes = generateSingleSubscribeEntityList();
        when(subscribeEntityJpaRepository.findAll()).thenReturn(subscribes);
        when(postEntityJpaRepository.findAllBySubscribeOrderByPubDateDesc(any()))
            .thenReturn(generate100PostEntityList());

        taskExecutor.initialize();
    }

    @DisplayName("RSS 문서에서 파싱된 게시글 목록을 모두 DB에 저장할 수 있다.")
    @Test
    void collectPosts() {
        // given
        List<RssItemResource> itemResources = generate50RssItemResourceList();
        when(postParser.parseRssDocuments(anyString()))
            .thenReturn(itemResources);

        // when
        postCollectService.collectPosts();

        // then
        verify(postEntityJpaRepository, times(itemResources.size())).save(any());
    }

    @DisplayName("RSS 문서에서 파싱된 게시글 목록을 모두 DB에 저장할 수 있다.")
    @Test
    void collectPosts_async() throws InterruptedException {
        // given
        List<RssItemResource> itemResources = generate50RssItemResourceList();
        when(postParser.parseRssDocuments(anyString()))
                .thenReturn(itemResources);

        // when
        postCollectService.collectPosts();
        taskExecutor.getThreadPoolExecutor().awaitTermination(10, TimeUnit.SECONDS);

        // then
        verify(postEntityJpaRepository, times(itemResources.size())).save(any());
    }

    @DisplayName("RSS 문서에서 파싱된 게시글 목록을 모두 DB에 저장할 수 있다.")
    @Test
    void shouldCollectPostsAsyncAndSaveToDB() throws InterruptedException {
        // given
        List<RssItemResource> itemResources = generate50RssItemResourceList();
        when(postParser.parseRssDocuments(anyString())).thenReturn(itemResources);

        // when
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            postCollectService.collectPosts();
        });

        // then
        future.join(); // 비동기 작업이 완료될 때까지 기다림

        verify(postEntityJpaRepository, times(itemResources.size())).save(any());
        verify(postEntityJpaRepository, times(itemResources.size()))
                .save(argThat(itemResources::contains));
    }


}

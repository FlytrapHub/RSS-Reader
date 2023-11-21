package com.flytrap.rssreader.service;

import static com.flytrap.rssreader.fixture.FixtureFactory.*;
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
import org.springframework.core.task.TaskExecutor;

@DisplayName("Post 서비스 로직 - PostCollector")
@ExtendWith(MockitoExtension.class)
class PostCollectServiceTest {

    @Mock
    TaskExecutor taskExecutor;

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
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(taskExecutor).execute(any());
    }

    @DisplayName("RSS 문서에서 파싱된 게시글 목록을 모두 DB에 저장할 수 있다.")
    @Test
    void collectPosts() throws InterruptedException {

        // given
        List<RssItemResource> itemResources = generate50RssItemResourceList();
        when(postParser.parseRssDocuments(anyString()))
            .thenReturn(itemResources);

        // when
        postCollectService.collectPosts();

        // then
        verify(postEntityJpaRepository, times(itemResources.size())).save(any());
    }

}

package com.flytrap.rssreader.service;

import static com.flytrap.rssreader.fixture.FixtureFactory.generate100PostEntityList;
import static com.flytrap.rssreader.fixture.FixtureFactory.generate50RssItemResourceList;
import static com.flytrap.rssreader.fixture.FixtureFactory.generateSingleSubscribeEntityList;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.when;

import com.flytrap.rssreader.domain.post.q.PostBulkInsertPublisher;
import com.flytrap.rssreader.domain.post.q.PostBulkInsertQueue;
import com.flytrap.rssreader.infrastructure.api.RssPostParser;
import com.flytrap.rssreader.infrastructure.api.dto.RssSubscribeResource;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.SubscribeEntityJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("PostCollect 서비스 로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class PostCollectServiceTest {

    @Mock
    RssPostParser postParser;

    @Mock
    SubscribeEntityJpaRepository subscribeEntityJpaRepository;

    @Mock
    PostEntityJpaRepository postEntityJpaRepository;

    @Mock
    PostBulkInsertPublisher publisher;

    @Mock
    PostBulkInsertQueue bulkInsertQueue;

    @InjectMocks
    PostCollectService postCollectService;


    @BeforeEach
    void init() {
        List<SubscribeEntity> subscribes = generateSingleSubscribeEntityList();
        when(postEntityJpaRepository.findAllBySubscribeOrderByPubDateDesc(any()))
                .thenReturn(generate100PostEntityList());

    }

    @DisplayName("RSS 문서에서 파싱된 게시글 목록을 모두 DB에 저장할 수 있다.")
    @Test
    void processPostCollectionAsyncTest() throws InterruptedException {

        // given
        RssSubscribeResource subscribeResource = new RssSubscribeResource("title",
                generate50RssItemResourceList());
        SubscribeEntity subscribe = generateSingleSubscribeEntityList().get(0);

        // when
        when(postParser.parseRssDocuments(anyString())).thenReturn(Optional.of(subscribeResource));
        postCollectService.processPostCollectionAsync(subscribe).join();

        // then
        verify(subscribeEntityJpaRepository).save(subscribe);
        verify(postEntityJpaRepository).findAllBySubscribeOrderByPubDateDesc(subscribe);
        verify(publisher, times(subscribeResource.itemResources().size())).publish(any());
    }
}

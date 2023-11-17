package com.flytrap.rssreader.service;

import static com.flytrap.rssreader.fixture.FixtureFactory.*;
import static org.mockito.BDDMockito.*;

import com.flytrap.rssreader.infrastructure.api.RssPostParser;
import com.flytrap.rssreader.infrastructure.api.dto.RssItemResource;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.SubscribeEntityJpaRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Post 서비스 로직 - PostCollector")
@ExtendWith(MockitoExtension.class)
class PostCollectServiceTest {

    @Mock
    RssPostParser postParser;

    @Mock
    SubscribeEntityJpaRepository subscribeEntityJpaRepository;

    @Mock
    PostEntityJpaRepository postEntityJpaRepository;

    @InjectMocks
    PostCollectService postCollectService;

    @BeforeEach
    void init() {
        List<RssItemResource> itemResources = generateSingleItemResourceList();
        List<SubscribeEntity> subscribes = generateSingleSubscribeEntityList();

        when(postParser.parseRssDocument(anyString())).thenReturn(itemResources);
        when(subscribeEntityJpaRepository.findAll()).thenReturn(subscribes);
    }

    @DisplayName("RSS 문서에서 파싱된 게시글이 DB에 없을 경우 저장한다.")
    @Test
    void collectNotExistPosts() {
        // given
        when(postEntityJpaRepository.existsBySubscribeAndGuid(any(), anyString()))
            .thenReturn(false);

        // when
        postCollectService.collectPosts();

        // then
        verify(postEntityJpaRepository, times(1)).save(any());
    }

    @DisplayName("RSS 문서에서 파싱된 게시글이 DB에 이미 존재하는 경우 저장하지 않는다.")
    @Test
    void collectExistPosts() {
        // given
        when(postEntityJpaRepository.existsBySubscribeAndGuid(any(), anyString()))
            .thenReturn(true);

        // when
        postCollectService.collectPosts();

        // then
        verify(postEntityJpaRepository, times(0)).save(any());
    }

}
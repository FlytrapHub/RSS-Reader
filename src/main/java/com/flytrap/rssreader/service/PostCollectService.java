package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.alert.q.SubscribeEventPublisher;
import com.flytrap.rssreader.infrastructure.api.RssPostParser;
import com.flytrap.rssreader.infrastructure.api.dto.RssSubscribeResource;
import com.flytrap.rssreader.infrastructure.api.dto.RssSubscribeResource.RssItemResource;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.SubscribeEntityJpaRepository;
import com.flytrap.rssreader.service.alert.AlertFacadeService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class PostCollectService {

    private static final int TEN_MINUTE = 600_000;

    private final RssPostParser postParser;
    private final SubscribeEntityJpaRepository subscribeEntityJpaRepository;
    private final PostEntityJpaRepository postEntityJpaRepository;
    private final SubscribeEventPublisher publisher;
    private final AlertFacadeService alertFacadeService;
    private final TaskExecutor taskExecutor;

    @Scheduled(fixedDelay = TEN_MINUTE)
    public void collectPosts() {
        List<SubscribeEntity> subscribes = subscribeEntityJpaRepository.findAll();

        for (SubscribeEntity subscribe : subscribes) {
            processPostCollectionAsync(subscribe);
        }
        //TODO 스케줄 작업이 끝나면 알림 envet작업이 발생한다. 큐에 담긴 이벤트를 발행한다.
        alertFacadeService.alert();
    }

    /**
     * 구독한 블로그의 RSS에서 게시글들을 읽어서 DB에 저장한다.<br> 비동기 처리되어 있으며 블로그 하나당 하나의 스레드에서 동작한다.
     *
     * @param subscribe 구독한 블로그
     */
    private void processPostCollectionAsync(SubscribeEntity subscribe) {
        CompletableFuture<Map<String, String>> futurePosts = CompletableFuture.supplyAsync(() ->
                postParser.parseRssDocuments(subscribe.getUrl())
                        .map(resource -> {
                            updateSubscribeTitle(resource, subscribe);
                            return savePosts(resource, subscribe);
                        })
                        .orElse(new HashMap<>()));

        futurePosts.thenAccept(posts -> {
            if (!posts.isEmpty()) {
                publisher.publish(subscribe.toDomain());
            }
        });
    }

    private void updateSubscribeTitle(RssSubscribeResource subscribeResource,
            SubscribeEntity subscribe) {
        subscribe.updateTitle(subscribeResource.subscribeTitle());
        subscribeEntityJpaRepository.save(subscribe);
    }

    //TODO: 글이 새로 추가되면 슬랙에 보낼URL을 기억한다.
    private Map<String, String> savePosts(RssSubscribeResource subscribeResource,
            SubscribeEntity subscribe) {
        List<PostEntity> posts = postEntityJpaRepository.findAllBySubscribeOrderByPubDateDesc(
                subscribe);

        Map<String, PostEntity> postMap = convertListToHashSet(posts);
        Map<String, String> postUrlMap = new HashMap<>();

        for (RssItemResource itemResource : subscribeResource.itemResources()) {
            PostEntity post;

            if (postMap.containsKey(itemResource.guid())) {
                post = postMap.get(itemResource.guid());
                post.updateBy(itemResource);
            } else {
                post = PostEntity.from(itemResource, subscribe);
                postUrlMap.put(post.getGuid(), post.getTitle());
            }
            postEntityJpaRepository.save(post);
        }
        return postUrlMap;
    }

    private static Map<String, PostEntity> convertListToHashSet
            (List<PostEntity> postEntities) {
        Map<String, PostEntity> map = new HashMap<>();

        for (PostEntity postEntity : postEntities) {
            map.put(postEntity.getGuid(), postEntity);
        }

        return map;
    }
}

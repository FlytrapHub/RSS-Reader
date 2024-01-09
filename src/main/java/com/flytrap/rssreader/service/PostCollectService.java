package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.post.q.PostBulkInsertPublisher;
import com.flytrap.rssreader.infrastructure.api.RssPostParser;
import com.flytrap.rssreader.infrastructure.api.dto.RssSubscribeResource;
import com.flytrap.rssreader.infrastructure.api.dto.RssSubscribeResource.RssItemResource;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.SubscribeEntityJpaRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class PostCollectService {

    //  private static final int TEN_MINUTE = 600_000;
    private final RssPostParser postParser;
    private final SubscribeEntityJpaRepository subscribeEntityJpaRepository;
    private final PostEntityJpaRepository postEntityJpaRepository;
    private final PostBulkInsertPublisher publisher;

    @Async("asyncTaskExecutor")
    public CompletableFuture<Map<String, String>> processPostCollectionAsync(
            SubscribeEntity subscribe) {
        log.info(" task1 with " + Thread.currentThread().getName() + " at time: "
                + LocalDateTime.now());
        return CompletableFuture.supplyAsync(
                () -> postParser.parseRssDocuments(subscribe.getUrl())
                        .map(resource -> {
                            updateSubscribeTitle(resource, subscribe);
                            return savePosts(resource, subscribe);
                        }).orElse(new HashMap<>()));
    }

    private void updateSubscribeTitle(RssSubscribeResource subscribeResource,
            SubscribeEntity subscribe) {
        subscribe.updateTitle(subscribeResource.subscribeTitle());
        subscribeEntityJpaRepository.save(subscribe);
    }

    //TODO: 글이 새로 추가되면 슬랙에 보낼URL을 기억한다.
    //TODO: RSS에서 50개를 긁어올 때 findAll 하지말고 APE랑 얘기를 해본다.
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
            publisher.publish(post);
//            log.info("bulkInsertQueue.size() = {}", bulkInsertQueue.size());
            //     postEntityJpaRepository.save(post);
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

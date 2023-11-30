package com.flytrap.rssreader.service;

import com.flytrap.rssreader.infrastructure.api.RssPostParser;
import com.flytrap.rssreader.infrastructure.api.dto.RssSubscribeResource;
import com.flytrap.rssreader.infrastructure.api.dto.RssSubscribeResource.RssItemResource;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.SubscribeEntityJpaRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final TaskExecutor taskExecutor;

    @Scheduled(fixedDelay = TEN_MINUTE)
    public void collectPosts() {
        List<SubscribeEntity> subscribes = subscribeEntityJpaRepository.findAll();

        for (SubscribeEntity subscribe : subscribes) {
            processPostCollectionAsync(subscribe);
        }
    }

    /**
     * 구독한 블로그의 RSS에서 게시글들을 읽어서 DB에 저장한다.<br>
     * 비동기 처리되어 있으며 블로그 하나당 하나의 스레드에서 동작한다.
     * @param subscribe 구독한 블로그
     */
    private void processPostCollectionAsync(SubscribeEntity subscribe) {
        taskExecutor.execute(() -> {
            postParser.parseRssDocuments(subscribe.getUrl())
                .ifPresent(resource -> {
                    updateSubscribeTitle(resource, subscribe);
                    savePosts(resource, subscribe);
                });
        });
    }

    private void updateSubscribeTitle(RssSubscribeResource subscribeResource, SubscribeEntity subscribe) {
        subscribe.updateTitle(subscribeResource.subscribeTitle());
        subscribeEntityJpaRepository.save(subscribe);
    }

    private void savePosts(RssSubscribeResource subscribeResource, SubscribeEntity subscribe) {
        List<PostEntity> posts = postEntityJpaRepository.findAllBySubscribeOrderByPubDateDesc(subscribe);
        Map<String, PostEntity> postMap = convertListToHashSet(posts);

        for (RssItemResource itemResource : subscribeResource.itemResources()) {
            PostEntity post;

            if (postMap.containsKey(itemResource.guid())) {
                post = postMap.get(itemResource.guid());
                post.updateBy(itemResource);
            } else {
                post = PostEntity.from(itemResource, subscribe);
            }

            postEntityJpaRepository.save(post);
        }
    }

    private static Map<String, PostEntity> convertListToHashSet(List<PostEntity> postEntities) {
        Map<String, PostEntity> map = new HashMap<>();

        for (PostEntity postEntity : postEntities) {
            map.put(postEntity.getGuid(), postEntity);
        }

        return map;
    }

}

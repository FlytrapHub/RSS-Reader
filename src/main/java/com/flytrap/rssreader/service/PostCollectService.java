package com.flytrap.rssreader.service;

import com.flytrap.rssreader.infrastructure.api.RssPostParser;
import com.flytrap.rssreader.infrastructure.api.dto.RssItemResource;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.SubscribeEntityJpaRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class PostCollectService {

    private static final int TEN_MINUTE = 600_000;

    private final RssPostParser postParser;
    private final SubscribeEntityJpaRepository subscribeEntityJpaRepository;
    private final PostEntityJpaRepository postEntityJpaRepository;

    @Transactional
    @Scheduled(fixedDelay = TEN_MINUTE)
    public void collectPosts() {
        List<SubscribeEntity> subscribes = subscribeEntityJpaRepository.findAll();

        for (SubscribeEntity subscribe : subscribes) {
            List<RssItemResource> itemResources = postParser.parseRssDocuments(subscribe.getUrl());
            savePosts(itemResources, subscribe);
        }
    }

    private void savePosts(List<RssItemResource> itemResources, SubscribeEntity subscribe) {
        List<PostEntity> posts = postEntityJpaRepository.findAllBySubscribeOrderByPubDateDesc(subscribe);
        Map<String, PostEntity> postMap = convertListToHashSet(posts);

        for (RssItemResource itemResource : itemResources) {
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

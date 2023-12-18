package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.alert.SubscribeEvent;
import com.flytrap.rssreader.domain.alert.q.SubscribeEventPublisher;
import com.flytrap.rssreader.domain.post.q.PostBulkInsertQueue;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledService {

    private final PostCollectService postCollectService;
    private final SubscribeService subscribeService;
    private final SubscribeEventPublisher publisher;
    private final PostBulkInsertQueue bulkInsertQueue;
    private final PostEntityJpaRepository postEntityJpaRepository;
    //TODO post크롤링, 큐에inset, 큐에서 poll한다음 alert 발생을 각각의 스레드를 할당해 실행 하도록한다.

    //Post 수집 크롤링
    @Scheduled(fixedRate = 5000)
    public void collectPosts() {
        String threadName = Thread.currentThread().getName();
        log.info("[{}] Collecting posts...", threadName);

        List<SubscribeEntity> subscribes = subscribeService.findSubscribeList();

        for (SubscribeEntity subscribe : subscribes) {
            CompletableFuture<Map<String, String>> future = postCollectService.processPostCollectionAsync(
                    subscribe);

            if (!future.join().isEmpty()) {
                SubscribeEvent event = new SubscribeEvent(subscribe.getId(),
                        Collections.unmodifiableMap(future.join()));
                log.info("future.join.size = {}", future.join().size());
                publisher.publish(event);
            }
        }

        log.info("[{}] Post collection completed.", threadName);
    }

    //queue에 bulk inert 10개씩
    @Scheduled(fixedRate = 5000)
    public void queueBulkInsert() {
        String threadName = Thread.currentThread().getName();
        log.info("[{}] Inserting posts into the database...", threadName);

        while (bulkInsertQueue.isRemaining() && bulkInsertQueue.size() > 10) {
            List<PostEntity> posts = bulkInsertQueue.pollBatch(10);
            log.info("---------------------------------------------");
            log.info("bulkInsertQueue.size = {}", bulkInsertQueue.size());
            log.info("bulkInsertQueue.hashCode = {}", bulkInsertQueue.hashCode());
            postEntityJpaRepository.saveAll(posts);
        }
        log.info("[{}] Post insertion completed.", threadName);
    }
}

package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.alert.SubscribeEvent;
import com.flytrap.rssreader.domain.alert.q.SubscribeEventPublisher;
import com.flytrap.rssreader.domain.post.q.PostBulkInsertQueue;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import java.time.LocalDateTime;
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
    private final SubscribeEventPublisher publisher;
    private final PostBulkInsertQueue bulkInsertQueue;
    private final PostEntityJpaRepository postEntityJpaRepository;
    private final SubscribeService subscribeService;
    //TODO post크롤링, 큐에inset, 큐에서 poll한다음 alert 발생을 각각의 스레드를 할당해 실행 하도록한다.

    //Post 수집 크롤링
    //스케쥴폴에서 스케쥴 전용 스레드를 할당하고
    //파서를 이용해 구독당 post를 가져오는건 비동기 병렬 처리
    @Scheduled(fixedDelay = 3 * 1000)
    public void collectPosts() {
        log.info(" scheduling1 with " + Thread.currentThread().getName() + " at time: "
                + LocalDateTime.now());
        List<SubscribeEntity> subscribes = subscribeService.findSubscribeList();

        for (SubscribeEntity subscribe : subscribes) {
            CompletableFuture<Map<String, String>> future = postCollectService.processPostCollectionAsync(
                    subscribe);

            if (!future.join().isEmpty()) {
                SubscribeEvent event = new SubscribeEvent(subscribe.getId(),
                        Collections.unmodifiableMap(future.join()));
                publisher.publish(event);
            }
        }
    }

    //queue에 bulk inert 10개씩
    //TODO: 수정해야함
    @Scheduled(fixedRate = 10000)
    public void queueBulkInsert() {
        log.info(" scheduling2 with" + Thread.currentThread().getName() + " at time: "
                + LocalDateTime.now());
        if (bulkInsertQueue.isRemaining() && bulkInsertQueue.size() > 10) {
            List<PostEntity> posts = bulkInsertQueue.pollBatch(30);
            postEntityJpaRepository.saveAll(posts);
        }
    }
}

package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.alert.SubscribeEvent;
import com.flytrap.rssreader.domain.alert.q.SubscribeEventPublisher;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final SubscribeEventPublisher subscribeEventPublisher;
    private final SubscribeService subscribeService;

    //TODO post크롤링, 큐에inset, 큐에서 poll한다음 alert 발생을 각각의 스레드를 할당해 실행 하도록한다.

    //Post 수집 크롤링
    //스케쥴폴에서 스케쥴 전용 스레드를 할당하고
    //파서를 이용해 구독당 post를 가져오는건 비동기 병렬 처리
    @Scheduled(fixedDelay = 5 * 1000)
    public void collectPosts() {
        long startTime = System.currentTimeMillis();
        log.info(" scheduling with " + Thread.currentThread().getName() + " at time: "
                + LocalDateTime.now());
        List<SubscribeEntity> subscribes = subscribeService.findSubscribeList();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (SubscribeEntity subscribe : subscribes) {
            //Post를 수집 크롤링하면서 SubscribeEvent 발생시킨다.
            CompletableFuture<Map<String, String>> future = postCollectService.processPostCollectionAsync(
                    subscribe);
            if (future != null) {
                futures.add(future.thenAcceptAsync(resultMap -> {
                    log.info(" scheduling thenAcceptAsync with " + Thread.currentThread().getName()
                            + " at time: "
                            + LocalDateTime.now());
                    if (!resultMap.isEmpty()) {
                        SubscribeEvent event = new SubscribeEvent(subscribe.getId(),
                                Collections.unmodifiableMap(resultMap));
                        subscribeEventPublisher.publish(event);
                    }
                }));
            }
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));
        allOf.join(); // 모든 task가 완료될때까지 기다린다. 즉 다음 스케줄러까지 기다린다.

        long executionTime = System.currentTimeMillis() - startTime;

        log.info("[PostFacadeService.createPost] Execution time (ms): {}", executionTime);
        log.info("Thread: Name={}, ID={}", Thread.currentThread().getName(),
                Thread.currentThread().getId());
    }
}

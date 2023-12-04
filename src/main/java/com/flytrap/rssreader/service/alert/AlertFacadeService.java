package com.flytrap.rssreader.service.alert;

import com.flytrap.rssreader.domain.alert.q.SubscribeEventQueue;
import com.flytrap.rssreader.domain.subscribe.Subscribe;
import com.flytrap.rssreader.infrastructure.entity.alert.AlertEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertFacadeService {

    private final SubscribeEventQueue queue;
    private final AlertService alertService;

    @Async("taskScheduler")
    @Scheduled(fixedRate = 1000)
    public void alert() {
        //TODO: 먼저 큐에있는 Susbscirbe를 확인한다.
        //TODO:큐를 확인해 큐의 Subscribe로 구독된 Folder를 찾는다. -> Alert를 찾는다 -> Member에게 알림을 고한다.
        //TODO: ex) 만약 0번 Member가 6번 Folder를 Subscribe 중이라면 이때 Slack(1)으로 알람 받기를 원하는 경우
        if (queue.isRemaining()) {
            Subscribe subscribe = queue.poll();
            log.info("alert 스케쥴러 실행 eventQueue poll 의 상태 = {}  ", subscribe.toString());
            List<AlertEntity> alertList = alertService.getAlertList(subscribe.getId());

            for (AlertEntity alertEntity : alertList) {
                log.info("alert 정보 = {} ", alertEntity);
                alertService.notifyAlert(subscribe.getUrl(), subscribe.getTitle());
            }
        }
    }
}

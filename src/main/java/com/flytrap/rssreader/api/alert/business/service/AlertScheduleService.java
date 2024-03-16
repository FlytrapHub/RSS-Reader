package com.flytrap.rssreader.api.alert.business.service;

import com.flytrap.rssreader.api.alert.business.event.subscribe.SubscribeEventQueue;
import com.flytrap.rssreader.api.folder.business.service.FolderReadService;
import com.flytrap.rssreader.domain.alert.Alert;
import com.flytrap.rssreader.domain.alert.SubscribeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertScheduleService {

    private final SubscribeEventQueue queue;
    private final AlertService alertService;
    private final FolderReadService folderReadService;

    @Async("alertThreadExecutor")
    @Scheduled(fixedRate = 1000L)
    public void processAlertSchedule() {
        if (queue.isRemaining()) {
            SubscribeEvent event = queue.poll();
            log.info("alert 스케쥴러 실행 eventQueue poll 의 상태 = {}  ", event.toString()); //새로운 게시글들

            List<Alert> alerts = alertService.getAlertListBySubscribe(event.subscribeId());
            if (!alerts.isEmpty()) {
                alerts.forEach(alert ->
                        alertService.publishAlertEvent(
                                folderReadService.findById(alert.getFolderId()).getName(),
                                alert.getWebhookUrl(),
                                event.posts()));
            }
        }
    }
}

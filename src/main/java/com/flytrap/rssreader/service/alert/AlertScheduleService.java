package com.flytrap.rssreader.service.alert;

import com.flytrap.rssreader.domain.alert.Alert;
import com.flytrap.rssreader.domain.alert.SubscribeEvent;
import com.flytrap.rssreader.domain.alert.q.SubscribeEventQueue;
import com.flytrap.rssreader.service.folder.FolderReadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

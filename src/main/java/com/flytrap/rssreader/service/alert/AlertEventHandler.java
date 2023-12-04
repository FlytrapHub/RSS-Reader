package com.flytrap.rssreader.service.alert;

import com.flytrap.rssreader.domain.alert.AlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertEventHandler {

    private final AlertService alertService;

    @Async
    @EventListener(AlertEvent.class)
    public void onEvent(AlertEvent event) {
        alertService.notifyPlatform(event.getValue());
    }
}


package com.flytrap.rssreader.domain.alert.q;

import com.flytrap.rssreader.domain.alert.SubscribeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Deprecated
public class SubscribeEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publish(SubscribeEvent subscribe) {
        log.info("Create new offer subscribe! {}", subscribe.toString());
        publisher.publishEvent(subscribe);
    }
}

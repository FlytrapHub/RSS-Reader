package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.post.PostOpenEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostOpenEventHandler {

    private final PostOpenService postOpenService;

    @EventListener(PostOpenEvent.class)
    public void handle(PostOpenEvent event) {
        log.info("PostOpenEvent: {}", event);
        postOpenService.open(event.getValue());
    }
}

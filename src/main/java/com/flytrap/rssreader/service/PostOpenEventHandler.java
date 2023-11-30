package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.post.PostOpenEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostOpenEventHandler {

    private final PostOpenService postOpenService;

    @Async
    @TransactionalEventListener(PostOpenEvent.class)
    public void handle(PostOpenEvent event) {
        postOpenService.open(event.getValue());
    }
}

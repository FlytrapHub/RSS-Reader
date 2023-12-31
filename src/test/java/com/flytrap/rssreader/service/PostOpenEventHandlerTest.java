package com.flytrap.rssreader.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.flytrap.rssreader.domain.post.PostOpenEvent;
import com.flytrap.rssreader.service.dto.PostOpenParam;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostOpenEventHandler 단위 테스트")
class PostOpenEventHandlerTest {

    @Spy
    ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    PostOpenEventHandler postOpenEventHandler;

    @Mock
    PostOpenService postOpenService;

    @Test
    @DisplayName("handle 메서드는 PostOpenEvent를 처리한다.")
    void handle_shouldHandlePostOpenEvent() {
        // given
        PostOpenEvent event = new PostOpenEvent(new PostOpenParam(1L, 0L));

        // when
        postOpenEventHandler.handle(event);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            verify(postOpenService, times(1)).open(event.getValue());
        });
    }

}

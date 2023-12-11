package com.flytrap.rssreader.global.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
@DisplayName("PublishEventAspect 단위 테스트")
class PublishEventAspectTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private PublishEventAspect publishEventAspect;

    @Test
    @DisplayName("PublishEvent 어노테이션이 붙은 메서드가 정상적으로 실행되었을 때 이벤트가 발행되는지 확인")
    void publishEvent_success() throws InvocationTargetException, IllegalAccessException {
        // Given
        PublishEventMock publishEventMock = new PublishEventMock();
        TestEvent testEvent = new TestEvent("TestParam");

        // When
        publishEventAspect.publishEventPointcut(publishEventMock);

        // Then
        verify(applicationEventPublisher).publishEvent(any(TestEvent.class));
    }

    // PublishEvent annotation mock 클래스
    private static class PublishEventMock implements PublishEvent {

        @Override
        public Class<? extends EventHolder> eventType() {
            return TestEvent.class;
        }

        public String params() {
            return "#{'TestParam'}";
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return PublishEvent.class;
        }


    }

    // event type mock 클래스
    private static class TestEvent implements EventHolder<String> {

        String value;

        @Override
        public String value() {
            return value;
        }

        public TestEvent(String value) {
            this.value = value;
        }
    }
}

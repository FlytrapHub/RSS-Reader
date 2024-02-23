package com.flytrap.rssreader.service.alert;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.domain.alert.AlertPlatform;
import com.flytrap.rssreader.domain.alert.SubscribeEvent;
import com.flytrap.rssreader.domain.alert.q.SubscribeEventQueue;
import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.infrastructure.entity.alert.AlertEntity;
import com.flytrap.rssreader.service.folder.FolderReadService;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("AlertScheduleService  로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class AlertScheduleServiceTest {

    @Mock
    private SubscribeEventQueue queue;

    @Mock
    private AlertService alertService;

    @Mock
    private FolderReadService folderReadService;

    @InjectMocks
    private AlertScheduleService alertScheduleService;


    @Test
    @DisplayName("새로운 구독 이벤트가 발행되면 알람 이벤트를 발행할 수 있다.")
    void alert_success() {
        // Given
        List<AlertEntity> alertList = List.of(
            AlertEntity.create(0L, 1L, AlertPlatform.DISCORD, "WEBHOOK_URL")
        );

        SubscribeEvent subscribeEvent = new SubscribeEvent(
            1L,
            Map.of(
                "newPostUrl1", "newPostTitle1",
                "newPostUrl2", "newPostTitle2",
                "newPostUrl3", "newPostTitle3"
            )
        );

        Folder folder = Folder.create("folder1", 1L);

        when(queue.isRemaining()).thenReturn(true);
        when(queue.poll()).thenReturn(subscribeEvent);
        when(alertService.getAlertList(anyLong())).thenReturn(alertList);
        when(folderReadService.findById(anyLong())).thenReturn(folder);

        // When
        alertScheduleService.processAlertSchedule();

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            verify(alertService, times(alertList.size()))
                .publishAlertEvent(anyString(), anyString(), anyMap());
        });
    }

    @Test
    @DisplayName("새로운 구독 이벤트가 없으면 알림 이벤트로 발행되지 않는다.")
    void noSubscribeEventInQueue() {
        // Given
        when(queue.isRemaining()).thenReturn(false);

        // When
        alertScheduleService.processAlertSchedule();

        // Then
        verify(alertService, never())
            .publishAlertEvent(anyString(), anyString(), anyMap());
    }
}

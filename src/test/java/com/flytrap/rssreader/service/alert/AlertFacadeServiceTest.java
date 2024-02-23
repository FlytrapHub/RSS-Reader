package com.flytrap.rssreader.service.alert;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.domain.alert.SubscribeEvent;
import com.flytrap.rssreader.domain.alert.q.SubscribeEventQueue;
import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.infrastructure.entity.alert.AlertEntity;
import com.flytrap.rssreader.service.alert.AlertScheduleService;
import com.flytrap.rssreader.service.alert.AlertService;
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

@DisplayName("AlertFacadeService  로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class AlertFacadeServiceTest {

    @Mock
    private SubscribeEventQueue queue;

    @Mock
    private AlertService alertService;

    @Mock
    private FolderReadService folderReadService;

    @InjectMocks
    private AlertScheduleService alertFacadeService;


    @Test
    @DisplayName("이벤트큐에 1L구독에  해당하는 3개의 글이 새로 save됐다")
    void alert_success() {
        // Given
        List<AlertEntity> alertList = List.of(
                AlertEntity.create(0L, 1L, 1)
        );

        SubscribeEvent subscribeEvent = new SubscribeEvent(
                1L,
                Map.of(
                        "test_url1", "test_title 1",
                        "test_url2", "test_title 2",
                        "test_url3", "test_title 3"
                )
        );

        Folder folder = Folder.create("folder1", 0L);

        when(queue.isRemaining()).thenReturn(true);
        when(queue.poll()).thenReturn(subscribeEvent);
        when(alertService.getAlertList(anyLong())).thenReturn(alertList);
        when(folderReadService.findById(anyLong())).thenReturn(folder);

        // When
        alertFacadeService.alert();

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            verify(alertService, times(alertList.size())).notifyAlert(any(), anyString());
        });
    }

    @Test
    @DisplayName("이벤트큐에 아무 구독도 없을경우")
    void noSubscribeEventInQueue() {
        // Given
        when(queue.isRemaining()).thenReturn(false);

        // When
        alertFacadeService.alert();

        // Then
        verify(alertService, never()).notifyAlert(any(), anyString());
    }
}

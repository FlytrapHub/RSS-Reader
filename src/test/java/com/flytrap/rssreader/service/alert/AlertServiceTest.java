package com.flytrap.rssreader.service.alert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.domain.alert.AlertPlatform;
import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.fixture.FixtureFactory;
import com.flytrap.rssreader.fixture.FolderFixtureFactory;
import com.flytrap.rssreader.infrastructure.entity.alert.AlertEntity;
import com.flytrap.rssreader.infrastructure.repository.AlertEntityJpaRepository;
import com.flytrap.rssreader.service.alert.AlertService;
import com.flytrap.rssreader.service.dto.AlertParam;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Alert 서비스 로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    AlertEntityJpaRepository alertRepository;

    @Mock
    SlackAlarmService slackAlarmService;

    @InjectMocks
    AlertService alertService;

    Member member = FixtureFactory.generateMember();

    Folder folder = FolderFixtureFactory.generateFolder();

    AlertPlatform slack = AlertPlatform.SLACK;

    @Test
    @DisplayName("특정 플랫폼 알람 받기 ON")
    void on() {

        // when
        when(alertRepository.save(Mockito.any(AlertEntity.class)))
                .thenAnswer(invocation -> {
                    AlertEntity savedEntity = invocation.getArgument(
                            0); // Get the passed AlertEntity
                    Integer serviceId = savedEntity.getServiceId();
                    AlertPlatform platform = AlertPlatform.ofCode(serviceId);
                    assertEquals(platform.getValue(), slack.getValue());
                    return savedEntity;
                });

        alertService.on(member.getId(), folder.getId(), slack.getValue());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            verify(alertRepository, times(1)).save(any());
        });
    }

    @Test
    @DisplayName("특정 플랫폼 알람 받기 OFF")
    void off() {
        AlertEntity entity = AlertEntity.builder()
                .memberId(member.getId())
                .folderId(folder.getId())
                .serviceId(slack.getValue())
                .build();

        when(alertRepository.findByFolderIdAndMemberId(folder.getId(), member.getId()))
                .thenReturn(Optional.of(entity));

        // when
        alertService.off(folder.getId(), member.getId());
        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            verify(alertRepository, times(1)).delete(any());
        });
    }

    @Test
    @DisplayName("특정 플랫폼 알람 받기")
    void testNotifyPlatform() {

        // then
        AlertParam alertParam = new AlertParam(Map.of(
                "test_url1", "test_title 1",
                "test_url2", "test_title 2",
                "test_url3", "test_title 3"
        ), "folderName");

        // when
        alertService.notifyPlatform(alertParam);

        // then
        verify(slackAlarmService).notifyReturn(alertParam);
    }
}

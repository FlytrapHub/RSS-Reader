package com.flytrap.rssreader.api.alert.business.service;

import com.flytrap.rssreader.domain.alert.AlertEvent;
import com.flytrap.rssreader.global.event.PublishEvent;
import com.flytrap.rssreader.infrastructure.entity.alert.AlertEntity;
import com.flytrap.rssreader.infrastructure.repository.AlertEntityJpaRepository;
import com.flytrap.rssreader.service.alert.platform.SlackAlarmService;
import com.flytrap.rssreader.service.dto.AlertParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertEntityJpaRepository alertRepository;
    private final SlackAlarmService slackAlarmService;

    public Long on(Long folderId, Long memberId, Integer platformNum) {
        return alertRepository.save(AlertEntity.create(memberId, folderId, platformNum))
                .getId();
    }

    public void off(Long folderId, Long memberId) {
        AlertEntity alert = findByAlert(folderId, memberId);
        alertRepository.delete(alert);
    }

    private AlertEntity findByAlert(Long folderId, Long memberId) {
        return alertRepository.findByFolderIdAndMemberId(folderId, memberId).orElseThrow();
    }

    public void notifyPlatform(AlertParam value) {
        slackAlarmService.notifyReturn(value);
    }

    //TODO: 굳이 사실 지금은 필요없어 보이기는합니다.
    @PublishEvent(eventType = AlertEvent.class,
            params = "#{T(com.flytrap.rssreader.service.dto.AlertParam).create(#posts,#name)}")
    public void notifyAlert(Map<String, String> posts, String name) {
        log.info("notifyAlert 실행 플랫폼 알람 publish 실행");
    }

    public List<AlertEntity> getAlertList(Long serviceId) {
        return alertRepository.findAlertsBySubscribeId(serviceId);
    }

}

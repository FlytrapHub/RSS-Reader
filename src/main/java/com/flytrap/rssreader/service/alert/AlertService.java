package com.flytrap.rssreader.service.alert;

import com.flytrap.rssreader.domain.alert.Alert;
import com.flytrap.rssreader.domain.alert.AlertEvent;
import com.flytrap.rssreader.domain.alert.AlertPlatform;
import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.global.event.PublishEvent;
import com.flytrap.rssreader.global.exception.NoSuchDomainException;
import com.flytrap.rssreader.infrastructure.repository.AlertPlatformEntityJpaRepository;
import com.flytrap.rssreader.service.alert.platform.SlackAlarmService;
import com.flytrap.rssreader.infrastructure.entity.alert.AlertEntity;
import com.flytrap.rssreader.infrastructure.repository.AlertEntityJpaRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertEntityJpaRepository alertRepository;
    private final AlertPlatformEntityJpaRepository alertPlatformRepository;
    private final SlackAlarmService slackAlarmService;

    public Alert registerAlert(Long folderId, Long memberId, String webhookUrl) {
        AlertPlatform alertPlatform = AlertPlatform.parseWebhookUrl(webhookUrl);

        return alertRepository.save(
                AlertEntity.create(memberId, folderId, alertPlatform, webhookUrl))
            .toDomain();
    }

    public void removeAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new NoSuchDomainException(Alert.class))
                .toDomain();

        alertRepository.deleteById(alert.getId());
    }

    //TODO: 굳이 사실 지금은 필요없어 보이기는합니다.
    @PublishEvent(eventType = AlertEvent.class,
        params = "#{T(com.flytrap.rssreader.service.alert.dto.AlertParam).create(#folder, #webhookUrl, #posts)}")
    public void notifyAlert(Folder folder, String webhookUrl, Map<String, String> posts) {
        // log.info("notifyAlert 실행 플랫폼 알람 publish 실행");
    }

    public List<AlertEntity> getAlertList(Long serviceId) {
        return alertRepository.findAlertsBySubscribeId(serviceId);
    }

}

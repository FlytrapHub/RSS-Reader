package com.flytrap.rssreader.service.alert;

import com.flytrap.rssreader.domain.alert.Alert;
import com.flytrap.rssreader.domain.alert.AlertEvent;
import com.flytrap.rssreader.domain.alert.AlertPlatform;
import com.flytrap.rssreader.global.event.PublishEvent;
import com.flytrap.rssreader.global.exception.NoSuchDomainException;
import com.flytrap.rssreader.service.alert.platform.AlarmService;
import com.flytrap.rssreader.infrastructure.entity.alert.AlertEntity;
import com.flytrap.rssreader.infrastructure.repository.AlertEntityJpaRepository;
import com.flytrap.rssreader.service.dto.AlertParam;
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
    private final List<AlarmService> alarmServices;

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

    @PublishEvent(eventType = AlertEvent.class,
        params = "#{T(com.flytrap.rssreader.service.dto.AlertParam).create(#folderName, #webhookUrl, #posts)}")
    public void notifyAlert(String folderName, String webhookUrl, Map<String, String> posts) {}

    public void notifyPlatform(AlertParam value) {
        AlertPlatform alertPlatform = AlertPlatform.parseWebhookUrl(value.webhookUrl());

        for (AlarmService alarmService : alarmServices) {
            if (alarmService.isSupport(alertPlatform)) {
                alarmService.notifyReturn(value);
            }
        }
    }

    public List<AlertEntity> getAlertList(Long serviceId) {
        return alertRepository.findAlertsBySubscribeId(serviceId);
    }

}

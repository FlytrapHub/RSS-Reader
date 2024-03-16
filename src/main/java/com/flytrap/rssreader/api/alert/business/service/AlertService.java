package com.flytrap.rssreader.api.alert.business.service;

import com.flytrap.rssreader.domain.alert.Alert;
import com.flytrap.rssreader.domain.alert.AlertEvent;
import com.flytrap.rssreader.domain.alert.AlertPlatform;
import com.flytrap.rssreader.global.event.PublishEvent;
import com.flytrap.rssreader.global.exception.NoSuchDomainException;
import com.flytrap.rssreader.infrastructure.api.alert.AlertSender;
import com.flytrap.rssreader.infrastructure.entity.alert.AlertEntity;
import com.flytrap.rssreader.infrastructure.repository.AlertEntityJpaRepository;
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
    private final List<AlertSender> alertSenders;

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
    public void publishAlertEvent(String folderName, String webhookUrl, Map<String, String> posts) {}

    public void sendAlertToPlatform(AlertParam value) {
        AlertPlatform alertPlatform = AlertPlatform.parseWebhookUrl(value.webhookUrl());

        for (AlertSender alertSender : alertSenders) {
            if (alertSender.isSupport(alertPlatform)) {
                alertSender.sendAlert(value);
            }
        }
    }

    public List<Alert> getAlertListBySubscribe(Long subscribeId) {
        return alertRepository.findAlertsBySubscribeId(subscribeId)
                .stream()
                .map(AlertEntity::toDomain)
                .toList();
    }

    public List<Alert> getAlertListByFolder(Long folderId) {
        return alertRepository.findAllByFolderId(folderId)
                .stream()
                .map(AlertEntity::toDomain)
                .toList();
    }

}


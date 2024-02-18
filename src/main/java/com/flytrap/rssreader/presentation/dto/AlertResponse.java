package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.alert.Alert;

public record AlertResponse(
    Long id,
    String platform,
    String webhookUrl
) {

    public static AlertResponse from(Alert alert) {
        return new AlertResponse(
            alert.getId(),
            alert.getAlertPlatform().getPlatform(),
            alert.getWebhookUrl());
    }
}

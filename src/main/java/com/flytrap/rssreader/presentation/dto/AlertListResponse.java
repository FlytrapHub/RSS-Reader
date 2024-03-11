package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.alert.Alert;
import java.util.List;

public record AlertListResponse(
    List<AlertResponse> alerts
) {
    public static AlertListResponse from(List<Alert> alerts) {
        return new AlertListResponse(
            alerts.stream()
            .map(AlertResponse::from)
            .toList()
        );
    }
}

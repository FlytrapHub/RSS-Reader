package com.flytrap.rssreader.infrastructure.api.alert;

import com.flytrap.rssreader.domain.alert.AlertPlatform;
import com.flytrap.rssreader.service.dto.AlertParam;

public interface AlertSender {

    boolean isSupport(AlertPlatform alertPlatform);
    void sendAlert(AlertParam value);
}

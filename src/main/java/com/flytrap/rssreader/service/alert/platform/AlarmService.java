package com.flytrap.rssreader.service.alert.platform;

import com.flytrap.rssreader.domain.alert.AlertPlatform;
import com.flytrap.rssreader.service.dto.AlertParam;

public interface AlarmService {

    boolean isSupport(AlertPlatform alertPlatform);
    void sendAlert(AlertParam value);
}

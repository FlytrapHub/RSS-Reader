package com.flytrap.rssreader.api.alert.business.service.platform;

import com.flytrap.rssreader.service.dto.AlertParam;

public interface AlarmService {

    void notifyReturn(AlertParam value);
}

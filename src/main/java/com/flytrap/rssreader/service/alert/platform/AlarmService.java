package com.flytrap.rssreader.service.alert.platform;

import com.flytrap.rssreader.service.dto.AlertParam;

public interface AlarmService {

    void notifyReturn(AlertParam value);
}

package com.flytrap.rssreader.domain.alert;

import com.flytrap.rssreader.global.event.EventHolder;
import com.flytrap.rssreader.service.dto.AlertParam;
import lombok.Getter;

public record AlertEvent(@Getter AlertParam value) implements EventHolder<AlertParam> {

}


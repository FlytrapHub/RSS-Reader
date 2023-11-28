package com.flytrap.rssreader.infrastructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RssSubscribeTagName {
    CHANNEL("channel"), TITLE("title");

    private final String tagName;
}

package com.flytrap.rssreader.infrastructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RssItemTagName {
    GUID("guid"), TITLE("title"), DESCRIPTION("description"), PUB_DATE("pubDate");

    private final String tagName;
}

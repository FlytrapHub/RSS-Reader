package com.flytrap.rssreader.infrastructure.api.parser.dto;

import com.flytrap.rssreader.domain.subscribe.BlogPlatform;

public record RssSubscribeData(
    String title,
    String url,
    BlogPlatform platform,
    String description
) {

}

package com.flytrap.rssreader.api.parser.dto;

import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;

public record RssSubscribeData(
    String title,
    String url,
    BlogPlatform platform,
    String description
) {

}

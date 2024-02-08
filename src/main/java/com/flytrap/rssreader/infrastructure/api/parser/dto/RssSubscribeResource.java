package com.flytrap.rssreader.infrastructure.api.parser.dto;

import java.time.Instant;
import java.util.List;

public record RssSubscribeResource(
    String subscribeTitle,
    List<RssItemResource> itemResources
) {

    public record RssItemResource(
        String guid,
        String title,
        String description,
        Instant pubDate,
        String thumbnailUrl
    ) {

    }

}

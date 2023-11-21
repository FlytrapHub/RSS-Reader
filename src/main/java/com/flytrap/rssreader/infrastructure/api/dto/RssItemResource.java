package com.flytrap.rssreader.infrastructure.api.dto;

import java.time.Instant;

public record RssItemResource(
    String guid,
    String title,
    String description,
    Instant pubDate
) {

}

package com.flytrap.rssreader.infrastructure.api.dto;

public record RssItemResource(
    String guid,
    String title,
    String description,
    String pubDate
) {

}

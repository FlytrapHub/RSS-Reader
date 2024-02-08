package com.flytrap.rssreader.infrastructure.api.parser.dto;

import com.flytrap.rssreader.domain.subscribe.BlogPlatform;

public record RssFeedData(String title, String url, BlogPlatform platform, String description) {

}

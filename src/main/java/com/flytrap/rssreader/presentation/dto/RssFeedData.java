package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.subscribe.BlogPlatform;

public record RssFeedData(String title, String url, BlogPlatform platform) {

}

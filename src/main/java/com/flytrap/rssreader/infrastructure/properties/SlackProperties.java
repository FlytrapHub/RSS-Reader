package com.flytrap.rssreader.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
public record SlackProperties (String webHookUrl) {

}


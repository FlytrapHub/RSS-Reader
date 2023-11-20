package com.flytrap.rssreader.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "admin")
public record AuthProperties(String sessionId) {

}

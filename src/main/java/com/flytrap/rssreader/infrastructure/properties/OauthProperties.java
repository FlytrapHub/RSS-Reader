package com.flytrap.rssreader.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth")
public record OauthProperties(Github github) {

    public record Github(String clientId, String clientSecret, String userResourceUri
        ,String accessTokenUri, String redirectUri) {
    }
}

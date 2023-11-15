package com.flytrap.rssreader.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessToken(@JsonProperty("access_token") String accessToken,
                          @JsonProperty("token_type") String tokenType) {

    public String getHeadValue() {
        return String.format("%s %s", accessToken, tokenType);
    }
}

package com.flytrap.rssreader.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.infrastructure.entity.member.OauthServer;

public record UserResource(Long id,
                           String email,
                           String login,
                           @JsonProperty("avatar_url") String avatarUrl) {

    public Member toDomainForCreate() {
        return Member.of(null, login, email, avatarUrl, id, OauthServer.GITHUB, null);
    }

}

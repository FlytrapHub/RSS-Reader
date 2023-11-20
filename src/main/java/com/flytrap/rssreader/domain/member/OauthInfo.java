package com.flytrap.rssreader.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class OauthInfo {

    private Long oauthPk;
    private OauthServer oauthServer;
}

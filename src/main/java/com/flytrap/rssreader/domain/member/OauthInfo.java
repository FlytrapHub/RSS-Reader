package com.flytrap.rssreader.domain.member;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class OauthInfo implements Serializable {

    private Long oauthPk;
    private OauthServer oauthServer;
}

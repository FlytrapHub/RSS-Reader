package com.flytrap.rssreader.domain.member;

import com.flytrap.rssreader.infrastructure.entity.member.OauthServer;
import java.io.Serializable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String profile;
    private OauthInfo oauthInfo;
    private Instant createdAt;

    @Builder
    private Member(Long id, String name, String email, String profile, long oauthPk, OauthServer oauthServer,
        Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.oauthInfo = new OauthInfo(oauthPk, oauthServer);
        this.createdAt = createdAt;
    }

    public static Member of(Long id, String name, String email, String profile, Long oauthPk, OauthServer oauthServer, Instant createdAt) {
        return Member.builder()
            .id(id)
            .name(name)
            .email(email)
            .profile(profile)
            .oauthPk(oauthPk)
            .oauthServer(oauthServer)
            .createdAt(createdAt)
            .build();
    }

    public Long getOauthPk() {
        return this.oauthInfo.getOauthPk();
    }

    public OauthServer getOauthServer() {
        return this.oauthInfo.getOauthServer();
    }
}

@Getter
@AllArgsConstructor
class OauthInfo implements Serializable {
    private Long oauthPk;
    private OauthServer oauthServer;
}

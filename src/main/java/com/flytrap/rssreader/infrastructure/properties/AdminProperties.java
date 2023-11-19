package com.flytrap.rssreader.infrastructure.properties;

import com.flytrap.rssreader.domain.member.Member;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "admin")
public record AdminProperties(String code,
                              Member user) {

    public Member getMember() {
        return Member.adminOf(user.getId(), user.getName(), user.getEmail());
    }

    public static record User(int id, String name, String email) {
    }
}

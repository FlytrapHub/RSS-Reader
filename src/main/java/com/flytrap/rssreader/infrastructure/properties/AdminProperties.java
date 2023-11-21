package com.flytrap.rssreader.infrastructure.properties;

import com.flytrap.rssreader.domain.member.Member;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "admin")
public record AdminProperties(String code,
                              int memberId,
                              String memberName,
                              String memberEmail) {

    public Member getMember() {
        return Member.adminOf(memberId, memberName, memberEmail);
    }
}

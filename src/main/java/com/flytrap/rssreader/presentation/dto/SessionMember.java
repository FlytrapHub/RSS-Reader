package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.member.Member;
import java.io.Serializable;

/**
 * 로그인 후 Session에 저장되는 Member DTO입니다.
 * @param id
 */
public record SessionMember(long id) implements Serializable {

    public static SessionMember from(Member member) {
        return new SessionMember(member.getId());
    }
}

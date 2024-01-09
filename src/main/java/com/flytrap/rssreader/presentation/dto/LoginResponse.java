package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.member.Member;

public record LoginResponse(
    long id,
    String name,
    String profile
) {

    public static LoginResponse from(Member member) {
        return new LoginResponse(member.getId(), member.getName(), member.getProfile());
    }
}

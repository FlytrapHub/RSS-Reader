package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.member.Member;
import java.util.List;

public record MemberSummary(Long id,
                            String name,
                            String profile) {

    public static MemberSummary from(Member member) {
        return new MemberSummary(
                member.getId(),
                member.getName(),
                member.getProfile()
        );
    }

    public static List<MemberSummary> from(List<Member> invitedMembers) {
        return invitedMembers.stream()
                .map(MemberSummary::from)
                .toList();
    }
}

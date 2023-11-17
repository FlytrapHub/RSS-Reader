package com.flytrap.rssreader.domain.folder;

import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.global.model.DefaultDomain;
import com.flytrap.rssreader.global.model.Domain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Domain(name = "folder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder implements DefaultDomain {

    private Long id;
    private String name;
    private Long memberId;
    private Boolean isShared;

    @Builder
    public Folder(Long id, String name, Long memberId, Boolean isShared) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.isShared = isShared;
    }

    public static Folder of(Long id, String name, Long memberId, Boolean isShared) {
        return Folder.builder()
                .id(id)
                .name(name)
                .memberId(memberId)
                .isShared(isShared)
                .build();
    }

    public static Folder create(String name, long member) {
        return Folder.builder()
                .name(name)
                .memberId(member)
                .isShared(false)
                .build();
    }
}

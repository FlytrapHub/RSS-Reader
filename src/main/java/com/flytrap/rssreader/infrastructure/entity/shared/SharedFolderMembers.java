package com.flytrap.rssreader.infrastructure.entity.shared;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "folder_member")
public class SharedFolderMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long folderId;
    private Long memberId;

    @Builder
    protected SharedFolderMembers(Long id, Long folderId, Long memberId) {
        this.id = id;
        this.folderId = folderId;
        this.memberId = memberId;
    }

    public static SharedFolderMembers of(Long id, long inviteeId) {
        return SharedFolderMembers.builder()
                .folderId(id)
                .memberId(inviteeId)
                .build();
    }
}

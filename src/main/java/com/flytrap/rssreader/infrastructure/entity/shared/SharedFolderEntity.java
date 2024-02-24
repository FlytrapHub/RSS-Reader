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

@Deprecated
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "folder_member")
public class SharedFolderEntity { // TODO: rename folder member entity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long folderId;
    private Long memberId;

    @Builder
    protected SharedFolderEntity(Long id, Long folderId, Long memberId) {
        this.id = id;
        this.folderId = folderId;
        this.memberId = memberId;
    }

    public static SharedFolderEntity of(Long id, long inviteeId) {
        return SharedFolderEntity.builder()
                .folderId(id)
                .memberId(inviteeId)
                .build();
    }
}

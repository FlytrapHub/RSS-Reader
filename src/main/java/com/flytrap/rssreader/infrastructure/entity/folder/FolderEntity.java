package com.flytrap.rssreader.infrastructure.entity.folder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "folder")
public class FolderEntity {

    @Id
    private Long id;
    @Column(length = 255, nullable = false)
    private String name;
    @Column(nullable = false)
    private Long memberId;
    @Column(nullable = false)
    private Boolean isShared;

    @Builder
    public FolderEntity(Long id, String name, Long memberId, Boolean isShared) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.isShared = isShared;
    }

}

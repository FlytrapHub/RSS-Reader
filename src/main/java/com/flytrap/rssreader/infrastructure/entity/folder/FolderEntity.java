package com.flytrap.rssreader.infrastructure.entity.folder;

import com.flytrap.rssreader.domain.folder.Folder;
import jakarta.persistence.Column;
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
@Table(name = "folder")
public class FolderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, nullable = false)
    private String name;
    @Column(nullable = false)
    private Long memberId;
    @Column(nullable = false)
    private Boolean isShared;

    @Builder
    protected FolderEntity(Long id, String name, Long memberId, Boolean isShared) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.isShared = isShared;
    }

    public static FolderEntity from(Folder folder) {
        return FolderEntity.builder()
                .id(folder.getId())
                .name(folder.getName())
                .memberId(folder.getMemberId())
                .isShared(folder.getIsShared())
                .build();
    }

    public Folder toDomain() {
        return Folder.of(id, name, memberId, isShared);
    }

}

package com.flytrap.rssreader.infrastructure.entity.folder;

import com.flytrap.rssreader.domain.subscribe.Subscribe;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "folder_subscribe")
public class FolderSubscribeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folder_id", nullable = false)
    private Long folderId;

    @Column(name = "subscribe_id", nullable = false)
    private Long subscribeId;

    @Column(length = 2500, nullable = false)
    private String description;

    @Builder
    public FolderSubscribeEntity(Long id, Long folderId, Long subscribeId, String description) {
        this.id = id;
        this.folderId = folderId;
        this.subscribeId = subscribeId;
        this.description = description;
    }

    public static FolderSubscribeEntity from(Subscribe subscribe, Long folderId) {
        return FolderSubscribeEntity.builder()
            .subscribeId(subscribe.getId())
            .folderId(folderId)
            .description(subscribe.getDescription())
            .build();
    }
}

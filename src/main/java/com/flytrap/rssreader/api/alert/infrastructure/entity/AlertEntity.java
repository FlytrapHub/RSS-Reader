package com.flytrap.rssreader.api.alert.infrastructure.entity;

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
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@Table(name = "alert")
public class AlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "folder_id", nullable = false)
    private Long folderId;

    @Column(name = "service_id", nullable = false)
    private Integer serviceId;

    @Builder
    protected AlertEntity(Long id, Long memberId, Long folderId, Integer serviceId) {
        this.id = id;
        this.memberId = memberId;
        this.folderId = folderId;
        this.serviceId = serviceId;
    }

    public static com.flytrap.rssreader.infrastructure.entity.alert.AlertEntity create(Long memberId, Long folderId, Integer serviceId) {
        return com.flytrap.rssreader.infrastructure.entity.alert.AlertEntity.builder()
                .memberId(memberId)
                .folderId(folderId)
                .serviceId(serviceId)
                .build();
    }
}

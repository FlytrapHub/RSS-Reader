package com.flytrap.rssreader.infrastructure.entity.alert;

import com.flytrap.rssreader.domain.alert.Alert;
import com.flytrap.rssreader.domain.alert.AlertPlatform;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "platform_id", nullable = false)
    private AlertPlatformEntity alertPlatform;

    @Column(name = "webhook_url", length = 2500, nullable = false)
    private String webhookUrl;

    @Builder
    protected AlertEntity(Long id, Long memberId, Long folderId, AlertPlatformEntity alertPlatform,
        String webhookUrl) {
        this.id = id;
        this.memberId = memberId;
        this.folderId = folderId;
        this.alertPlatform = alertPlatform;
        this.webhookUrl = webhookUrl;
    }

    public static AlertEntity create(Long memberId, Long folderId, AlertPlatform alertPlatform,
        String webhookUrl) {
        return AlertEntity.builder()
            .memberId(memberId)
            .folderId(folderId)
            .alertPlatform(AlertPlatformEntity.create(alertPlatform))
            .webhookUrl(webhookUrl)
            .build();
    }

    public Alert toDomain() {
        return Alert.builder()
            .id(id)
            .memberId(memberId)
            .alertPlatform(alertPlatform.toDomain())
            .webhookUrl(webhookUrl)
            .build();
    }
}

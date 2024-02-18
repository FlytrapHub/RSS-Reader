package com.flytrap.rssreader.infrastructure.entity.alert;

import com.flytrap.rssreader.domain.alert.AlertPlatform;
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
@Table(name = "alert_platform")
public class AlertPlatformEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25, nullable = false)
    private String platform;

    @Column(length = 2500, nullable = false)
    private String signatureUrl;

    @Builder
    public AlertPlatformEntity(Long id, String platform, String signatureUrl) {
        this.id = id;
        this.platform = platform;
        this.signatureUrl = signatureUrl;
    }

    public static AlertPlatformEntity create(AlertPlatform alertPlatform) {
        return AlertPlatformEntity.builder()
            .id(alertPlatform.getId())
            .platform(alertPlatform.getPlatform())
            .signatureUrl(alertPlatform.getSignatureUrl())
            .build();
    }

    public AlertPlatform toDomain() {
        return AlertPlatform.builder()
            .id(id)
            .platform(platform)
            .signatureUrl(signatureUrl)
            .build();
    }
}

package com.flytrap.rssreader.domain.alert;

import com.flytrap.rssreader.global.model.DefaultDomain;
import com.flytrap.rssreader.global.model.Domain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Domain(name = "alertPlatform")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlertPlatform implements DefaultDomain {

    private Long id;
    private String platform;
    private String signatureUrl;

    @Builder
    protected AlertPlatform(Long id, String platform, String signatureUrl) {
        this.id = id;
        this.platform = platform;
        this.signatureUrl = signatureUrl;
    }

    public boolean verifyWebhookUrl(String webhookUrl) {
        if (webhookUrl == null || webhookUrl.isEmpty()) return false;

        return webhookUrl.contains(signatureUrl);
    }
}

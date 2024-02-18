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
@Domain(name = "alert")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alert  implements DefaultDomain {

    private Long id;
    private Long memberId;
    private AlertPlatform alertPlatform;
    private String webhookUrl;

    @Builder
    protected Alert(Long id, Long memberId, AlertPlatform alertPlatform, String webhookUrl) {
        this.id = id;
        this.memberId = memberId;
        this.alertPlatform = alertPlatform;
        this.webhookUrl = webhookUrl;
    }
}

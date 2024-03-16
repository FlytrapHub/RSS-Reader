package com.flytrap.rssreader.api.alert.infrastructure.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AlertPlatform {
    SLACK(1), DISCORD(2);

    private final int value;

    AlertPlatform(int value) {
        this.value = value;
    }

    public static AlertPlatform ofCode(Integer dbData) {
        return Arrays.stream(AlertPlatform.values())
                .filter(v -> v.value == (dbData))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 플렛폼입니다."));
    }


    public static AlertPlatform findAlertPlatform(AlertPlatform alertPlatform) {
        // 정규표현식을 사용하여 도메인 추출
        return Arrays.stream(AlertPlatform.values())
                .filter(alertPlatform::equals)
                .findFirst()
                .orElseThrow();
    }
}

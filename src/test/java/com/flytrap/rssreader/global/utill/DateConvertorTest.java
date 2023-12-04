package com.flytrap.rssreader.global.utill;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateConvertorTest {

    @Test
    @DisplayName("다양한 offset을 가진 pubDate를 Instant로 변환할 수 있다.")
    void convertToInstant() {
        String[] pubDates = {
            "Wed, 29 Nov 2023 13:49:14 +0900",
            "Tue, 07 Nov 2023 14:12:30 GMT",
            "Tue, 21 Nov 2023 07:30:08 +0000",
        };

        // 에러가 나지 않으면 Instant로 변환에 성공한 것
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThatCode(() -> {
            for (String pubDate : pubDates) {
                DateConvertor.convertToInstant(pubDate);
            }
        }).doesNotThrowAnyException();
        softAssertions.assertAll();
    }
}
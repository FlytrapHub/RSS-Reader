package com.flytrap.rssreader.domain.subscribe;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlogPlatform {
    VELOG("velog"), TISTORY("tistory"), GITPAGE("gitpage"), ETC("");

    private final String name;

    public static BlogPlatform parseLink(final String link) {
        // 정규표현식을 사용하여 도메인 추출
        return Arrays.stream(BlogPlatform.values())
                .filter(blogPlatform -> link.contains(blogPlatform.name))
                .findFirst()
                //TODO 일치하는 패턴이 없을 경우 예외 처리 또는 기본값 설정 , 요거 예외처리 해야함
                .orElseThrow(RuntimeException::new);
    }
}

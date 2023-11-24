package com.flytrap.rssreader.domain.subscribe;

import com.flytrap.rssreader.global.exception.ApplicationException;
import com.flytrap.rssreader.global.exception.NoSuchBlogPlatformException;
import java.io.NotSerializableException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlogPlatform {
    VELOG, TISTORY, GITPAGE, ETC;

    public static BlogPlatform parseLink(String link) {
        // 정규표현식을 사용하여 도메인 추출
        return Arrays.stream(BlogPlatform.values())
                .filter(blogPlatform -> link.toUpperCase().contains(blogPlatform.name()))
                .findFirst()
                //TODO 일치하는 패턴이 없을 경우 예외 처리 또는 기본값 설정 , 요거 예외처리 해야함
                .orElseThrow(() -> new NoSuchBlogPlatformException(link));
    }
}

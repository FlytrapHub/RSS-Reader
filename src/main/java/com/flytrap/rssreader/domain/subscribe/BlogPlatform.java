package com.flytrap.rssreader.domain.subscribe;

import com.flytrap.rssreader.global.exception.NoSuchBlogPlatformException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlogPlatform {
    VELOG, TISTORY, GITPAGE, ETC;

    public static BlogPlatform parseLink(String link) {

        return Arrays.stream(BlogPlatform.values())
                .filter(blogPlatform -> link.toUpperCase().contains(blogPlatform.name()))
                .findFirst()
                .orElseThrow(() -> new NoSuchBlogPlatformException(link));
    }
}

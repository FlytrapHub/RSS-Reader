package com.flytrap.rssreader.domain.subscribe;

import com.flytrap.rssreader.global.model.DefaultDomain;
import com.flytrap.rssreader.global.model.Domain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Domain(name = "subscribe")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscribe implements DefaultDomain {

    private Long id;
    private String url;
    private String description;

    @Builder
    protected Subscribe(Long id, String url, String description) {
        this.id = id;
        this.url = url;
        this.description = description;
    }

    public static Subscribe create(String url, String description) {
        return Subscribe.builder()
                .url(url)
                .description(description)
                .build();
    }

    public static Subscribe of(Long id, String url, String description) {
        return Subscribe.builder()
                .id(id)
                .url(url)
                .description(description)
                .build();
    }

    public static Subscribe of(Long id, String url) {
        return Subscribe.builder()
                .id(id)
                .url(url)
                .build();
    }
}

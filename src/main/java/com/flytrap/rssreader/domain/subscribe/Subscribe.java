package com.flytrap.rssreader.domain.subscribe;

import com.flytrap.rssreader.global.model.DefaultDomain;
import com.flytrap.rssreader.global.model.Domain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Domain(name = "subscribe")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscribe implements DefaultDomain {

    private Long id;
    private String title;
    private String url;
    private String description;

    @Builder
    public Subscribe(Long id, String title, String url, String description) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.description = description;
    }

    public static Subscribe create(String url, String description) {
        return Subscribe.builder()
                .url(url)
                .description(description)
                .build();
    }

    public static Subscribe of(Long id, String title, String url, String description) {
        return Subscribe.builder()
                .id(id)
                .title(title)
                .url(url)
                .description(description)
                .build();
    }

    public static Subscribe of(Long id, String title, String url) {
        return Subscribe.builder()
                .id(id)
                .title(title)
                .url(url)
                .build();
    }
}

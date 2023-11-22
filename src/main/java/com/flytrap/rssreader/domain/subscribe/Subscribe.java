package com.flytrap.rssreader.domain.subscribe;

import com.flytrap.rssreader.global.model.Domain;
import com.flytrap.rssreader.presentation.dto.RssFeedData;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Domain(name = "subscribe")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscribe {

    private Long id;
    private String description;

    @Builder
    protected Subscribe(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public static Subscribe create(RssFeedData rssFeedData) {
        return Subscribe.builder()
                .description(rssFeedData.description())
                .build();
    }
}

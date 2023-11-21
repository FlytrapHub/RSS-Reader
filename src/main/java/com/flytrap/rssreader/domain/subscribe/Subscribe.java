package com.flytrap.rssreader.domain.subscribe;

import com.flytrap.rssreader.domain.folder.SharedStatus;
import com.flytrap.rssreader.global.model.Domain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Domain(name = "subscribe")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscribe {

    private Long id;

    @Builder
    protected Subscribe(Long id) {
        this.id = id;
    }
}

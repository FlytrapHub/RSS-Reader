package com.flytrap.rssreader.service.dto;

import com.flytrap.rssreader.infrastructure.entity.post.OpenEntity;

public record PostOpenParam(long memberId,
                            long postId) {

    public OpenEntity toEntity() {
        return OpenEntity.builder()
                         .postId(postId)
                         .memberId(memberId)
                         .build();
    }
}

package com.flytrap.rssreader.service.dto;

import com.flytrap.rssreader.infrastructure.entity.post.OpenEntity;

public record PostOpenParam(long postId,
                            long memberId) {

    public static PostOpenParam of(long postId,
                                   long memberId) {
        return new PostOpenParam(postId, memberId);
    }

    public OpenEntity toEntity() {
        return OpenEntity.builder()
                         .postId(postId)
                         .memberId(memberId)
                         .build();
    }
}

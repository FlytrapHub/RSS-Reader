package com.flytrap.rssreader.service.dto;


import com.flytrap.rssreader.api.post.infrastructure.entity.OpenEntity;

@Deprecated
public record PostOpenParam(Long memberId,
                            Long postId) {

    public static PostOpenParam create(Long memberId, Long postId) {
        return new PostOpenParam(memberId, postId);
    }

    public OpenEntity toEntity() {
        return OpenEntity.builder()
                         .postId(postId)
                         .memberId(memberId)
                         .build();
    }
}

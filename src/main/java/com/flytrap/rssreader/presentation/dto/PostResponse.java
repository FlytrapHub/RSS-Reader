package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.post.Post;
import java.time.Instant;
import java.util.List;

@Deprecated
public record PostResponse(
    long id,
    String guid,
    String title,
    String thumbnailUrl,
    String description,
    Instant pubDate,
    String subscribeTitle,
    boolean open,
    boolean bookmark
) {

    public record PostListResponse(
        List<PostResponse> posts
        // TODO: react
    ) { }

    public static PostResponse from(Post post) {
        return new PostResponse(
            post.getId(),
            post.getGuid(),
            post.getTitle(),
            post.getThumbnailUrl(),
            post.getDescription(),
            post.getPubDate(),
            post.getSubscribeTitle(),
            post.isOpen(),
            post.isBookmark()
        );
    }
}

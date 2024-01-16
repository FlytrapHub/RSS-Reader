package com.flytrap.rssreader.infrastructure.repository.output;

import com.flytrap.rssreader.domain.post.Post;
import java.time.Instant;

public record PostPaginationOutput(
        Long id,
        Long subscribeId,
        String guid,
        String title,
        String thumbnailUrl,
        String description,
        Instant pubDate,
        String subscribeTitle,
        boolean open,
        boolean bookmark,
        String cursor
        // TODO: react 추가하기
) {

    public Post toDomain() {
        return Post.builder()
                .id(id)
                .subscribeId(subscribeId)
                .guid(guid)
                .title(title)
                .thumbnailUrl(thumbnailUrl)
                .description(description)
                .pubDate(pubDate)
                .subscribeTitle(subscribeTitle)
                .open(open)
                .bookmark(bookmark)
                .build();
    }
}

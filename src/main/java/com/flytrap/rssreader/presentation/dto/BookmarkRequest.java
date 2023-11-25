package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.bookmark.Bookmark;
import jakarta.validation.constraints.NotNull;

public record BookmarkRequest() {

    public record CreateRequest(@NotNull Long postId) {
    }

    public record DeleteRequest(@NotNull Long bookmarkId) {
    }

    public record Response(Long id, Long memberId, Long postId) {
        public static Response from(Bookmark bookmark) {
            return new Response(bookmark.getId(), bookmark.getMemberId(), bookmark.getPostId());
        }
    }

}

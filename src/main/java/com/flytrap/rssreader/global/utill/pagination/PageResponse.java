package com.flytrap.rssreader.global.utill.pagination;

public record PageResponse<T>(
        T content,
        String nextPageCursor,
        Boolean hasNext
) {
}

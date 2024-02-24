package com.flytrap.rssreader.api.post.domain;

import org.springframework.util.StringUtils;

public record PostFilter( //TODO ???
        Boolean read,
        Long start,
        Long end,
        String keyword
) {
    public boolean hasKeyword() {
        return StringUtils.hasText(keyword);
    }

    public boolean hasDataRange() {
        return start != null && end != null;
    }

    public boolean hasReadCondition() {
        return read != null && read;
    }

    public boolean hasUnReadCondition() {
        return read != null && !read;
    }
}
// TODO : 전체 레이어를 관통하는 필터 로직. 어떻게 해결하나?

// http request
// | controller
// filter
// | service
// domain
// | controller
// response
 // 다 된건가

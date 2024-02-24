package com.flytrap.rssreader.presentation.dto;

import org.springframework.util.StringUtils;

@Deprecated
public record PostFilter(Boolean read,
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

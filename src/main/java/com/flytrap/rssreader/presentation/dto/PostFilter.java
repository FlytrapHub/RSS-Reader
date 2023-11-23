package com.flytrap.rssreader.presentation.dto;

public record PostFilter(Boolean read,
                         Long start,
                         Long end,
                         String keyword
) {

}

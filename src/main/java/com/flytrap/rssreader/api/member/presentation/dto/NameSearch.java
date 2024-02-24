package com.flytrap.rssreader.api.member.presentation.dto;

import com.flytrap.rssreader.presentation.dto.MemberSummary;

import java.util.List;

public record NameSearch(String name) {

    public record Result(List<MemberSummary> memberSummary) {
    }
}

package com.flytrap.rssreader.presentation.dto;

public record NameSearch(String name) {

    public record Result(MemberSummary memberSummary) {
    }
}

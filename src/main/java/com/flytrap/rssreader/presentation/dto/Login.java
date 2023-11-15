package com.flytrap.rssreader.presentation.dto;

import lombok.Getter;

public record Login(Request request) {

    public record Request (String code) {}

}

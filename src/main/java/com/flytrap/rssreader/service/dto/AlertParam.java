package com.flytrap.rssreader.service.dto;


public record AlertParam(String url, String title) {

    public static AlertParam create(String url, String title) {
        return new AlertParam(url, title);
    }
}


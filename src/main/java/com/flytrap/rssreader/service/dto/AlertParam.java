package com.flytrap.rssreader.service.dto;


import java.util.Map;

public record AlertParam(Map<String, String> posts, String name) {

    public static AlertParam create(Map<String, String> posts, String name) {
        return new AlertParam(posts, name);
    }
}


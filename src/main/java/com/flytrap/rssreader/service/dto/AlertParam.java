package com.flytrap.rssreader.service.dto;


public record AlertParam(Integer serviceId) {

    public static AlertParam create(Integer serviceId) {
        return new AlertParam(serviceId);
    }
}


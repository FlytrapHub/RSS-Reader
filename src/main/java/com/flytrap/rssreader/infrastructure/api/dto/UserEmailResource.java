package com.flytrap.rssreader.infrastructure.api.dto;

public record UserEmailResource(String email,
                                boolean primary,
                                boolean verified) {

}

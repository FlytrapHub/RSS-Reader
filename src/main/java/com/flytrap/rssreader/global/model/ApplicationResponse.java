package com.flytrap.rssreader.global.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationResponse<T> {
     T data;

    public static ApplicationResponse<String> success() {
        return new ApplicationResponse<>("success");
    }

    public static ApplicationResponse<Object> success(Object data) {
        return new ApplicationResponse<>(data);
    }
}

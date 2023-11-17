package com.flytrap.rssreader.global.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationResponse<T> {
     T data;
}

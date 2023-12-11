package com.flytrap.rssreader.presentation.docs.post;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Tag(name = "게시글 목록 불러오기")
public @interface PostListReadControllerDocs { }


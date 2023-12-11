package com.flytrap.rssreader.presentation.docs.post;

import com.flytrap.rssreader.presentation.dto.PostResponse.PostListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(summary = "구독한 블로그의 게시글 목록 불러오기", description = "구독한 블로그의 게시글 목록을 반환한다.")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostListResponse.class))),
})
public @interface GetPostsBySubscribeDocs { }

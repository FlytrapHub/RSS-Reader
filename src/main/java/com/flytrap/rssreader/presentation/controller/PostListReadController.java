package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import com.flytrap.rssreader.presentation.dto.PostResponse;
import com.flytrap.rssreader.presentation.dto.PostResponse.PostListResponse;
import com.flytrap.rssreader.service.PostListReadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscribes/{subscribeId}/posts")
public class PostListReadController {

    private final PostListReadService postListReadService;

    @GetMapping
    public ApplicationResponse<PostListResponse> getPostsByFolder(
            @PathVariable Long subscribeId,
            PostFilter postFilter,
            @PageableDefault(page = 0, size = 15) Pageable pageable) {

        List<PostResponse> posts = postListReadService.getPosts(subscribeId, postFilter, pageable)
            .stream()
            .map(PostResponse::from)
            .toList();

        return new ApplicationResponse<>(
            new PostListResponse(posts));
    }

}

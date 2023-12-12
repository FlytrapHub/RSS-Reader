package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.controller.api.PostReadControllerApi;
import com.flytrap.rssreader.presentation.dto.PostResponse;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import com.flytrap.rssreader.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostReadController implements PostReadControllerApi {

    private final PostReadService postReadService;


    @GetMapping("/{postId}")
    public ApplicationResponse<PostResponse> getPost(
            @PathVariable Long postId,
            @Login SessionMember member) {

        PostResponse post = PostResponse.from(postReadService.getPost(member, postId));

        return new ApplicationResponse<>(post); //TODO 내보내는 데이터를 PostResponse로 변경
    }
}

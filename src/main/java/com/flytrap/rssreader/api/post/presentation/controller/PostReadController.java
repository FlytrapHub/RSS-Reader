package com.flytrap.rssreader.api.post.presentation.controller;

import com.flytrap.rssreader.api.post.business.service.PostReadService;
import com.flytrap.rssreader.api.post.presentation.controller.swagger.PostReadControllerApi;
import com.flytrap.rssreader.api.post.presentation.dto.response.PostResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
import com.flytrap.rssreader.global.presentation.resolver.Login;
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
        return new ApplicationResponse<>(post);
    }
}

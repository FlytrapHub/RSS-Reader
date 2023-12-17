package com.flytrap.rssreader.presentation.controller.api;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.PostResponse;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import org.springframework.web.bind.annotation.PathVariable;

public interface PostReadControllerApi {

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<PostResponse> getPost(
        @PathVariable Long postId,
        @Login SessionMember member);
}

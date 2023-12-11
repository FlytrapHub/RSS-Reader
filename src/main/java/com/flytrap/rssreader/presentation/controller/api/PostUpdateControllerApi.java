package com.flytrap.rssreader.presentation.controller.api;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.ReactionRequest;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface PostUpdateControllerApi {

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<Long> addReaction(
        @PathVariable Long postId,
        @Valid @RequestBody ReactionRequest request,
        @Login SessionMember member);

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<Void> deleteReaction(
        @PathVariable Long postId,
        @Login SessionMember member);

    ApplicationResponse<Void> deleteRead(
        @PathVariable Long postId,
        @Login SessionMember member);
}

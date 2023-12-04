package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.ReactionRequest;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import com.flytrap.rssreader.service.PostOpenService;
import com.flytrap.rssreader.service.ReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostUpdateController {

    private final ReactionService reactionService;
    private final PostOpenService postOpenService;

    /**
     * 리액션 반응은 공유된 폴더의 POST만 가능합니다. 공유 된 폴더인지 체크 한 후 POST와 MEMBER사이에 리액션이 일어납니다.
     *
     * @param postId
     * @param member
     * @return
     */
    @PostMapping("/{postId}/reactions")
    public ApplicationResponse<Long> addReaction(
            @PathVariable Long postId,
            @Valid @RequestBody ReactionRequest request,
            @Login SessionMember member) {

        //TODO: 1.공유된 폴더인가?, 유효한 폴더 인가?
        // 2.공유폴더에 구독된 POST를 알아야한다
        Long reaction = reactionService.addReaction(postId, member.id(), request.emoji());

        return new ApplicationResponse<>(reaction);
    }

    @DeleteMapping("/{postId}/reactions")
    public ApplicationResponse<Void> deleteReaction(
            @PathVariable Long postId,
            @Login SessionMember member) {

        //TODO: 1.공유된 폴더인가?, 유효한 폴더 인가?
        // 2.공유폴더에 구독된 POST를 알아야한다
        reactionService.deleteReaction(postId, member.id());

        return new ApplicationResponse<>(null);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{postId}/read")
    public ApplicationResponse<Void> deleteRead(
            @PathVariable Long postId,
            @Login SessionMember member) {

        postOpenService.deleteRead(member.id(), postId);

        return new ApplicationResponse<>(null);
    }
}

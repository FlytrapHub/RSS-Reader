package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.ReactionRequest;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import com.flytrap.rssreader.service.FolderVerifyOwnerService;
import com.flytrap.rssreader.service.ReactionService;
import com.flytrap.rssreader.service.SharedFolderReadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostUpdateController {

    private final ReactionService reactionService;
    private final SharedFolderReadService sharedFolderReadService;
    private final FolderVerifyOwnerService folderVerifyOwnerService;

    /**
     * 리액션 반응은 공유된 폴더의 POST만 가능합니다. 공유 된 폴더인지 체크 한 후 POST와 MEMBER사이에 리액션이 일어납니다.
     *
     * @param postId
     * @param member
     * @return
     */
    @PostMapping("/posts/{postId}/reactions")
    public ApplicationResponse<Long> addReaction(
            @PathVariable Long postId,
            @Valid @RequestBody ReactionRequest request,
            @Login SessionMember member) {

        //TODO: 1.공유된 폴더인가?, 유효한 폴더 인가?
        // 2.공유폴더에 구독된 POST를 알아야한다
        Long reaction = reactionService.addReaction(postId, member.id(), request.emoji());

        return new ApplicationResponse<>(reaction);
    }
}

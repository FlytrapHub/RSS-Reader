package com.flytrap.rssreader.presentation.controller.api;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.InviteMemberRequest;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import javax.security.sasl.AuthenticationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface SharedFolderUpdateControllerApi {

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse inviteMember(
        @PathVariable Long folderId,
        @Login SessionMember loginMember,
        @RequestBody InviteMemberRequest request
    ) throws AuthenticationException;

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse leaveFolder(
        @PathVariable Long folderId,
        @Login SessionMember member
    );

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse deleteMember(
        @PathVariable Long folderId,
        @PathVariable Long inviteeId,
        @Login SessionMember member
    ) throws AuthenticationException;
}

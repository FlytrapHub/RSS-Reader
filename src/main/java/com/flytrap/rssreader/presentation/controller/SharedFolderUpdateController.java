package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.controller.api.SharedFolderUpdateControllerApi;
import com.flytrap.rssreader.presentation.dto.InviteMemberRequest;
import com.flytrap.rssreader.presentation.dto.MemberSummary;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import com.flytrap.rssreader.service.SharedFolderReadService;
import com.flytrap.rssreader.service.folder.FolderUpdateService;
import com.flytrap.rssreader.service.folder.FolderVerifyService;
import com.flytrap.rssreader.service.MemberService;
import com.flytrap.rssreader.service.SharedFolderUpdateService;
import javax.security.sasl.AuthenticationException;
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
@RequestMapping("/api/folders")
public class SharedFolderUpdateController implements SharedFolderUpdateControllerApi {

    private final SharedFolderReadService sharedFolderReadService;
    private final SharedFolderUpdateService sharedFolderUpdateService;
    private final FolderUpdateService folderUpdateService;
    private final FolderVerifyService folderVerifyService;
    private final MemberService memberService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{folderId}/members")
    public ApplicationResponse<MemberSummary> inviteMember(
            @PathVariable Long folderId,
            @Login SessionMember loginMember,
            @RequestBody InviteMemberRequest request
    ) throws AuthenticationException {

        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, loginMember.id());
        Member member = memberService.findById(request.inviteeId());
        sharedFolderUpdateService.invite(verifiedFolder, member.getId());
        folderUpdateService.shareFolder(verifiedFolder);

        return new ApplicationResponse<>(MemberSummary.from(member));
    }

    // 공유 폴더에 사람 나가기 (내가 스스로 나간다)
    @DeleteMapping("/{folderId}/members/me")
    public ApplicationResponse<String> leaveFolder(
            @PathVariable Long folderId,
            @Login SessionMember member
    ) {
        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id());
        
        sharedFolderUpdateService.leave(verifiedFolder, member.id());

        if (sharedFolderReadService.countAllMembersByFolder(folderId) <= 0) {
            folderUpdateService.makePrivate(verifiedFolder);
        }

        return ApplicationResponse.success();
    }

    //공유 폴더에 사람 삭제하기 (만든 사람만)
    @DeleteMapping("/{folderId}/members/{inviteeId}")
    public ApplicationResponse<String> deleteMember(
            @PathVariable Long folderId,
            @PathVariable Long inviteeId,
            @Login SessionMember member
    ) throws AuthenticationException {
        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id());

        sharedFolderUpdateService.removeFolderMember(verifiedFolder, inviteeId, member.id());

        if (sharedFolderReadService.countAllMembersByFolder(folderId) <= 0) {
            folderUpdateService.makePrivate(verifiedFolder);
        }

        return ApplicationResponse.success();
    }
}

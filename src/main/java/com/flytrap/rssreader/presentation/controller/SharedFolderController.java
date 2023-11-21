package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.InviteMemberRequest;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import com.flytrap.rssreader.service.FolderUpdateService;
import com.flytrap.rssreader.service.FolderVerifyOwnerService;
import com.flytrap.rssreader.service.SharedFolderService;
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
public class SharedFolderController {

    private final SharedFolderService sharedFolderService;
    private final FolderUpdateService folderUpdateService;
    private final FolderVerifyOwnerService folderVerifyOwnerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{folderId}/members")
    public ApplicationResponse inviteMember(
            @PathVariable Long folderId,
            @Login SessionMember member,
            @RequestBody InviteMemberRequest request
    ) {
        Folder verifiedFolder = folderVerifyOwnerService.getVerifiedFolder(folderId, member.id());
        folderUpdateService.shareFolder(verifiedFolder);
        sharedFolderService.invite(verifiedFolder, request.inviteeId());
        return ApplicationResponse.success(request.inviteeId());
    }

    // 공유 폴더에 사람 나가기 (내가 스스로 나간다)
    @DeleteMapping("/{folderId}/members/me")
    public ApplicationResponse leaveFolder(
            @PathVariable Long folderId,
            @Login SessionMember member
    ) {
        Folder verifiedFolder = folderVerifyOwnerService.getVerifiedFolder(folderId, member.id());
        sharedFolderService.leave(verifiedFolder, member.id());
        return ApplicationResponse.success();
    }

    //공유 폴더에 사람 삭제하기 (만든 사람만)
    @DeleteMapping("/{folderId}/members/{memberId}")
    public ApplicationResponse deleteMember(
            @PathVariable Long folderId,
            @PathVariable Long memberId,
            @Login SessionMember member
    ) {
        Folder verifiedFolder = folderVerifyOwnerService.getVerifiedFolder(folderId, member.id());
        sharedFolderService.makeOut(verifiedFolder, memberId, member.id());
        return ApplicationResponse.success();
    }
}

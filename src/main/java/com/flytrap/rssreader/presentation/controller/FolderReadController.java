package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.Folders;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.facade.InvitedToFolderFacade;
import com.flytrap.rssreader.presentation.facade.MyFolderFacade;
import com.flytrap.rssreader.presentation.facade.OpenCheckFacade;
import com.flytrap.rssreader.presentation.facade.SubscribeInFolderFacade;
import com.flytrap.rssreader.presentation.resolver.Login;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderReadController {

    private final MyFolderFacade myFolderFacade;
    private final SubscribeInFolderFacade subscribeInFolderFacade;
    private final OpenCheckFacade openCheckFacade;
    private final InvitedToFolderFacade invitedToFolderFacade;

    @GetMapping
    public ApplicationResponse<Folders> getFolders(@Login SessionMember member) {

        // 내가 소속된 폴더 목록 반환
        List<Folder> folders = myFolderFacade.getMyFolders(member.id());

        // 폴더당 블로그 목록 추가
        folders = subscribeInFolderFacade.addSubscribesInFolder(folders);

        // 폴더당 초대된 멤버 추가
        folders = invitedToFolderFacade.addInvitedMembersInFolder(folders);

        // 블로그당 읽지 않은 글 개수 추가
        folders = openCheckFacade.addUnreadCountInSubscribes(member.id(), folders);

        return new ApplicationResponse(Folders.from(folders));
    }
}

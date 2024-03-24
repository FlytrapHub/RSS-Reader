package com.flytrap.rssreader.api.folder.presentation.controller;

import com.flytrap.rssreader.api.folder.business.facade.InvitedToFolderFacade;
import com.flytrap.rssreader.api.folder.business.facade.MyFolderFacade;
import com.flytrap.rssreader.api.folder.business.service.FolderSubscribeService;
import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.presentation.controller.swagger.FolderReadControllerApi;
import com.flytrap.rssreader.api.folder.presentation.dto.Folders;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
import com.flytrap.rssreader.api.subscribe.presentation.dto.SubscribeRequest;
import com.flytrap.rssreader.api.post.business.facade.OpenCheckFacade;
import com.flytrap.rssreader.api.subscribe.business.facade.SubscribeInFolderFacade;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import com.flytrap.rssreader.api.subscribe.business.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderReadController implements FolderReadControllerApi {

    private final MyFolderFacade myFolderFacade;
    private final SubscribeInFolderFacade subscribeInFolderFacade;
    private final OpenCheckFacade openCheckFacade;
    private final InvitedToFolderFacade invitedToFolderFacade;
    private final FolderVerifyService folderVerifyService;
    private final SubscribeService subscribeService;
    private final FolderSubscribeService folderSubscribeService;

    @GetMapping
    public ApplicationResponse<Folders> getFolders(@Login SessionMember member) {

        // 내가 소속된 폴더 목록 반환
        List<? extends Folder> folders = myFolderFacade.getMyFolders(member.id());

        // 폴더당 블로그 목록 추가
        folders = subscribeInFolderFacade.addSubscribesInFolder(folders);

        // 폴더당 초대된 멤버 추가
        folders = invitedToFolderFacade.addInvitedMembersInFolder(folders);

        // 블로그당 읽지 않은 글 개수 추가
        folders = openCheckFacade.addUnreadCountInSubscribes(member.id(), folders);

        return new ApplicationResponse(Folders.from(folders));
    }

    @GetMapping("/{folderId}/rss")
    public ApplicationResponse<SubscribeRequest.ResponseList> read(
            @PathVariable Long folderId,
            @Login SessionMember member) {

        //TODO 폴더에 추가된 블로그 리스트 보기 즉 folderId 가 일치하는 구독된 정보를 다가져오기
        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id());
        List<Long> list = folderSubscribeService.getFolderSubscribeId(verifiedFolder.getId());
        List<Subscribe> subscribeList = subscribeService.read(list);
        return new ApplicationResponse<>(SubscribeRequest.ResponseList.from(subscribeList));
    }

}

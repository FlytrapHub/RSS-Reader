package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.folder.FolderSubscribe;
import com.flytrap.rssreader.domain.subscribe.Subscribe;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.infrastructure.entity.alert.AlertPlatform;
import com.flytrap.rssreader.presentation.controller.api.FolderUpdateControllerApi;
import com.flytrap.rssreader.presentation.dto.AlertRequest;
import com.flytrap.rssreader.presentation.dto.FolderRequest;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.dto.SubscribeRequest;
import com.flytrap.rssreader.presentation.facade.OpenCheckFacade;
import com.flytrap.rssreader.presentation.resolver.Login;
import com.flytrap.rssreader.service.alert.AlertService;
import com.flytrap.rssreader.service.folder.FolderSubscribeService;
import com.flytrap.rssreader.service.folder.FolderUpdateService;
import com.flytrap.rssreader.service.folder.FolderVerifyOwnerService;
import com.flytrap.rssreader.service.SubscribeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderUpdateController implements FolderUpdateControllerApi {

    private final FolderUpdateService folderService;
    private final FolderVerifyOwnerService folderVerifyOwnerService;
    private final SubscribeService subscribeService;
    private final FolderSubscribeService folderSubscribeService;
    private final AlertService alertService;
    private final OpenCheckFacade openCheckFacade;

    @PostMapping
    public ApplicationResponse<FolderRequest.Response> createFolder(
        @Valid @RequestBody FolderRequest.CreateRequest request,
        @Login SessionMember member) {

        Folder newFolder = folderService.createNewFolder(request, member.id());

        return new ApplicationResponse<>(FolderRequest.Response.from(newFolder));
    }

    @PatchMapping("/{folderId}")
    public ApplicationResponse<FolderRequest.Response> updateFolder(
        @Valid @RequestBody FolderRequest.CreateRequest request,
        @PathVariable Long folderId,
        @Login SessionMember member) {

        Folder verifiedFolder = folderVerifyOwnerService.getVerifiedFolder(folderId, member.id());
        Folder updatedFolder = folderService.updateFolder(request, verifiedFolder, member.id());

        return new ApplicationResponse<>(FolderRequest.Response.from(updatedFolder));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{folderId}")
    public ApplicationResponse<String> deleteFolder(
        @PathVariable Long folderId,
        @Login SessionMember member) {

        Folder verifiedFolder = folderVerifyOwnerService.getVerifiedFolder(folderId, member.id());
        Folder folder = folderService.deleteFolder(verifiedFolder, member.id());

        return new ApplicationResponse<>("폴더가 삭제되었습니다 : " + folder.getName());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{folderId}/rss")
    public ApplicationResponse<SubscribeRequest.Response> subscribe(
        @PathVariable Long folderId,
        @Valid @RequestBody SubscribeRequest.CreateRequest request,
        @Login SessionMember member) {
        //TODO: 존재하는 폴더인지 검증하는 로직 , 공유폴더초대받은 인지 검증
        //TODO: 내가만든 폴더랑, 초대받은 그룹의 폴더인지 구분해서 구독해야합니다.
        //지금 부분은 검증된 개인 폴더입니다.
        Folder verifiedFolder = folderVerifyOwnerService.getVerifiedFolder(folderId, member.id());
        Subscribe subscribe = subscribeService.subscribe(request);
        folderSubscribeService.folderSubscribe(subscribe,
            verifiedFolder.getId());

        FolderSubscribe folderSubscribe = FolderSubscribe.from(subscribe);
        folderSubscribe = openCheckFacade.addUnreadCountInFolderSubscribe(member.id(), subscribe, folderSubscribe);

        return new ApplicationResponse<>(SubscribeRequest.Response.from(folderSubscribe));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{folderId}/rss/{subscribeId}")
    public ApplicationResponse<Void> unsubscribe(
        @PathVariable Long folderId,
        @PathVariable Long subscribeId,
        @Login SessionMember member) {

        Folder verifiedFolder = folderVerifyOwnerService.getVerifiedFolder(folderId, member.id());
        subscribeService.unsubscribe(subscribeId);
        folderSubscribeService.folderUnsubscribe(subscribeId,
            verifiedFolder.getId());
        return new ApplicationResponse<>(null);
    }

    @PostMapping("/{folderId}/alerts")
    public ApplicationResponse<Long> onAlert(
        @PathVariable Long folderId,
        @Valid @RequestBody AlertRequest request,
        @Login SessionMember member) {

        Folder verifiedFolder = folderVerifyOwnerService.getVerifiedFolder(folderId, member.id());
        AlertPlatform.ofCode(request.platformNum());
        Long alertID = alertService.on(verifiedFolder.getId(), member.id(), request.platformNum());
        return new ApplicationResponse<>(alertID);
    }

    @DeleteMapping("/{folderId}/alerts ")
    public ApplicationResponse<Void> offAlert(
        @PathVariable Long folderId,
        @Login SessionMember member) {

        Folder verifiedFolder = folderVerifyOwnerService.getVerifiedFolder(folderId, member.id());
        alertService.off(verifiedFolder.getId(), member.id());
        return new ApplicationResponse<>(null);
    }
}

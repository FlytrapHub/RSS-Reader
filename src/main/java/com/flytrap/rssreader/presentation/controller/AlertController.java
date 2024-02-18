package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.domain.alert.Alert;
import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.controller.api.AlertControllerApi;
import com.flytrap.rssreader.presentation.dto.AlertRequest;
import com.flytrap.rssreader.presentation.dto.AlertResponse;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import com.flytrap.rssreader.service.alert.AlertService;
import com.flytrap.rssreader.service.folder.FolderVerifyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class AlertController implements AlertControllerApi {

    private final AlertService alertService;
    private final FolderVerifyService folderVerifyService;

    @PostMapping("/{folderId}/alerts")
    public ApplicationResponse<AlertResponse> registerAlert(
        @PathVariable Long folderId,
        @Valid @RequestBody AlertRequest request,
        @Login SessionMember member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedAccessableFolder(folderId, member.id());
        Alert alert = alertService.registerAlert(verifiedFolder.getId(), member.id(), request.webhookUrl());
        return new ApplicationResponse<>(AlertResponse.from(alert));
    }

    @DeleteMapping("/{folderId}/alerts ")
    public ApplicationResponse<Void> offAlert(
        @PathVariable Long folderId,
        @Login SessionMember member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id());
        alertService.off(verifiedFolder.getId(), member.id());
        return new ApplicationResponse<>(null);
    }

}

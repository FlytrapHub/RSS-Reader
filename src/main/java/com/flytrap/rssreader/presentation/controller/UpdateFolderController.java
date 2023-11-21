package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.FolderCreate;
import com.flytrap.rssreader.presentation.dto.FolderCreate.Response;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import com.flytrap.rssreader.service.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class UpdateFolderController {

    private final FolderService folderService;

    @PostMapping
    public ApplicationResponse<Response> createFolder(
            @Valid @RequestBody FolderCreate request,
            @Login SessionMember member) {

        Folder newFolder = folderService.createNewFolder(request, member.id());

        return new ApplicationResponse<>(FolderCreate.Response.from(newFolder));
    }

    @DeleteMapping
    public ApplicationResponse<Response> deleteSubscribeFolder(
            @Valid @RequestBody FolderCreate request,
            @Login SessionMember member) {

        Folder newFolder = folderService.createNewFolder(request, member.id());

        return new ApplicationResponse<>(FolderCreate.Response.from(newFolder));
    }

    @PostMapping
    public ApplicationResponse<Response> subscribeFolder(
            @Valid @RequestBody FolderCreate request,
            @Login SessionMember member) {

        Folder newFolder = folderService.createNewFolder(request, member.id());

        return new ApplicationResponse<>(FolderCreate.Response.from(newFolder));
    }
}

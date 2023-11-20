package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import com.flytrap.rssreader.presentation.dto.FolderRequest;
import com.flytrap.rssreader.service.FolderService;
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
public class FolderController {

    private final FolderService folderService;

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

        Folder updatedFolder = folderService.updateFolder(request, folderId, member.id());

        return new ApplicationResponse<>(FolderRequest.Response.from(updatedFolder));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{folderId}")
    public ApplicationResponse<Void> deleteFolder(
            @PathVariable Long folderId,
            @Login SessionMember member) {

        folderService.deleteFolder(folderId, member.id());

        return new ApplicationResponse<>(null);
    }
}

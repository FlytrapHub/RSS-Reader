package com.flytrap.rssreader.presentation.controller.api;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.AlertRequest;
import com.flytrap.rssreader.presentation.dto.FolderRequest;
import com.flytrap.rssreader.presentation.dto.FolderRequest.Response;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.dto.SubscribeRequest;
import com.flytrap.rssreader.presentation.resolver.Login;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface FolderUpdateControllerApi {

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<Response> createFolder(
        @Valid @RequestBody FolderRequest.CreateRequest request,
        @Login SessionMember member);

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<FolderRequest.Response> updateFolder(
        @Valid @RequestBody FolderRequest.CreateRequest request,
        @PathVariable Long folderId,
        @Login SessionMember member);

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<String> deleteFolder(
        @PathVariable Long folderId,
        @Login SessionMember member);

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<SubscribeRequest.Response> subscribe(
        @PathVariable Long folderId,
        @Valid @RequestBody SubscribeRequest.CreateRequest request,
        @Login SessionMember member);

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<Void> unsubscribe(
        @PathVariable Long folderId,
        @PathVariable Long subscribeId,
        @Login SessionMember member);

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<SubscribeRequest.ResponseList> read(
        @PathVariable Long folderId,
        @Login SessionMember member);

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<Long> onAlert(
        @PathVariable Long folderId,
        @Valid @RequestBody AlertRequest request,
        @Login SessionMember member);

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<Void> offAlert(
        @PathVariable Long folderId,
        @Login SessionMember member);
}

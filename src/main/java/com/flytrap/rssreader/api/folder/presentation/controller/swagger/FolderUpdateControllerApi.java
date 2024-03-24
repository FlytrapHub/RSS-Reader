package com.flytrap.rssreader.api.folder.presentation.controller.swagger;

import com.flytrap.rssreader.api.folder.presentation.dto.FolderRequest;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertRequest;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
import com.flytrap.rssreader.api.subscribe.presentation.dto.SubscribeRequest;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface FolderUpdateControllerApi {

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<FolderRequest.Response> createFolder(
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

    @Operation(summary = "폴더에 블로그 추가하기(블로그 구독하기)", description = "이미 추가된 폴더에 블로그를 새로 추가한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "블로그 추가 성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscribeRequest.Response.class))),
    })
    ApplicationResponse<SubscribeRequest.Response> subscribe(
        @Parameter(description = "블로그를 추가할 폴더의 ID") @PathVariable Long folderId,
        @Parameter(description = "추가할 블로그의 주소") @Valid @RequestBody SubscribeRequest.CreateRequest request,
        @Parameter(description = "현재 로그인한 회원 정보") @Login SessionMember member);
}

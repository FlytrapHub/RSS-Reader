package com.flytrap.rssreader.presentation.controller.api;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.AlertRequest;
import com.flytrap.rssreader.presentation.dto.AlertResponse;
import com.flytrap.rssreader.presentation.dto.PostResponse.PostListResponse;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface AlertControllerApi {

    @Operation(summary = "알림 웹 훅 등록하기", description = "알림을 보낼 웹 훅 URL을 등록한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertResponse.class))),
    })
    ApplicationResponse<AlertResponse> registerAlert(
        @Parameter(description = "웹 훅을 등록할 Folder의 Id") @PathVariable Long folderId,
        @Parameter(description = "등록할 웹 훅 URL") @Valid @RequestBody AlertRequest request,
        @Parameter(description = "현재 로그인한 회원 정보") @Login SessionMember member);

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<Void> offAlert(
        @PathVariable Long folderId,
        @Login SessionMember member);

}

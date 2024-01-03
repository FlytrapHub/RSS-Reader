package com.flytrap.rssreader.presentation.controller.api;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.InviteMemberRequest;
import com.flytrap.rssreader.presentation.dto.MemberSummary;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.security.sasl.AuthenticationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name ="공유 폴더")
public interface SharedFolderUpdateControllerApi {

    @Operation(summary = "폴더에 회원 초대", description = "폴더에 회원을 한명 초대한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberSummary.class))),
    })
    ApplicationResponse<MemberSummary> inviteMember(
        @Parameter(description = "회원을 초대할 폴더의 ID") @PathVariable Long folderId,
        @Parameter(description = "현재 로그인한 회원 정보") @Login SessionMember loginMember,
        @Parameter(description = "초대할 회원의 ID") @RequestBody InviteMemberRequest request
    ) throws AuthenticationException;

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse leaveFolder(
        @PathVariable Long folderId,
        @Login SessionMember member
    );

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse deleteMember(
        @PathVariable Long folderId,
        @PathVariable Long inviteeId,
        @Login SessionMember member
    ) throws AuthenticationException;
}

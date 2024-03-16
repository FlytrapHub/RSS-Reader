package com.flytrap.rssreader.api.member.presentation.controller.swagger;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.NameSearch;
import com.flytrap.rssreader.presentation.dto.NameSearch.Result;

public interface MemberControllerApi {

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<Result> searchMemberByName(NameSearch nameSearch);
}

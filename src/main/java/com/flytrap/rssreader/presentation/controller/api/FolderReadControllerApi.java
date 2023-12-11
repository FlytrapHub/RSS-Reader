package com.flytrap.rssreader.presentation.controller.api;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.Folders;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;

public interface FolderReadControllerApi {

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<Folders> getFolders(@Login SessionMember member);
}

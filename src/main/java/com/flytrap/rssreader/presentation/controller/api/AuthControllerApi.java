package com.flytrap.rssreader.presentation.controller.api;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.dto.Login;
import com.flytrap.rssreader.presentation.dto.LoginResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerApi {

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<LoginResponse> login(@RequestBody Login request,
        HttpSession session);

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<Void> logout(HttpSession session);
}

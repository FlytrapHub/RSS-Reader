package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.controller.api.AuthControllerApi;
import com.flytrap.rssreader.presentation.dto.Login;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerApi {

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ApplicationResponse<SessionMember> login(@RequestBody Login request,
            HttpSession session) {

        Member member = authService.doAuthentication(request);

        return new ApplicationResponse<>(authService.login(member, session));
    }

    @Override
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApplicationResponse<Void> logout(HttpSession session) {

        authService.logout(session);

        return new ApplicationResponse<>(null); // DTO 반환
    }
}

package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.infrastructure.api.AuthProvider;
import com.flytrap.rssreader.presentation.dto.Login;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String SESSION_KEY = "login_session::";

    private final AuthProvider authProvider;
    private final MemberService memberService;

    public Member doAuthentication(Login request) {
        return authProvider.requestAccessToken(request.code())
            .flatMap(authProvider::requestUserResource)
            .map(memberService::loginMember)
            .block();
    }

    public void login(Member member, HttpSession session) {
        session.setAttribute(SESSION_KEY, member);
    }

    public void logout(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
    }
}

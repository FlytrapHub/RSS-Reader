package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.infrastructure.api.AuthProvider;
import com.flytrap.rssreader.infrastructure.properties.AuthProperties;
import com.flytrap.rssreader.presentation.dto.Login;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthProperties authProperties;

    private final AuthProvider authProvider;
    private final MemberService memberService;

    public Member doAuthentication(Login request) {
        return authProvider.requestAccessToken(request.code())
                .flatMap(authProvider::requestUserResource)
                .map(memberService::loginMember)
                .block();
    }

    public SessionMember login(Member member, HttpSession session) {
        session.setAttribute(authProperties.sessionId(), SessionMember.from(member));
        return SessionMember.from(member);
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}

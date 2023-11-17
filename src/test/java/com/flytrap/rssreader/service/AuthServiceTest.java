package com.flytrap.rssreader.service;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.when;

import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.domain.member.OauthServer;
import com.flytrap.rssreader.infrastructure.api.AuthProvider;
import com.flytrap.rssreader.infrastructure.api.dto.AccessToken;
import com.flytrap.rssreader.infrastructure.api.dto.UserResource;
import com.flytrap.rssreader.presentation.dto.Login;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@DisplayName("Auth 서비스 로직 -Auth")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    AuthProvider authProvider;

    @Mock
    MemberService memberService;

    @Mock
    HttpSession session;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    void init() {
        when(authProvider.requestAccessToken(anyString()))
                .thenReturn(Mono.just(new AccessToken("test_access_token", "Bearer")));
        when(authProvider.requestUserResource(any()))
                .thenReturn(Mono.just(
                        UserResource.builder().id(1L).email("test@gmail.com").login("login")
                                .avatarUrl("img.jpg").build()));
        when(memberService.loginMember(any()))
                .thenReturn(
                        Member.builder()
                                .id(1L)
                                .name("테스트")
                                .email("test@gmail.com")
                                .profile("img.jpg")
                                .oauthPk(11L)
                                .oauthServer(OauthServer.GITHUB)
                                .build()
                );
    }

    @Test
    @DisplayName("클라이언트가 oauth 인증 서버로부터 받은 코드를 통해 회원가입하거나 로그인할 수 있다.")
    void doAuthentication() {
        // when
        Member member = authService.doAuthentication(new Login("code"));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getId()).isEqualTo(1);
            softAssertions.assertThat(member.getEmail()).isEqualTo("test@gmail.com");
            softAssertions.assertThat(member.getProfile()).isEqualTo("img.jpg");
        });
    }

    @Test
    @DisplayName("클라이언트가 oauth 인증 서버로부터 받은 코드를 통해 세션에 member를 저장된다.")
    void login() {
        //given
        Member member = authService.doAuthentication(new Login("code"));

        //when
        authService.login(member, session);

        //then
        verify(session, times(1)).setAttribute("login_session::", member);
    }

    @Test
    @DisplayName("클라이언트가 oauth 인증 서버로부터 받은 코드를 통해 세션에 member를 삭제된다.")
    void logout() {
        //given
        Member member = authService.doAuthentication(new Login("code"));

        //when
        authService.logout(session);

        //then
        verify(session, times(1)).removeAttribute("login_session::");
    }
}

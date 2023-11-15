package com.flytrap.rssreader.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.infrastructure.api.dto.UserResource;
import com.flytrap.rssreader.infrastructure.entity.member.MemberEntity;
import com.flytrap.rssreader.infrastructure.entity.member.OauthServer;
import com.flytrap.rssreader.infrastructure.repository.MemberEntityJpaRepository;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Member 서비스 로직 -Member")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberEntityJpaRepository memberEntityJpaRepository;

    @InjectMocks
    MemberService memberService;

    @Test
    @DisplayName("기존에 존재하는 멤버인 경우 회원을 저장 하지 않고 멤버를 반환한다.")
    void loginMemberWithExistMember() {
        // given
        when(memberEntityJpaRepository.findByOauthPk(1L))
            .thenReturn(Optional.of(
                MemberEntity.builder()
                    .id(1L)
                    .email("test@gmail.com")
                    .name("name")
                    .profile("avatarUrl.jpg")
                    .oauthPk(1L)
                    .oauthServer(OauthServer.GITHUB)
                    .build()
            ));

        // when
        Member existMember
            = memberService.loginMember(
                new UserResource(1L, "test@gmail.com", "login", "avatarUrl.jpg"));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(existMember.getId()).isEqualTo(1);
            softAssertions.assertThat(existMember.getEmail()).isEqualTo("test@gmail.com");
            softAssertions.assertThat(existMember.getProfile()).isEqualTo("avatarUrl.jpg");
            verify(memberEntityJpaRepository, times(0)).save(any());
        });
    }

    @Test
    @DisplayName("기존에 존재하지 않는 멤버 DB에 저장한 후 멤버를 반환한다.")
    void loginMemberWithNotExistMember() {
        //given
        when(memberEntityJpaRepository.findByOauthPk(1L))
            .thenReturn(Optional.empty());

        when(memberEntityJpaRepository.save(any()))
            .thenReturn(
                MemberEntity.builder()
                    .id(1L)
                    .email("test@gmail.com")
                    .name("name")
                    .profile("avatarUrl.jpg")
                    .oauthPk(1L)
                    .oauthServer(OauthServer.GITHUB)
                    .build()
            );

        // when
        Member newMember
            = memberService.loginMember(
            new UserResource(1L, "test@gmail.com", "login", "avatarUrl.jpg"));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(newMember.getId()).isEqualTo(1);
            softAssertions.assertThat(newMember.getEmail()).isEqualTo("test@gmail.com");
            softAssertions.assertThat(newMember.getProfile()).isEqualTo("avatarUrl.jpg");
            verify(memberEntityJpaRepository, times(1)).save(any());
        });
    }
}
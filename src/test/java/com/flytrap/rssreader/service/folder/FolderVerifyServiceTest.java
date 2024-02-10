package com.flytrap.rssreader.service.folder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.fixture.FixtureFactory;
import com.flytrap.rssreader.fixture.FolderFixtureFactory;
import com.flytrap.rssreader.global.exception.ForbiddenAccessFolderException;
import com.flytrap.rssreader.global.exception.NotBelongToMemberException;
import com.flytrap.rssreader.infrastructure.repository.FolderEntityJpaRepository;

import java.util.Optional;

import com.flytrap.rssreader.infrastructure.repository.SharedFolderJpaRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Folder 생성 멤버 검증 로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class FolderVerifyServiceTest {

    @Mock
    FolderEntityJpaRepository repository;

    @InjectMocks
    FolderVerifyService folderVerifyService;

    Member member;
    Member anotherMember;

    @BeforeEach
    void init() {
        when(repository.findByIdAndIsDeletedFalse(1L)).thenReturn(
                Optional.ofNullable(FolderFixtureFactory.generateFolderEntity()));

        member = FixtureFactory.generateMember();
        anotherMember = FixtureFactory.generateAnotherMember();
    }

    @Nested
    @DisplayName("Folder의 생성 멤버를 검증할 때")
    class getVerifiedFolder {

        @Test
        @DisplayName("생성 멤버가 일치하면 Folder를 반환한다.")
        void is() {
            Folder verifiedFolder = folderVerifyService.getVerifiedOwnedFolder(1L, member.getId());

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(verifiedFolder.getId()).isEqualTo(1L);
                softAssertions.assertThat(verifiedFolder.getName()).isEqualTo("folderName");
                softAssertions.assertThat(verifiedFolder.getMemberId()).isEqualTo(member.getId());
            });
        }

        @Test
        @DisplayName("생성 멤버가 일치하지 않으면 예외를 던진다.")
        void isNot() {
            assertThrows(NotBelongToMemberException.class, () -> {
                folderVerifyService.getVerifiedOwnedFolder(1L, anotherMember.getId());
            });
        }
    }
}

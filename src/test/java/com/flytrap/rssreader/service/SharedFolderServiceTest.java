package com.flytrap.rssreader.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.fixture.FixtureFactory;
import com.flytrap.rssreader.fixture.FolderFixtureFactory;
import com.flytrap.rssreader.infrastructure.entity.shared.SharedFolderMembers;
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
import org.springframework.dao.DuplicateKeyException;

@DisplayName("SharedFolder 서비스 로직 - SharedFolderService")
@ExtendWith(MockitoExtension.class)
class SharedFolderServiceTest {

    @Mock
    SharedFolderJpaRepository sharedFolderJpaRepository;

    @InjectMocks
    SharedFolderService sharedFolderService;

    Member member;
    Member anotherMember;

    Folder folder;

    @BeforeEach
    void init() {
        member = FixtureFactory.generateMember();
        anotherMember = FixtureFactory.generateAnotherMember();
        folder = FolderFixtureFactory.generateFolder();
    }

    @Nested
    @DisplayName("공유 폴더에 사람을 초대할 때")
    class invite {

            @Test
            @DisplayName("공유 폴더에 사람을 초대할 수 있다.")
            void invite_success() {
                sharedFolderService.invite(folder, anotherMember.getId());
                verify(sharedFolderJpaRepository).save(any());
            }

            @Test
            @DisplayName("공유 폴더에 이미 초대된 사람을 초대할 수 없다.")
            void inviteWithAlreadyInvitedMember() {
                when(sharedFolderJpaRepository.existsByFolderIdAndMemberId(folder.getId(), anotherMember.getId()))
                    .thenReturn(true);

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThatThrownBy(() -> sharedFolderService.invite(folder, anotherMember.getId()))
                        .isInstanceOf(DuplicateKeyException.class);
                });
            }

            @Test
            @DisplayName("공유 폴더에 나 자신을 초대할 수 없다.")
            void inviteWithAlreadyJoinedMember() {
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThatThrownBy(() -> sharedFolderService.invite(folder, member.getId()))
                        .isInstanceOf(IllegalArgumentException.class);
                });
            }
    }

    @Nested
    @DisplayName("공유 폴더에서 사람을 삭제할 때")
    class leave {

        @Test
        @DisplayName("공유 폴더에서 사람을 삭제할 수 있다.")
        void leave_success() {
            when(sharedFolderJpaRepository.existsByFolderIdAndMemberId(folder.getId(), anotherMember.getId()))
                .thenReturn(true);

            sharedFolderService.leave(folder, anotherMember.getId());
            verify(sharedFolderJpaRepository).delete(any(SharedFolderMembers.class));
        }

        @Test
        @DisplayName("공유 폴더에서 나 자신을 삭제할 수 없다.")
        void leaveWithMe() {
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThatThrownBy(() -> sharedFolderService.leave(folder, member.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
            });
        }

        @Test
        @DisplayName("공유 폴더에서 존재하지 않는 사람을 삭제할 수 없다.")
        void leaveWithNotExistMember() {
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThatThrownBy(() -> sharedFolderService.leave(folder, 100L))
                    .isInstanceOf(IllegalArgumentException.class);
            });
        }

    }

    @Nested
    @DisplayName("공유 폴더에서 내보내기")
    class MakeOut {

        @Test
        @DisplayName("공유 폴더에서 내보낼 수 있다.")
        void makeOut_success() {
            when(sharedFolderJpaRepository.existsByFolderIdAndMemberId(folder.getId(), anotherMember.getId()))
                .thenReturn(true);

            sharedFolderService.makeOut(folder, anotherMember.getId(), member.getId());
            verify(sharedFolderJpaRepository).delete(any(SharedFolderMembers.class));
        }

        @Test
        @DisplayName("공유 폴더에서 내보낼 수 없다.")
        void makeOutWithNotExistMember() {
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThatThrownBy(() -> sharedFolderService.makeOut(folder, 100L, member.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
            });
        }
    }
}

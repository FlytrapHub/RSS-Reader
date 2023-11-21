package com.flytrap.rssreader.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.fixture.FixtureFactory;
import com.flytrap.rssreader.fixture.FolderFixtureFactory;
import com.flytrap.rssreader.global.exception.NotBelongToMemberException;
import com.flytrap.rssreader.infrastructure.entity.folder.FolderEntity;
import com.flytrap.rssreader.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.presentation.dto.FolderRequest;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Folder 서비스 로직 -Folder")
@ExtendWith(MockitoExtension.class)
class FolderServiceTest {

    @Mock
    FolderEntityJpaRepository repository;

    @InjectMocks
    FolderUpdateService folderService;

    Member member = FixtureFactory.generateMember();

    Folder folder = FolderFixtureFactory.generateFolder();

    String newFolderName = "newFolderName";

    @BeforeEach
    void setUp() {
        when(repository.findByIdAndIsDeletedFalse(1L)).thenReturn(
                Optional.ofNullable(FolderFixtureFactory.generateFolderEntity()));
    }

    @Nested
    @DisplayName("folder 이름 수정 시")
    class ModifyFolderName {

        @Test
        @DisplayName("folder 이름이 성공적으로 변경된다.")
        void modifyFolderName_success() {
            when(repository.save(Mockito.any(FolderEntity.class)))
                    .thenAnswer(i -> i.getArguments()[0]);

            // given
            FolderRequest.CreateRequest request = new FolderRequest.CreateRequest(newFolderName);

            // when
            Folder updatedFolder = folderService.updateFolder(request, folder.getId(),
                    member.getId());

            // then
            // exception 없이 정상적으로 종료
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(updatedFolder.getName()).isEqualTo(newFolderName);
                softAssertions.assertThat(updatedFolder.getId()).isEqualTo(folder.getId());
                softAssertions.assertThat(updatedFolder.getMemberId()).isEqualTo(member.getId());
            });
        }

        @Test
        @DisplayName("작성자가 아닐 시 예외가 발생한다.")
        void modifyFolderName_fail() {
            // given
            FolderRequest.CreateRequest request = new FolderRequest.CreateRequest(newFolderName);

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThatThrownBy(
                                () -> folderService.updateFolder(request, folder.getId(), 2L))
                        .isInstanceOf(NotBelongToMemberException.class);
                softAssertions.assertThat(folder.getName())
                        .isEqualTo(FolderFixtureFactory.FolderFields.name);
            });
        }
    }

    @Nested
    @DisplayName("folder 삭제 시")
    class DeleteFolder {

        @Test
        @DisplayName("folder가 삭제된다.")
        void deleteFolder_success() {
            when(repository.save(Mockito.any(FolderEntity.class)))
                    .thenAnswer(i -> i.getArguments()[0]);

            // when
            Folder deletedFolder = folderService.deleteFolder(folder.getId(), member.getId());
            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(deletedFolder.isDeleted()).isTrue();
                softAssertions.assertThat(deletedFolder.getId()).isEqualTo(folder.getId());
                softAssertions.assertThat(deletedFolder.getMemberId()).isEqualTo(member.getId());
            });
        }

        @Test
        @DisplayName("작성자가 아닐 시 예외가 발생한다.")
        void deleteFolder_authException() {
            // when
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThatThrownBy(
                                () -> folderService.deleteFolder(folder.getId(), 20L))
                        .isInstanceOf(NotBelongToMemberException.class);
                softAssertions.assertThat(folder.isDeleted()).isFalse();
                verify(repository, Mockito.times(0)).save(Mockito.any(FolderEntity.class));
            });
        }
    }
}

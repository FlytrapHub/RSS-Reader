package com.flytrap.rssreader.fixture;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.infrastructure.entity.folder.FolderEntity;

public class FolderFixtureFactory {

    public static class FolderFields {
        public static Long id = 1L;
        public static String name = "folderName";
        public static Member member = FixtureFactory.generateMember();
    }

    public static FolderEntity generateFolderEntity() {
        return FolderEntity.builder()
                .id(FolderFields.id)
                .name(FolderFields.name)
                .memberId(FolderFields.member.getId())
                .isShared(false)
                .isDeleted(false)
                .build();
    }

    public static Folder generateFolder() {
        return Folder.builder()
                .id(FolderFields.id)
                .name(FolderFields.name)
                .memberId(FolderFields.member.getId())
                .isShared(false)
                .isDeleted(false)
                .build();
    }

}

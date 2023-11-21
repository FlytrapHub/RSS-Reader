package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.folder.Folder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FolderCreate(@Size(min = 1, max = 255) String name) {

    public static record Response(Long folderId, String folderName) {

        public static Response from(Folder newFolder) {
            return new Response(newFolder.getId(), newFolder.getName());
        }
    }
}

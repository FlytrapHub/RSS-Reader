package com.flytrap.rssreader.presentation.dto;

import jakarta.validation.constraints.Size;

@Deprecated
public record FolderRequest() {

    public static record CreateRequest(@Size(min = 1, max = 255) String name) {
    }
    public static record Response(Long folderId, String folderName) {
        public static Response from(com.flytrap.rssreader.domain.folder.Folder newFolder) {
            return new Response(newFolder.getId(), newFolder.getName());
        }
    }

}

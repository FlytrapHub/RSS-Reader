package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.folder.SharedStatus;
import com.flytrap.rssreader.domain.shared.SharedFolder;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record Folders(Map<SharedStatus, List<FolderListSummary>> folders) {

    //TODO DTO조립할 것!!!
    public static Folders from(Map<SharedStatus, List<Folder>> folders,
            List<SharedFolder> sharedFolders,
            Map<Long, List<SubscribeEntity>> blogMaps,
            Map<Long, Long> unreadCountMap) {

        List<FolderListSummary> sharedFolderSummary = new ArrayList<>();

        sharedFolderSummary.addAll(sharedFolders.stream()
                .map(folder -> FolderListSummary.from(folder, blogMaps, unreadCountMap))
                .toList());

        sharedFolderSummary.addAll(folders.get(SharedStatus.SHARED).stream()
                .map(folder -> FolderListSummary.from(folder, blogMaps, unreadCountMap))
                .toList());

        return new Folders(Map.of(
                SharedStatus.SHARED, sharedFolderSummary,
                SharedStatus.PRIVATE, folders.get(SharedStatus.PRIVATE).stream()
                        .map(folder -> FolderListSummary.from(folder, blogMaps, unreadCountMap))
                        .toList()));
    }
}

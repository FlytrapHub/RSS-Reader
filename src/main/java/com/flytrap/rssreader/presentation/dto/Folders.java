package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.folder.SharedStatus;
import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import java.util.List;
import java.util.Map;

public record Folders(Map<SharedStatus, List<FolderListSummary>> folders) {

    public static Folders from(Map<SharedStatus, List<Folder>> folders,
            Map<Long, List<SubscribeEntity>> blogMaps,
            Map<Long, List<Member>> memberMap,
            Map<Long, Long> unreadCountMap) {
        return new Folders(Map.of(
                SharedStatus.SHARED, folders.get(SharedStatus.SHARED).stream()
                        .map(folder -> FolderListSummary.from(folder, blogMaps, memberMap, unreadCountMap))
                        .toList(),
                SharedStatus.PRIVATE, folders.get(SharedStatus.PRIVATE).stream()
                        .map(folder -> FolderListSummary.from(folder, blogMaps, memberMap, unreadCountMap))
                        .toList()
        ));
    }
}

package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.shared.SharedFolder;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import java.util.List;
import java.util.Map;

public record FolderListSummary(long id,
                                String name,
                                int unreadCount,
                                List<SubscribeListSummary> blogs,
                                List<MemberSummary> invitedMembers) {

    public static FolderListSummary from(Folder folder, Map<Long, List<SubscribeEntity>> blogMaps,
            Map<Long, Long> unreadCountMap) {
        if (folder instanceof SharedFolder sharedFolder) {
            return new FolderListSummary(folder.getId(),
                    folder.getName(),
                    unreadCountMap.getOrDefault(folder.getId(), 0L).intValue(),
                    SubscribeListSummary.from(blogMaps.get(folder.getId()), unreadCountMap),
                    sharedFolder.getInvitedMembers().stream()
                            .map(MemberSummary::from)
                            .toList());
        }

        return new FolderListSummary(folder.getId(),
                folder.getName(),
                unreadCountMap.getOrDefault(folder.getId(), 0L).intValue(),
                SubscribeListSummary.from(blogMaps.get(folder.getId()), unreadCountMap),
                List.of());
    }
}

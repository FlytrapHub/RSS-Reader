package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import java.util.List;
import java.util.Map;

public record FolderListSummary(long id,
                                String name,
                                int unreadCount,
                                List<SubscribeListSummary> blogs,
                                List<MemberSummary> invitedMembers) {

    public static FolderListSummary from(Folder folder, Map<Long, List<SubscribeEntity>> blogMaps,
            Map<Long, List<Member>> memberMap, Map<Long, Long> unreadCountMap) {
        return new FolderListSummary(folder.getId(),
                folder.getName(),
                0,
                blogMaps.get(folder.getId()).stream()
                        .map(SubscribeListSummary::from)
                        .toList(),
                memberMap.get(folder.getId()).stream()
                        .map(MemberSummary::from)
                        .toList());
    }
}

package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.folder.FolderSubscribe;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import java.util.List;
import java.util.Map;

public record SubscribeListSummary(long id,
                                   String title,
                                   int unreadCount) {

    public static List<SubscribeListSummary> from(List<FolderSubscribe> subscribes)
    {
        return subscribes.stream()
                .map(e -> new SubscribeListSummary(e.getId(),
                        e.getTitle(),
                        e.getUnreadCount()))
                .toList();
    }
}

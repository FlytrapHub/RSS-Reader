package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import java.util.List;
import java.util.Map;

public record SubscribeListSummary(long id,
                                   //String title,
                                   int unreadCount) {

    public static List<SubscribeListSummary> from(List<SubscribeEntity> subscribeEntities, Map<Long, Long> unreadCountMap) {
        return subscribeEntities.stream()
                .map(e -> new SubscribeListSummary(e.getId(),
                        //e.getTitle(),
                        unreadCountMap.getOrDefault(e.getId(), 0L).intValue()))
                .toList();
    }
}

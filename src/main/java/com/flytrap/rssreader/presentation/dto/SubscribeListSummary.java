package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;

public record SubscribeListSummary(long id,
                                   //String title,
                                   String imageUrl,
                                   int unreadCount) {

    public static SubscribeListSummary from(SubscribeEntity subscribeEntity) {
        return new SubscribeListSummary(subscribeEntity.getId(),
                //subscribeEntity.getTitle(),
                subscribeEntity.getPlatform().name(),
                0);
    }
}

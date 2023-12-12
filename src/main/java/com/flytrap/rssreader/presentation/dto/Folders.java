package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.folder.SharedStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Folders(Map<SharedStatus, List<FolderListSummary>> folders) {

    public static Folders from(List<? extends Folder> unreadCountInSubscribes) {
        Map<SharedStatus, List<Folder>> collect = unreadCountInSubscribes.stream()
                .collect(Collectors.groupingBy(Folder::getSharedStatus));

        Map<SharedStatus, List<FolderListSummary>> result = Map.of(
                SharedStatus.SHARED,
                collect.getOrDefault(SharedStatus.SHARED, new ArrayList<>()).stream()
                        .map(FolderListSummary::from)
                        .collect(Collectors.toList()),
                SharedStatus.PRIVATE,
                collect.getOrDefault(SharedStatus.PRIVATE, new ArrayList<>()).stream()
                        .map(FolderListSummary::from)
                        .collect(Collectors.toList()));

        return new Folders(result);
    }
}

package com.flytrap.rssreader.presentation.facade;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.folder.FolderSubscribe;
import com.flytrap.rssreader.domain.subscribe.Subscribe;
import com.flytrap.rssreader.service.FolderSubscribeService;
import com.flytrap.rssreader.service.SubscribeService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeInFolderFacade {

    private final SubscribeService subscribeService;
    private final FolderSubscribeService folderSubscribeService;

    public List<Folder> addSubscribesInFolder(List<Folder> folders) {
        Map<Folder, List<Long>> folderSubscribeIds =
                folderSubscribeService.getFolderSubscribeIds(folders);

        Set<Long> subscribeIds = folderSubscribeIds.values().stream()
                .flatMap(List::stream).collect(Collectors.toSet());

        Map<Long, Subscribe> subscribes = subscribeService.read(subscribeIds).stream()
                .collect(Collectors.toMap(Subscribe::getId, subscribe -> subscribe));

        folders.forEach(folder -> {
            List<Long> subscribeIdsInFolder = folderSubscribeIds.get(folder);
            subscribeIdsInFolder.stream()
                    .map(subscribes::get)
                    .forEach(e -> folder.addSubscribe(FolderSubscribe.from(e)));
        });
        return folders;
    }
}

package com.flytrap.rssreader.service.folder;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.subscribe.Subscribe;
import com.flytrap.rssreader.infrastructure.entity.folder.FolderSubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.FolderSubscribeEntityJpaRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderSubscribeService {

    private final FolderSubscribeEntityJpaRepository folderSubscribeRepository;

    @Transactional
    public void folderSubscribe(Subscribe subscribe, Long folderId) {
        folderSubscribeRepository.save(FolderSubscribeEntity.from(subscribe, folderId));
    }

    @Transactional
    public void folderUnsubscribe(Long subscribeId, Long folderId) {
        folderSubscribeRepository.deleteBySubscribeIdAndFolderId(subscribeId, folderId);
    }

    @Transactional
    public void unsubscribeAllByFolder(Folder folder) {
        folderSubscribeRepository.deleteAllByFolderId(folder.getId());
    }

    @Transactional(readOnly = true)
    public List<Long> getFolderSubscribeId(Long folderId) {
        return folderSubscribeRepository.findAllByFolderId(folderId)
                .stream()
                .map(FolderSubscribeEntity::getId)
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<Folder, List<Long>> getFolderSubscribeIds(List<? extends Folder> folders) {
        List<Long> folderIds = folders.stream()
                .map(Folder::getId)
                .toList();

        List<FolderSubscribeEntity> folderSubscribes =
                folderSubscribeRepository.findAllByFolderIdIn(folderIds);

        Map<Long, List<Long>> folderSubscribeIds = folderSubscribes.stream()
                .collect(Collectors.groupingBy(FolderSubscribeEntity::getFolderId,
                        Collectors.mapping(FolderSubscribeEntity::getSubscribeId,
                                Collectors.toList())));

        return folders.stream()
                .collect(Collectors.toMap(folder -> folder,
                        folder -> folderSubscribeIds.getOrDefault(folder.getId(), List.of())));
    }
}

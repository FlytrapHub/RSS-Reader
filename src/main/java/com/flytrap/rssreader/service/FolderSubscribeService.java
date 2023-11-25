package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.subscribe.Subscribe;
import com.flytrap.rssreader.infrastructure.entity.folder.FolderSubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.FolderSubscribeEntityJpaRepository;
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
}

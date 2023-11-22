package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.subscribe.Subscribe;
import com.flytrap.rssreader.infrastructure.entity.folder.FolderSubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.FolderSubscribeEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FolderSubscribeService {

    private final FolderSubscribeEntityJpaRepository folderSubscribeRepository;

    public void folderSubscribe(Subscribe subscribe, Long folderId) {
        folderSubscribeRepository.save(FolderSubscribeEntity.from(subscribe, folderId));
    }
}

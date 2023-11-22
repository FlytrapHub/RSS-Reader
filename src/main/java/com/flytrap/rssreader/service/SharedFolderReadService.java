package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.infrastructure.entity.folder.FolderEntity;
import com.flytrap.rssreader.infrastructure.entity.shared.SharedFolderEntity;
import com.flytrap.rssreader.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.SharedFolderJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SharedFolderReadService {

    private SharedFolderJpaRepository sharedFolderJpaRepository;
    private FolderEntityJpaRepository folderEntityJpaRepository;

    @Transactional(readOnly = true)
    public List<Folder> findFoldersInvited(long memberId) {
        List<SharedFolderEntity> shared = sharedFolderJpaRepository.findAllByMemberId(
                memberId);

        List<Long> idList = shared.stream()
                .map(SharedFolderEntity::getFolderId)
                .toList();

        return folderEntityJpaRepository.findAllById(idList).stream()
                .map(FolderEntity::toDomain)
                .toList();
    }
}

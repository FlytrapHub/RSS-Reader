package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.folder.SharedStatus;
import com.flytrap.rssreader.infrastructure.entity.folder.FolderEntity;
import com.flytrap.rssreader.infrastructure.repository.FolderEntityJpaRepository;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderReadService {

    private final FolderEntityJpaRepository repository;

    public Folder findById(Long id) {
        return repository.findByIdAndIsDeletedFalse(id).orElseThrow().toDomain();
    }

    @Transactional(readOnly = true)
    public Map<SharedStatus, Folder> findAllByMemberIdGroupByShared(long memberId) {
        return repository.findAllByMemberIdAndIsDeletedFalse(memberId).stream()
                .map(FolderEntity::toDomain)
                .collect(Collectors.toMap(Folder::getSharedStatus, folder -> folder));
    }

}

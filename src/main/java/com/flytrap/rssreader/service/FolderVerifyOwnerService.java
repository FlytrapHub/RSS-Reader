package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.global.exception.NoSuchDomainException;
import com.flytrap.rssreader.global.exception.NotBelongToMemberException;
import com.flytrap.rssreader.infrastructure.entity.folder.FolderEntity;
import com.flytrap.rssreader.infrastructure.repository.FolderEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderVerifyOwnerService {

    private final FolderEntityJpaRepository repository;

    @Transactional(readOnly = true)
    public Folder getVerifiedFolder(Long folderId, long memberId) {
        FolderEntity folderEntity = repository.findByIdAndIsDeletedFalse(folderId)
                .orElseThrow(() -> new NoSuchDomainException(Folder.class));
        verifyOwnership(memberId, folderEntity);
        return folderEntity.toDomain();
    }

    private void verifyOwnership(long memberId, FolderEntity folderEntity) {
        if (folderEntity.getMemberId() != memberId) {
            throw new NotBelongToMemberException(folderEntity.toDomain());
        }
    }
}

package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.global.exception.ForbiddenAccessFolderException;
import com.flytrap.rssreader.global.exception.NoSuchDomainException;
import com.flytrap.rssreader.global.exception.NotBelongToMemberException;
import com.flytrap.rssreader.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.SharedFolderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderVerifyService {

    private final FolderEntityJpaRepository repository;
    private final SharedFolderJpaRepository sharedFolderJpaRepository;

    @Transactional(readOnly = true)
    public Folder getVerifiedOwnedFolder(Long folderId, long memberId) {
        Folder folder = repository.findByIdAndIsDeletedFalse(folderId)
                .orElseThrow(() -> new NoSuchDomainException(Folder.class)).toDomain();

        if (!folder.isOwner(memberId))
            throw new NotBelongToMemberException(folder);

        return folder;
    }

    @Transactional(readOnly = true)
    public Folder getVerifiedAccessableFolder(Long folderId, long memberId) {
        Folder folder = repository.findByIdAndIsDeletedFalse(folderId)
                .orElseThrow(() -> new NoSuchDomainException(Folder.class)).toDomain();

        if (!folder.isOwner(memberId) && !isSharedFolder(folderId, memberId)) {
            throw new ForbiddenAccessFolderException(folder);
        }
        return folder;
    }

    private boolean isSharedFolder(Long folderId, long memberId) {
        return sharedFolderJpaRepository.existsByFolderIdAndMemberId(folderId, memberId);
    }

}

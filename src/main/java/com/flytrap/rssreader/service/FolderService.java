package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.folder.SharedStatus;
import com.flytrap.rssreader.global.exception.NoSuchDomainException;
import com.flytrap.rssreader.global.exception.NotBelongToMemberException;
import com.flytrap.rssreader.infrastructure.entity.folder.FolderEntity;
import com.flytrap.rssreader.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.presentation.dto.FolderRequest.CreateRequest;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderEntityJpaRepository repository;

    public Folder createNewFolder(@Valid CreateRequest request, long member) {
        Folder folder = Folder.create(request.name(), member);
        return repository.save(FolderEntity.from(folder)).toDomain();
    }

    @Transactional
    public Folder updateFolder(CreateRequest request, long folderId, long memberId) {
        FolderEntity folderEntity = repository.findByIdAndIsDeletedFalse(folderId)
                .orElseThrow(() -> new NoSuchDomainException(Folder.class));
        verifyBelongTo(memberId, folderEntity);

        Folder folder = folderEntity.toDomain();
        folder.updateName(request.name());

        return repository.save(FolderEntity.from(folder)).toDomain();
    }

    private void verifyBelongTo(long memberId, FolderEntity folderEntity) {
        if (folderEntity.getMemberId() != memberId) {
            throw new NotBelongToMemberException(folderEntity.toDomain());
        }
    }

    @Transactional
    public Folder deleteFolder(Long folderId, long id) {
        FolderEntity folderEntity = repository.findByIdAndIsDeletedFalse(folderId)
                .orElseThrow(() -> new NoSuchDomainException(Folder.class));
        verifyBelongTo(id, folderEntity);

        Folder folder = folderEntity.toDomain();
        folder.delete();

        return repository.save(FolderEntity.from(folder)).toDomain();
    }

    @Transactional(readOnly = true)
    public Map<SharedStatus, Folder> findAllByMemberIdGroupByShared(long memberId) {
        return repository.findAllByMemberIdAndIsDeletedFalse(memberId).stream()
                .map(FolderEntity::toDomain)
                .collect(Collectors.toMap(Folder::getSharedStatus, folder -> folder));
    }

}

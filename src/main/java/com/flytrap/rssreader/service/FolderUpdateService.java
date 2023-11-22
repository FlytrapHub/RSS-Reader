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
public class FolderUpdateService {

    private final FolderEntityJpaRepository repository;
    private final FolderVerifyOwnerService folderVerifyOwnerService;

    public Folder createNewFolder(@Valid CreateRequest request, long member) {
        Folder folder = Folder.create(request.name(), member);
        return repository.save(FolderEntity.from(folder)).toDomain();
    }

    @Transactional
    public Folder updateFolder(CreateRequest request, long folderId, long memberId) {
        Folder folder = folderVerifyOwnerService.getVerifiedFolder(folderId, memberId);
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
        Folder folder = folderVerifyOwnerService.getVerifiedFolder(folderId, id);
        folder.delete();

        return repository.save(FolderEntity.from(folder)).toDomain();
    }

    public void shareFolder(Folder folder) {

        if (!folder.isShared()) {
            folder.toShare();
            repository.save(FolderEntity.from(folder));
        }

    }
}

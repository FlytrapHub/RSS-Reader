package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.global.exception.NoSuchDomainException;
import com.flytrap.rssreader.global.exception.NotBelongToMemberException;
import com.flytrap.rssreader.infrastructure.entity.folder.FolderEntity;
import com.flytrap.rssreader.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.presentation.dto.FolderRequest.CreateRequest;
import jakarta.validation.Valid;
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
        FolderEntity folderEntity = repository.findById(folderId)
                .orElseThrow(() -> new NoSuchDomainException(Folder.class));
        verifyBelongTo(memberId, folderEntity);

        Folder folder = folderEntity.toDomain();
        folder.updateName(request.name());

        repository.save(FolderEntity.from(folder));

        return folder;
    }

    private void verifyBelongTo(long memberId, FolderEntity folderEntity) {
        if (folderEntity.getMemberId() != memberId) {
            throw new NotBelongToMemberException(folderEntity.toDomain());
        }
    }
}

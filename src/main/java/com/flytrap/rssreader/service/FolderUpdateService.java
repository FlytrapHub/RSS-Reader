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

    public Folder createNewFolder(@Valid CreateRequest request, long member) {
        Folder folder = Folder.create(request.name(), member);
        return repository.save(FolderEntity.from(folder)).toDomain();
    }

    @Transactional
    public Folder updateFolder(CreateRequest request, Folder folder, long memberId) {
        folder.updateName(request.name());

        return repository.save(FolderEntity.from(folder)).toDomain();
    }

    @Transactional
    public Folder deleteFolder(Folder folder, long id) {
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

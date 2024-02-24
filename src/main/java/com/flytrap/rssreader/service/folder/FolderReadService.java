package com.flytrap.rssreader.service.folder;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.folder.SharedStatus;
import com.flytrap.rssreader.infrastructure.entity.folder.FolderEntity;
import com.flytrap.rssreader.infrastructure.repository.FolderEntityJpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Deprecated
@Service
@RequiredArgsConstructor
public class FolderReadService {

    private final FolderEntityJpaRepository repository;

    @Transactional(readOnly = true)
    public Folder findById(long id) {
        return repository.findByIdAndIsDeletedFalse(id).orElseThrow().toDomain();
    }

    @Transactional(readOnly = true)
    public List<Folder> findAllByMemberId(long memberId) {
        return repository.findAllByMemberIdAndIsDeletedFalse(memberId).stream()
                .map(FolderEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Folder> findAllByIds(Collection<Long> ids) {
        return repository.findAllById(ids).stream()
                .map(FolderEntity::toDomain)
                .toList();
    }
}

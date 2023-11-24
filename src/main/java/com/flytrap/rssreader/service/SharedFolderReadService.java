package com.flytrap.rssreader.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.infrastructure.entity.folder.FolderEntity;
import com.flytrap.rssreader.infrastructure.entity.shared.SharedFolderEntity;
import com.flytrap.rssreader.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.SharedFolderJpaRepository;
import java.util.List;
import java.util.Map;
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

    @Transactional(readOnly = true)
    public Map<Long, List<Member>> findAllInvited(long id) {
        List<SharedFolderEntity> sharedFolderEntities =
                sharedFolderJpaRepository.findAllByMemberId(id);

        return sharedFolderEntities.stream()
                .collect(groupingBy(SharedFolderEntity::getFolderId,
                        mapping(e -> e.getMember().toDomain(), toList())));
    }

    @Transactional(readOnly = true)
    public Folder findFolderInvited(long folderId, long memberId) {

        if (!sharedFolderJpaRepository.existsByFolderIdAndMemberId(folderId, memberId)) {
            throw new IllegalArgumentException("공유된 폴더가 아닙니다.");
        }

        return folderEntityJpaRepository.findById(folderId)
                .map(FolderEntity::toDomain)
                .orElseThrow();
    }
}

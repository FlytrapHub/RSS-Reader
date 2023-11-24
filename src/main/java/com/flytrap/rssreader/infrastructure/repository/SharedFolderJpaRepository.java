package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.shared.SharedFolderEntity;
import io.lettuce.core.ScanIterator;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedFolderJpaRepository extends JpaRepository<SharedFolderEntity, Long> {

    List<SharedFolderEntity> findAllByFolderId(long folderId);

    boolean existsByFolderIdAndMemberId(long folderId, long memberId);

    Optional<SharedFolderEntity> findByFolderIdAndMemberId(long folderId, long memberId);

    List<SharedFolderEntity> findAllByMemberId(long memberId);
    List<SharedFolderEntity> findAllByFolderIdIn(List<Long> folderIds);

}

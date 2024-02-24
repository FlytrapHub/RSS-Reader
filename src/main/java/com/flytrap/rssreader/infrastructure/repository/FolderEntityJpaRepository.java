package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.folder.FolderEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@Deprecated
public interface FolderEntityJpaRepository extends JpaRepository<FolderEntity, Long> {

    List<FolderEntity> findAllByIsDeletedFalse();
    Optional<FolderEntity> findByIdAndIsDeletedFalse(Long id);
    List<FolderEntity> findAllByMemberIdAndIsDeletedFalse(Long memberId);
}

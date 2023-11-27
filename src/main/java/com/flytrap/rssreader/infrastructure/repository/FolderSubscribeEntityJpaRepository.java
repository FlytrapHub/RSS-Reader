package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.folder.FolderSubscribeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FolderSubscribeEntityJpaRepository extends
        JpaRepository<FolderSubscribeEntity, Long> {

    @Modifying
    @Query("DELETE FROM FolderSubscribeEntity e WHERE e.subscribeId = :subscribeId AND e.folderId = :folderId")
    void deleteBySubscribeIdAndFolderId(@Param("subscribeId") Long subscribeId,
            @Param("folderId") Long folderId);

}

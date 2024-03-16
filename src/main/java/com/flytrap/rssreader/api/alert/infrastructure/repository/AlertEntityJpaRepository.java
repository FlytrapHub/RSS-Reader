package com.flytrap.rssreader.api.alert.infrastructure.repository;

import com.flytrap.rssreader.api.alert.infrastructure.entity.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlertEntityJpaRepository extends JpaRepository<AlertEntity, Long> {

    Optional<AlertEntity> findByFolderIdAndMemberId(Long folderId, Long memberId);

    @Query("SELECT alert FROM AlertEntity alert " +
            "JOIN FolderEntity f ON alert.folderId = f.id " +
            "JOIN FolderSubscribeEntity fs ON fs.folderId = f.id " +
            "WHERE fs.subscribeId = :subscribeId")
    List<AlertEntity> findAlertsBySubscribeId(@Param("subscribeId") Long subscribeId);
}
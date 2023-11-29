package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.alert.AlertEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertEntityJpaRepository extends JpaRepository<AlertEntity, Long> {

    Optional<AlertEntity> findByFolderIdAndMemberId(Long folderId, Long memberId);
}

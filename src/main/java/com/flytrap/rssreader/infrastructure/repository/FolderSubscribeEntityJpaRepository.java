package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.folder.FolderSubscribeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderSubscribeEntityJpaRepository extends
        JpaRepository<FolderSubscribeEntity, Long> {

}

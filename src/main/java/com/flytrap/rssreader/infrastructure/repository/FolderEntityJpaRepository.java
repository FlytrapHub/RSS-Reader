package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.folder.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderEntityJpaRepository extends JpaRepository<FolderEntity, Long> {

}

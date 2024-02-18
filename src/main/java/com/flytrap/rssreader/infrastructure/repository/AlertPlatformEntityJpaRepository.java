package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.alert.AlertPlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertPlatformEntityJpaRepository extends JpaRepository<AlertPlatformEntity, Long> {

}

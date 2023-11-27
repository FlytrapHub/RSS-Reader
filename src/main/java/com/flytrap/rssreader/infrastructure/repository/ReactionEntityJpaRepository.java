package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.reaction.ReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionEntityJpaRepository extends JpaRepository<ReactionEntity, Long> {

}

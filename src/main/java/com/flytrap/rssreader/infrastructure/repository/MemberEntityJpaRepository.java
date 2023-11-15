package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.member.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEntityJpaRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByOauthPk(long oauthPk);
}

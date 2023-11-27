package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.member.MemberEntity;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.reaction.ReactionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionEntityJpaRepository extends JpaRepository<ReactionEntity, Long> {

    Optional<ReactionEntity> findByMemberAndPost(MemberEntity member, PostEntity post);
}

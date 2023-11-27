package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.post.OpenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostOpenRepository extends JpaRepository<OpenEntity, Long> {

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}

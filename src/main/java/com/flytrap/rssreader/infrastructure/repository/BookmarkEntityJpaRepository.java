package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.bookmark.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkEntityJpaRepository extends JpaRepository<BookmarkEntity, Long> {
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    void deleteAllByMemberIdAndPostId(Long memberId, Long postId);
}

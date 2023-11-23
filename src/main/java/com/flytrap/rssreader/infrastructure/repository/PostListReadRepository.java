package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import com.querydsl.core.BooleanBuilder;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PostListReadRepository {
    List<PostEntity> findAllBySubscribe(long subscribeId, PostFilter postFilter, Pageable pageable);

    List<PostEntity> findAllByFolder(long folderId, PostFilter postFilter, Pageable pageable);
}

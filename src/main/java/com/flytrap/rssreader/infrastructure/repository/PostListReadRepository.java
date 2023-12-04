package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.repository.output.PostOutput;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PostListReadRepository {
    List<PostOutput> findAllBySubscribe(long subscribeId, PostFilter postFilter, Pageable pageable);

    List<PostOutput> findAllByFolder(long folderId, PostFilter postFilter, Pageable pageable);

    List<PostOutput> findAllByMember(long memberId, PostFilter postFilter, Pageable pageable);

    List<PostEntity> findAllBookmarks(long memberId, PostFilter postFilter, Pageable pageable);
}

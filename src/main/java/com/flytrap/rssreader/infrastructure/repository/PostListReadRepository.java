package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.repository.output.PostOutput;
import com.flytrap.rssreader.infrastructure.repository.output.PostPaginationOutput;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostListReadRepository {

    Optional<PostOutput> findById(Long postId);

    List<PostOutput> findAllBySubscribe(long memberId, long subscribeId, PostFilter postFilter,
            Pageable pageable);

    List<PostOutput> findAllByFolder(long memberId, long folderId, PostFilter postFilter,
            Pageable pageable);

    List<PostOutput> findAllByMember(long memberId, PostFilter postFilter, Pageable pageable);

    List<PostOutput> findAllBookmarks(long memberId, PostFilter postFilter, Pageable pageable);

    Slice<PostPaginationOutput> findAllNoOffsetByMember(long memberId, PostFilter postFilter, String cursor,
            int size);
}

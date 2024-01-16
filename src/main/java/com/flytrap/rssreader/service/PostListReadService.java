package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.post.Post;
import com.flytrap.rssreader.global.utill.pagination.PageResponse;
import com.flytrap.rssreader.global.utill.pagination.PagingUtils;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.PostListReadRepository;
import com.flytrap.rssreader.infrastructure.repository.output.PostOutput;
import com.flytrap.rssreader.infrastructure.repository.output.PostPaginationOutput;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import com.flytrap.rssreader.presentation.dto.PostResponse;
import com.flytrap.rssreader.presentation.dto.PostResponse.PostListResponse;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@AllArgsConstructor
@Service
public class PostListReadService {

    private final PostListReadRepository postListReadRepository;
    private final PostEntityJpaRepository postRepository;

    public List<Post> getPostsBySubscribe(SessionMember member, Long subscribeId,
            PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllBySubscribe(member.id(), subscribeId, postFilter,
                        pageable)
                .stream()
                .map(PostOutput::toDomain)
                .toList();
    }

    public List<Post> getPostsByFolder(SessionMember member, Folder folder, PostFilter postFilter,
            Pageable pageable) {
        return postListReadRepository.findAllByFolder(member.id(), folder.getId(), postFilter,
                        pageable)
                .stream()
                .map(PostOutput::toDomain)
                .toList();
    }

    public PageResponse<PostListResponse> getPostsByMember(String cursor,
            int size, PostFilter postFilter, SessionMember member) {

        //cursor가 psot id
        Slice<PostPaginationOutput> slice = postListReadRepository.findAllNoOffsetByMember(
                member.id(), postFilter, cursor, size);

        //list마지막 페이지커서
        String nextCursor = PagingUtils.setNextCursor(slice.getContent(), slice.hasNext());

        List<Post> posts = slice.stream()
                .map(PostPaginationOutput::toDomain)
                .toList();

        return new PageResponse<>(
                new PostListResponse(posts.stream()
                        .map(PostResponse::from)
                        .toList()),
                nextCursor,
                slice.hasNext());
    }

    public List<Post> getPostsByMemberV1(SessionMember member, PostFilter postFilter,
            Pageable pageable) {

        return postListReadRepository.findAllByMember(member.id(), postFilter, pageable)
                .stream()
                .map(PostOutput::toDomain)
                .toList();
    }
}

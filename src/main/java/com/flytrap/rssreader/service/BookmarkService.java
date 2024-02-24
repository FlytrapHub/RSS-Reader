package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.bookmark.Bookmark;
import com.flytrap.rssreader.domain.post.Post;
import com.flytrap.rssreader.global.exception.DuplicateDomainException;
import com.flytrap.rssreader.infrastructure.entity.bookmark.BookmarkEntity;
import com.flytrap.rssreader.infrastructure.repository.BookmarkEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.PostListReadRepository;
import com.flytrap.rssreader.infrastructure.repository.output.PostOutput;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor
@Service
public class BookmarkService {
    // 최상위에 도메인이 있다고 하셨죠
    // global 에서는 바꿀 거 없나요?

    private final PostListReadRepository postListReadRepository;
    private final BookmarkEntityJpaRepository bookmarkRepository;

    public List<Post> getBookmarks(SessionMember member, PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllBookmarks(member.id(), postFilter, pageable)
            .stream()
            .map(PostOutput::toDomain)
            .toList();
    }

    public Bookmark addBookmark(SessionMember member, Post post) {

        if (existBookmark(member, post)) {
            throw new DuplicateDomainException(Bookmark.class);
        }

        return bookmarkRepository.save(BookmarkEntity.create(member.id(), post.getId())).toDomain();
    }

    public void removeBookmark(SessionMember member, Long postId) {

        bookmarkRepository.deleteAllByMemberIdAndPostId(member.id(), postId);
    }

    public boolean existBookmark(SessionMember member, Post post) {
        return bookmarkRepository.existsByMemberIdAndPostId(member.id(), post.getId());
    }

}

package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.post.Post;
import com.flytrap.rssreader.infrastructure.repository.PostListReadRepository;
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

    private final PostListReadRepository postListReadRepository;

    public List<Post> getBookmarks(SessionMember member, PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllBookmarks(member.id(), postFilter, pageable)
            .stream()
            .map(p -> p.toDomain(member.id()))
            .toList();
    }

}

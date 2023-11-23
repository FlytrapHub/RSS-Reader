package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.post.Post;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.repository.PostListReadRepository;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PostListReadService {

    private final PostListReadRepository postListReadRepository;

    public List<Post> getPosts(Long subscribeId, PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllBySubscribe(subscribeId, postFilter, pageable)
            .stream()
            .map(PostEntity::toDomain)
            .toList();
    }
}

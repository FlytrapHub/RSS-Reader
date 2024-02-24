package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.post.Post;
import com.flytrap.rssreader.global.exception.NoSuchDomainException;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Deprecated
public class PostService {

    private final PostEntityJpaRepository postRepository;

    public Post findById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new NoSuchDomainException(Post.class))
            .toDomain();
    }

}

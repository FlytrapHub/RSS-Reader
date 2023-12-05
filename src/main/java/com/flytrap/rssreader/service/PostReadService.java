package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.post.Post;
import com.flytrap.rssreader.domain.post.PostOpenEvent;
import com.flytrap.rssreader.global.event.PublishEvent;
import com.flytrap.rssreader.global.exception.NoSuchDomainException;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.PostListReadRepository;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostReadService {

    private final PostEntityJpaRepository postEntityJpaRepository;
    private final PostListReadRepository postListReadRepository;

    @Transactional(readOnly = true)
    @PublishEvent(eventType = PostOpenEvent.class,
            params = "#{T(com.flytrap.rssreader.service.dto.PostOpenParam).create(#sessionMember.id(), #postId)}")
    public Post getPost(SessionMember sessionMember, Long postId) {

        return postListReadRepository.findById(postId)
            .orElseThrow(() -> new NoSuchDomainException(Post.class))
            .toDomain();
    }

    @Transactional(readOnly = true)
    public Map<Long, Integer> countPosts(List<Long> subscribes) {
        return postEntityJpaRepository.countsGroupBySubscribeId(subscribes);
    }
}

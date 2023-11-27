package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.post.Post;
import com.flytrap.rssreader.domain.post.PostOpenEvent;
import com.flytrap.rssreader.global.event.PublishEvent;
import com.flytrap.rssreader.global.exception.NoSuchDomainException;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostReadService {

    private final PostEntityJpaRepository postEntityJpaRepository;

    @Transactional(readOnly = true)
    @PublishEvent(eventType = PostOpenEvent.class, params = "#member.id(), #postId")
    public Post getPost(SessionMember member, Long postId) {

        PostEntity post = postEntityJpaRepository.findById(postId)
                .orElseThrow(() -> new NoSuchDomainException(Post.class));

        return post.toDomain(member.id());
    }


}

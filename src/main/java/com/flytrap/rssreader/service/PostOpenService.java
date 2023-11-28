package com.flytrap.rssreader.service;

import com.flytrap.rssreader.infrastructure.entity.post.OpenEntity;
import com.flytrap.rssreader.infrastructure.repository.PostOpenRepository;
import com.flytrap.rssreader.service.dto.PostOpenParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostOpenService {

    private final PostOpenRepository postOpenRepository;

    @Transactional
    public void open(PostOpenParam value) {

        if (postOpenRepository.existsByMemberIdAndPostId(value.memberId(), value.postId())) {
            return;
        }

        postOpenRepository.save(value.toEntity());
    }
}

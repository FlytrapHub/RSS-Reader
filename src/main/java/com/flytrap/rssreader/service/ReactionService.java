package com.flytrap.rssreader.service;

import com.flytrap.rssreader.infrastructure.entity.member.MemberEntity;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.reaction.ReactionEntity;
import com.flytrap.rssreader.infrastructure.repository.MemberEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.ReactionEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final PostEntityJpaRepository postRepository;
    private final MemberEntityJpaRepository memberRepository;
    private final ReactionEntityJpaRepository reactionRepository;

    @Transactional
    public Long addReaction(Long postId, Long memberId, String emoji) {

        PostEntity post = postRepository.findById(postId).orElseThrow();
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();
        return reactionRepository.save(ReactionEntity.create(post, member, emoji)).getId();
    }
}

package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.infrastructure.api.dto.UserResource;
import com.flytrap.rssreader.infrastructure.entity.member.MemberEntity;
import com.flytrap.rssreader.infrastructure.repository.MemberEntityJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberEntityJpaRepository memberEntityJpaRepository;

    /**
     * User resource가 Member 테이블에 존재하지 않으면 새로 가입 후 Member 정보를 반환합니다.
     * @param userResource
     * @return
     */
    @Transactional
    public Member loginMember(UserResource userResource) {

        return memberEntityJpaRepository.findByOauthPk(userResource.id())
            .orElseGet(() -> joinMember(userResource.toDomainForCreate()))
            .toDomain();
    }

    private MemberEntity joinMember(Member member) {
        return memberEntityJpaRepository.save(MemberEntity.from(member));
    }
}

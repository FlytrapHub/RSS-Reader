package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.infrastructure.api.dto.UserResource;
import com.flytrap.rssreader.infrastructure.entity.member.MemberEntity;
import com.flytrap.rssreader.infrastructure.repository.MemberEntityJpaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberEntityJpaRepository memberEntityJpaRepository;

    /**
     * Member 정보를 반환합니다.
     * User resource가 Member 테이블에 존재하지 않으면 새로 가입 후 Member 정보를 반환합니다.
     * @param userResource
     * @return Member domain
     */
    @Transactional
    public Member loginMember(UserResource userResource) {

        return memberEntityJpaRepository.findByOauthPk(userResource.getId())
            .orElseGet(() -> joinMember(userResource.toDomainForCreate()))
            .toDomain();
    }

    /**
     * Member 테이블에 새로운 회원을 가입시킵니다.
     * @param member
     * @return
     */
    private MemberEntity joinMember(Member member) {
        return memberEntityJpaRepository.save(MemberEntity.from(member));
    }

    /**
     * 이름으로 회원을 검색합니다.
     * @param name
     * @return Member domain list
     */
    public List<Member> findByName(String name) {
        return memberEntityJpaRepository.findAllByName(name).stream()
                .map(MemberEntity::toDomain)
                .toList();
    }
}

package com.flytrap.rssreader.infrastructure.entity.member;

import com.flytrap.rssreader.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String email;

    @Column(length = 2500, nullable = false)
    private String profile;

    @Column(unique = true, nullable = false)
    private Long oauthPk;

    @Enumerated(EnumType.STRING)
    private OauthServer oauthServer;

    @CreatedDate
    private Instant createdAt;

    @Builder
    protected MemberEntity(Long id, String name, String email, String profile, Long oauthPk,
        OauthServer oauthServer) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.oauthPk = oauthPk;
        this.oauthServer = oauthServer;
    }

    public Member toDomain() {
        return Member.of(this.id, this.name, this.email, this.profile, this.oauthPk, this.oauthServer, this.createdAt);
    }

    public static MemberEntity from(Member member) {
        return MemberEntity.builder()
            .id(member.getId())
            .name(member.getName())
            .email(member.getEmail())
            .profile(member.getProfile())
            .oauthPk(member.getOauthPk())
            .oauthServer(member.getOauthServer())
            .build();
    }
}

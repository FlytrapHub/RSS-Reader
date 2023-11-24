package com.flytrap.rssreader.fixture;

import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.fixture.FixtureFields.MemberEntityFields;
import com.flytrap.rssreader.fixture.FixtureFields.MemberFields;
import com.flytrap.rssreader.fixture.FixtureFields.PostEntityFields;
import com.flytrap.rssreader.fixture.FixtureFields.RssItemResourceFields;
import com.flytrap.rssreader.fixture.FixtureFields.SubscribeEntityFields;
import com.flytrap.rssreader.fixture.FixtureFields.UserResourceFields;
import com.flytrap.rssreader.infrastructure.api.dto.RssItemResource;
import com.flytrap.rssreader.infrastructure.api.dto.UserResource;
import com.flytrap.rssreader.infrastructure.entity.member.MemberEntity;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import java.util.ArrayList;
import java.util.List;

public class FixtureFactory {

    // Member
    public static UserResource generateUserResource() {
        return UserResource.builder()
            .id(UserResourceFields.id)
            .email(UserResourceFields.email)
            .login(UserResourceFields.login)
            .avatarUrl(UserResourceFields.avatarUrl)
            .build();
    }

    public static MemberEntity generateMemberEntity() {
        return MemberEntity.builder()
            .id(MemberEntityFields.id)
            .email(MemberEntityFields.email)
            .name(MemberEntityFields.name)
            .profile(MemberEntityFields.profile)
            .oauthPk(MemberEntityFields.oauthPk)
            .oauthServer(MemberEntityFields.oauthServer)
            .build();
    }

    public static Member generateMember() {
        return Member.builder()
            .id(MemberFields.id)
            .name(MemberFields.name)
            .email(MemberFields.email)
            .profile(MemberFields.profile)
            .oauthPk(MemberFields.oauthPk)
            .oauthServer(MemberFields.oauthServer)
            .build();
    }

    public static Member generateAnotherMember() {
        return Member.builder()
            .id(MemberFields.anotherId)
            .name(MemberFields.anotherName)
            .email(MemberFields.anotherEmail)
            .profile(MemberFields.anotherProfile)
            .oauthPk(MemberFields.anotherOauthPk)
            .oauthServer(MemberFields.anotherOauthServer)
            .build();
    }

    // Post
    public static RssItemResource generateRssItemResource() {
        return new RssItemResource(
            RssItemResourceFields.guid,
            RssItemResourceFields.title,
            RssItemResourceFields.description,
            RssItemResourceFields.pubDate
        );
    }

    public static List<RssItemResource> generate50RssItemResourceList() {
        List<RssItemResource> rssItemResources = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            rssItemResources.add(generateRssItemResource());
        }
        return rssItemResources;
    }

    public static PostEntity generatePostEntity(Long id) {
        return PostEntity.builder()
            .id(id)
            .guid(PostEntityFields.guid)
            .title(PostEntityFields.title)
            .description(PostEntityFields.description)
            .subscribe(PostEntityFields.subscribe)
            .build();
    }

    public static List<PostEntity> generate100PostEntityList() {
        List<PostEntity> postEntities = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            postEntities.add(generatePostEntity((long) i));
        }

        return postEntities;
    }

    // Subscribe
    public static SubscribeEntity generateSubscribeEntity() {
        return SubscribeEntity.builder()
            .id(SubscribeEntityFields.id)
            .url(SubscribeEntityFields.url)
            .platform(SubscribeEntityFields.platform)
            .build();
    }

    public static List<SubscribeEntity> generateSingleSubscribeEntityList() {
        return List.of(generateSubscribeEntity());
    }


}

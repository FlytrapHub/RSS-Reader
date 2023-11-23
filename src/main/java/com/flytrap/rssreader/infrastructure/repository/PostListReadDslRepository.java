package com.flytrap.rssreader.infrastructure.repository;

import static com.flytrap.rssreader.infrastructure.entity.post.QOpenEntity.openEntity;
import static com.flytrap.rssreader.infrastructure.entity.post.QPostEntity.postEntity;

import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class PostListReadDslRepository implements PostListReadRepository {

    private final JPAQueryFactory queryFactory;

    public PostListReadDslRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<PostEntity> findAllBySubscribe(long subscribeId, PostFilter postFilter, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(postEntity.subscribe.id.eq(subscribeId));

        if (StringUtils.hasText(postFilter.keyword())) {
            builder
                .and(postEntity.title.contains(postFilter.keyword()))
                .and(postEntity.description.contains(postFilter.keyword()));
        }

        if (postFilter.start() != null && postFilter.end() != null) {
            builder
                .and(postEntity.pubDate.between(
                    Instant.ofEpochMilli(postFilter.start()),
                    Instant.ofEpochMilli(postFilter.end()))
                );
        }

        if (postFilter.read() != null && postFilter.read()) {
            builder.and(openEntity.isNotNull());
        } else if (postFilter.read() != null) {
            builder.and(openEntity.isNull());
        }

        return queryFactory.selectFrom(postEntity)
            .leftJoin(openEntity).on(postEntity.id.eq(openEntity.postId))
            .where(builder)
            .orderBy(postEntity.pubDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }
}

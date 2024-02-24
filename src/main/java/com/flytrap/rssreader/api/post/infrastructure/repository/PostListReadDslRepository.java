package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.repository.output.PostOutput;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.flytrap.rssreader.infrastructure.entity.bookmark.QBookmarkEntity.bookmarkEntity;
import static com.flytrap.rssreader.infrastructure.entity.folder.QFolderEntity.folderEntity;
import static com.flytrap.rssreader.infrastructure.entity.folder.QFolderSubscribeEntity.folderSubscribeEntity;
import static com.flytrap.rssreader.infrastructure.entity.post.QOpenEntity.openEntity;
import static com.flytrap.rssreader.infrastructure.entity.post.QPostEntity.postEntity;
import static com.flytrap.rssreader.infrastructure.entity.shared.QSharedFolderEntity.sharedFolderEntity;
import static com.flytrap.rssreader.infrastructure.entity.subscribe.QSubscribeEntity.subscribeEntity;

@Repository
public class PostListReadDslRepository implements PostListReadRepository {

    private final JPAQueryFactory queryFactory;

    public PostListReadDslRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Optional<PostOutput> findById(Long postId) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
                .and(postEntity.id.eq(postId));

        return Optional.ofNullable(initFindAllQuery()
                .where(builder)
                .fetchOne());
    }

    public List<PostOutput> findAllBySubscribe(long memberId, long subscribeId, PostFilter postFilter, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
                .and(postEntity.subscribe.id.eq(subscribeId));

        addFilterCondition(builder, postFilter, memberId);

        return initFindAllQuery()
                .where(builder)
                .orderBy(postEntity.pubDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<PostOutput> findAllByFolder(long memberId, long folderId, PostFilter postFilter, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
                .and(folderSubscribeEntity.folderId.eq(folderId));

        addFilterCondition(builder, postFilter, memberId);

        return initFindAllQuery()
                .join(subscribeEntity).on(postEntity.subscribe.id.eq(subscribeEntity.id))
                .join(folderSubscribeEntity).on(subscribeEntity.id.eq(folderSubscribeEntity.subscribeId))
                .where(builder)
                .orderBy(postEntity.pubDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<PostOutput> findAllByMember(long memberId, PostFilter postFilter, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
                .and(folderEntity.isDeleted.eq(false))
                .and(folderEntity.memberId.eq(memberId))
                .or(sharedFolderEntity.memberId.eq(memberId));

        addFilterCondition(builder, postFilter, memberId);

        return initFindAllQuery()
                .join(subscribeEntity).on(postEntity.subscribe.id.eq(subscribeEntity.id))
                .join(folderSubscribeEntity).on(subscribeEntity.id.eq(folderSubscribeEntity.subscribeId))
                .join(folderEntity).on(folderSubscribeEntity.folderId.eq(folderEntity.id))
                .leftJoin(sharedFolderEntity).on(folderEntity.id.eq(sharedFolderEntity.folderId))
                .where(builder)
                .orderBy(postEntity.pubDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<PostOutput> findAllBookmarks(long memberId, PostFilter postFilter, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
                .and(bookmarkEntity.memberId.eq(memberId));

        addFilterCondition(builder, postFilter, memberId);

        return initFindAllQuery()
                .where(builder)
                .orderBy(postEntity.pubDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private JPAQuery<PostOutput> initFindAllQuery() {
        return queryFactory
                .selectDistinct(
                        Projections.constructor(PostOutput.class,
                                postEntity.id,
                                postEntity.subscribe.id,
                                postEntity.guid,
                                postEntity.title,
                                postEntity.thumbnailUrl,
                                postEntity.description,
                                postEntity.pubDate,
                                postEntity.subscribe.title,
                                Expressions.booleanTemplate("{0} is not null", openEntity.id),
                                Expressions.booleanTemplate("{0} is not null", bookmarkEntity.id)
                        )
                )
                .from(postEntity)
                .leftJoin(openEntity).on(postEntity.id.eq(openEntity.postId))
                .leftJoin(bookmarkEntity).on(postEntity.id.eq(bookmarkEntity.postId));
    }

    private void addFilterCondition(BooleanBuilder builder, PostFilter postFilter, long memberId) {

        builder
                .and(openEntity.memberId.eq(memberId).or(openEntity.memberId.isNull()))
                .and(bookmarkEntity.memberId.eq(memberId).or(bookmarkEntity.memberId.isNull()));

        if (postFilter.hasKeyword()) {
            builder
                    .and(postEntity.title.contains(postFilter.keyword()))
                    .and(postEntity.description.contains(postFilter.keyword()));
        }

        if (postFilter.hasDataRange()) {
            builder
                    .and(postEntity.pubDate.between(
                            Instant.ofEpochMilli(postFilter.start()),
                            Instant.ofEpochMilli(postFilter.end()))
                    );
        }

        if (postFilter.hasReadCondition()) {
            builder.and(openEntity.isNotNull());
        } else if (postFilter.hasUnReadCondition()) {
            builder.and(openEntity.isNull());
        }
    }
}

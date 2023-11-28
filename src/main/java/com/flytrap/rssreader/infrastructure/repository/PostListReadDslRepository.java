package com.flytrap.rssreader.infrastructure.repository;


import static com.flytrap.rssreader.infrastructure.entity.bookmark.QBookmarkEntity.bookmarkEntity;
import static com.flytrap.rssreader.infrastructure.entity.folder.QFolderEntity.folderEntity;
import static com.flytrap.rssreader.infrastructure.entity.folder.QFolderSubscribeEntity.folderSubscribeEntity;
import static com.flytrap.rssreader.infrastructure.entity.post.QOpenEntity.openEntity;
import static com.flytrap.rssreader.infrastructure.entity.post.QPostEntity.postEntity;
import static com.flytrap.rssreader.infrastructure.entity.subscribe.QSubscribeEntity.subscribeEntity;

import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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

        addFilterCondition(builder, postFilter);

        return queryFactory.selectFrom(postEntity)
            .leftJoin(openEntity).on(postEntity.id.eq(openEntity.postId))
            .where(builder)
            .orderBy(postEntity.pubDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    public List<PostEntity> findAllByFolder(long folderId, PostFilter postFilter, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(folderSubscribeEntity.folderId.eq(folderId));

        addFilterCondition(builder, postFilter);

        return queryFactory.selectFrom(postEntity)
            .leftJoin(openEntity).on(postEntity.id.eq(openEntity.postId))
            .join(subscribeEntity).on(postEntity.subscribe.id.eq(subscribeEntity.id))
            .join(folderSubscribeEntity).on(subscribeEntity.id.eq(folderSubscribeEntity.subscribeId))
            .where(builder)
            .orderBy(postEntity.pubDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    public List<PostEntity> findAllByMember(long memberId, PostFilter postFilter, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(folderEntity.memberId.eq(memberId));

        addFilterCondition(builder, postFilter);

        return queryFactory.selectFrom(postEntity)
            .leftJoin(openEntity).on(postEntity.id.eq(openEntity.postId))
            .join(subscribeEntity).on(postEntity.subscribe.id.eq(subscribeEntity.id))
            .join(folderSubscribeEntity).on(subscribeEntity.id.eq(folderSubscribeEntity.subscribeId))
            .join(folderEntity).on(folderSubscribeEntity.folderId.eq(folderEntity.id))
            .where(builder)
            .orderBy(postEntity.pubDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    public List<PostEntity> findAllBookmarks(long memberId, PostFilter postFilter, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(bookmarkEntity.memberId.eq(memberId));

        addFilterCondition(builder, postFilter);

        return queryFactory.selectFrom(postEntity)
            .leftJoin(openEntity).on(postEntity.id.eq(openEntity.postId))
            .leftJoin(bookmarkEntity).on(postEntity.id.eq(bookmarkEntity.postId))
            .where(builder)
            .orderBy(postEntity.pubDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    private void addFilterCondition(BooleanBuilder builder, PostFilter postFilter) {
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

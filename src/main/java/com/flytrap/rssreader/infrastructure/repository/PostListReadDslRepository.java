package com.flytrap.rssreader.infrastructure.repository;


import static com.flytrap.rssreader.infrastructure.entity.bookmark.QBookmarkEntity.bookmarkEntity;
import static com.flytrap.rssreader.infrastructure.entity.folder.QFolderEntity.folderEntity;
import static com.flytrap.rssreader.infrastructure.entity.folder.QFolderSubscribeEntity.folderSubscribeEntity;
import static com.flytrap.rssreader.infrastructure.entity.post.QOpenEntity.openEntity;
import static com.flytrap.rssreader.infrastructure.entity.post.QPostEntity.postEntity;
import static com.flytrap.rssreader.infrastructure.entity.shared.QSharedFolderEntity.sharedFolderEntity;
import static com.flytrap.rssreader.infrastructure.entity.subscribe.QSubscribeEntity.subscribeEntity;

import com.flytrap.rssreader.infrastructure.repository.output.PostOutput;
import com.flytrap.rssreader.infrastructure.repository.output.PostPaginationOutput;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public class PostListReadDslRepository implements PostListReadRepository, PaginationRepository {

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

    public List<PostOutput> findAllBySubscribe(long memberId, long subscribeId,
            PostFilter postFilter, Pageable pageable) {

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

    public List<PostOutput> findAllByFolder(long memberId, long folderId, PostFilter postFilter,
            Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
                .and(folderSubscribeEntity.folderId.eq(folderId));

        addFilterCondition(builder, postFilter, memberId);

        return initFindAllQuery()
                .join(subscribeEntity).on(postEntity.subscribe.id.eq(subscribeEntity.id))
                .join(folderSubscribeEntity)
                .on(subscribeEntity.id.eq(folderSubscribeEntity.subscribeId))
                .where(builder)
                .orderBy(postEntity.pubDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }


    public Slice<PostPaginationOutput> findAllNoOffsetByMember(long memberId, PostFilter postFilter,
            String cursor, int pageSize) {
        BooleanBuilder builder = new BooleanBuilder();
        builder
                .and(folderEntity.memberId.eq(memberId))
                .or(sharedFolderEntity.memberId.eq(memberId));

        addFilterCondition(builder, postFilter, memberId);

        List<PostPaginationOutput> postOutputs = initFindPaginationAllQuery()
                .join(subscribeEntity).on(postEntity.subscribe.id.eq(subscribeEntity.id))
                .join(folderSubscribeEntity)
                .on(subscribeEntity.id.eq(folderSubscribeEntity.subscribeId))
                .join(folderEntity).on(folderSubscribeEntity.folderId.eq(folderEntity.id))
                .leftJoin(sharedFolderEntity).on(folderEntity.id.eq(sharedFolderEntity.folderId))
                .where(lessThanPubDate(cursor), builder)
                .orderBy(postEntity.pubDate.desc(), postEntity.id.desc())
                .limit(pageSize + 1L)
                .fetch();

        return checkLastPage(pageSize, postOutputs);
    }

    private BooleanExpression lessThanPubDate(String cursor) {
        if (cursor != null) {
            // TO_SECONDS 및 LPAD를 적용한 커스텀 커서
            DateTemplate<Long> secondsTemplate = Expressions.dateTemplate(Long.class,
                    "TO_SECONDS({0})", postEntity.pubDate);

            // customCursor가 cursorBigInt보다 작은지 확인하는 조건식 생성
            return Expressions.booleanTemplate("{0} < {1}", Expressions.stringTemplate(
                            "CONCAT(LPAD({0}, 12, '0'), LPAD({1}, 8, '0'))",
                            secondsTemplate.stringValue(), postEntity.id.stringValue()),
                    Expressions.constant(cursor));
        } else {
            return null;
        }
    }

    public List<PostOutput> findAllByMember(long memberId, PostFilter postFilter,
            Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
                .and(folderEntity.memberId.eq(memberId))
                .or(sharedFolderEntity.memberId.eq(memberId));

        addFilterCondition(builder, postFilter, memberId);

        return initFindAllQuery()
                .join(subscribeEntity).on(postEntity.subscribe.id.eq(subscribeEntity.id))
                .join(folderSubscribeEntity)
                .on(subscribeEntity.id.eq(folderSubscribeEntity.subscribeId))
                .join(folderEntity).on(folderSubscribeEntity.folderId.eq(folderEntity.id))
                .leftJoin(sharedFolderEntity).on(folderEntity.id.eq(sharedFolderEntity.folderId))
                .where(builder)
                .orderBy(postEntity.pubDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<PostOutput> findAllBookmarks(long memberId, PostFilter postFilter,
            Pageable pageable) {

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
                                Expressions.booleanTemplate("{0} is not null", bookmarkEntity.id)))
                //커스텀 커서를 위한 컬럼 날짜 + ID 로 만든다
                .from(postEntity)
                .leftJoin(openEntity)
                .on(postEntity.id.eq(openEntity.postId))
                .leftJoin(bookmarkEntity)
                .on(postEntity.id.eq(bookmarkEntity.postId));
    }


    private JPAQuery<PostPaginationOutput> initFindPaginationAllQuery() {
        DateTemplate<Long> secondsTemplate = Expressions.dateTemplate(Long.class,
                "TO_SECONDS({0})", postEntity.pubDate);

        //유닉시 함수를 사용하는 방법
//        NumberTemplate<Long> template = Expressions.numberTemplate(Long.class,
//                "UNIX_TIMESTAMP({0})", postEntity.pubDate);

        return queryFactory
                .selectDistinct(
                        Projections.constructor(PostPaginationOutput.class,
                                postEntity.id,
                                postEntity.subscribe.id,
                                postEntity.guid,
                                postEntity.title,
                                postEntity.thumbnailUrl,
                                postEntity.description,
                                postEntity.pubDate,
                                postEntity.subscribe.title,
                                Expressions.booleanTemplate("{0} is not null", openEntity.id),
                                Expressions.booleanTemplate("{0} is not null", bookmarkEntity.id),
                                // 커스텀 커서를 위한 컬럼: 날짜 + ID 로 만든다
                                Expressions.stringTemplate(
                                        "CONCAT(LPAD({0}, 12, '0'), LPAD({1}, 8, '0'))",
                                        secondsTemplate.stringValue(), postEntity.id.stringValue())
                        )
                )
                .from(postEntity)
                .leftJoin(openEntity)
                .on(postEntity.id.eq(openEntity.postId))
                .leftJoin(bookmarkEntity)
                .on(postEntity.id.eq(bookmarkEntity.postId));
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

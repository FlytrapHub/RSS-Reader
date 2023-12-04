package com.flytrap.rssreader.infrastructure.entity.post;

import com.flytrap.rssreader.domain.post.Post;
import com.flytrap.rssreader.infrastructure.api.dto.RssSubscribeResource.RssItemResource;
import com.flytrap.rssreader.infrastructure.entity.bookmark.BookmarkEntity;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "rss_post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2500, nullable = false)
    private String guid;

    @Column(length = 2500, nullable = false)
    private String title;

    @Column(length = 2500, nullable = true)
    private String thumbnailUrl;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Instant pubDate;

    @ManyToOne
    @JoinColumn(name = "subscribe_id")
    private SubscribeEntity subscribe;

    @OneToMany
    @JoinColumn(name = "post_id")
    private List<OpenEntity> opens = new ArrayList<OpenEntity>();

    @OneToMany
    @JoinColumn(name = "post_id")
    private List<BookmarkEntity> bookmarks = new ArrayList<BookmarkEntity>();

    // TODO: React, Thumbnail 추가 하기

    @Builder
    protected PostEntity(Long id, String guid, String title, String thumbnailUrl, String description, Instant pubDate,
        SubscribeEntity subscribe, List<OpenEntity> opens, List<BookmarkEntity> bookmarks) {
        this.id = id;
        this.guid = guid;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.pubDate = pubDate;
        this.subscribe = subscribe;
        this.opens = opens;
        this.bookmarks = bookmarks;
    }

    public static PostEntity from(RssItemResource rssItemResource, SubscribeEntity subscribe) {
        return PostEntity.builder()
            .guid(rssItemResource.guid())
            .title(rssItemResource.title())
            .thumbnailUrl(rssItemResource.thumbnailUrl())
            .description(rssItemResource.description())
            .pubDate(rssItemResource.pubDate())
            .subscribe(subscribe)
            .build();
    }

    public void updateBy(RssItemResource itemResource) {
        this.title = itemResource.title();
        this.thumbnailUrl = itemResource.thumbnailUrl();
        this.description = itemResource.description();
    }

    public Post toDomain() {
        return Post.builder()
            .id(id)
            .subscribeId(subscribe.getId())
            .guid(guid)
            .title(title)
            .thumbnailUrl(thumbnailUrl)
            .description(description)
            .pubDate(pubDate)
            .build();
    }

    public Post toDomain(Long memberId) {
        return Post.builder()
            .id(id)
            .subscribeId(subscribe.getId())
            .guid(guid)
            .title(title)
            .thumbnailUrl(thumbnailUrl)
            .description(description)
            .pubDate(pubDate)
            .subscribeTitle(subscribe.getTitle())
            .open(isOpen(memberId))
            .bookmark(isBookmark(memberId))
            .build();
    }

    public boolean isOpen(Long memberId) {
        for (OpenEntity o : opens) {
            if (o.isSameMember(memberId)) {
                return true;
            }
        }

        return false;
    }

    public boolean isBookmark(Long memberId) {
        for (BookmarkEntity b : bookmarks) {
            if (b.isSameMember(memberId)) {
                return true;
            }
        }

        return false;
    }

}

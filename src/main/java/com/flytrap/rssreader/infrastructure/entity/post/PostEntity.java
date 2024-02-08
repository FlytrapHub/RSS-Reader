package com.flytrap.rssreader.infrastructure.entity.post;

import com.flytrap.rssreader.domain.post.Post;
import com.flytrap.rssreader.infrastructure.api.parser.dto.RssSubscribeResource.RssItemResource;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
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

    // TODO: React 추가 하기

    @Builder
    protected PostEntity(Long id, String guid, String title, String thumbnailUrl, String description, Instant pubDate,
        SubscribeEntity subscribe) {
        this.id = id;
        this.guid = guid;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.pubDate = pubDate;
        this.subscribe = subscribe;
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

}

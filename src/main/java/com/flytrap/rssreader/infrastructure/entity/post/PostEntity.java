package com.flytrap.rssreader.infrastructure.entity.post;

import com.flytrap.rssreader.infrastructure.api.dto.RssItemResource;
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

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "subscribe_id")
    private SubscribeEntity subscribe;

    @Temporal(TemporalType.TIMESTAMP)
    private Instant pubDate;

    // TODO: Bookmark, Open, React 추가하기

    @Builder
    protected PostEntity(Long id, String guid, String title, String description, SubscribeEntity subscribe, Instant pubDate) {
        this.id = id;
        this.guid = guid;
        this.title = title;
        this.description = description;
        this.subscribe = subscribe;
        this.pubDate = pubDate;
    }

    public static PostEntity from(RssItemResource rssItemResource, SubscribeEntity subscribe) {
        return PostEntity.builder()
            .guid(rssItemResource.guid())
            .title(rssItemResource.title())
            .description(rssItemResource.description())
            .pubDate(rssItemResource.pubDate())
            .subscribe(subscribe)
            .build();
    }

    public void updateBy(RssItemResource itemResource) {
        this.title = itemResource.title();
        this.description = itemResource.description();
    }

}

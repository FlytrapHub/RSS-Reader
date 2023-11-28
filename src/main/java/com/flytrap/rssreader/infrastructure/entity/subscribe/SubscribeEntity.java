package com.flytrap.rssreader.infrastructure.entity.subscribe;

import com.flytrap.rssreader.domain.subscribe.BlogPlatform;
import com.flytrap.rssreader.domain.subscribe.Subscribe;
import com.flytrap.rssreader.presentation.dto.RssFeedData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "rss_subscribe")
public class SubscribeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2500, nullable = false)
    private String title;

    @Column(length = 2500, nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    private BlogPlatform platform;

    @Builder
    protected SubscribeEntity(Long id, String title, String url,
            BlogPlatform platform) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.platform = platform;
    }

    public static SubscribeEntity from(RssFeedData rssFeedData) {
        return SubscribeEntity.builder()
            .title(rssFeedData.title())
            .url(rssFeedData.url())
            .platform(rssFeedData.platform())
            .build();
    }

    public Subscribe toDomain(RssFeedData rssFeedData) {
        return Subscribe.of(this.id, rssFeedData.title(), url);
    }

    public Subscribe toDomain() {
        return Subscribe.of(this.id, this.title, this.url);
    }

}

package com.flytrap.rssreader.infrastructure.entity.subscribe;

import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
    private String description;

    @Column(length = 2500, nullable = false)
    private String url;

    @OneToMany
    @JoinColumn(name = "subscribe_id")
    private List<PostEntity> post = new ArrayList<PostEntity>();

    @Enumerated(EnumType.STRING)
    private BlogPlatform platform;

    @Builder
    protected SubscribeEntity(Long id, String description, String url, List<PostEntity> post,
        BlogPlatform platform) {
        this.id = id;
        this.description = description;
        this.url = url;
        this.post = post;
        this.platform = platform;
    }
}

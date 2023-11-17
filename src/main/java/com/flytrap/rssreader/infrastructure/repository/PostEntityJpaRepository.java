package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityJpaRepository extends JpaRepository<PostEntity, Long>  {
    List<PostEntity> findAllBySubscribeOrderByPubDateDesc(SubscribeEntity subscribe);
}

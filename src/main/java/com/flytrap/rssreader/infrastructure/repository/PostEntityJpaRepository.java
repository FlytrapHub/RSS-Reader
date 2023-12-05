package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.post.SubscribePostCount;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostEntityJpaRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findAllBySubscribeOrderByPubDateDesc(SubscribeEntity subscribe);

    @Query("select p.subscribe.id as subscribeId, count(p.id) as postCount "
            + "from PostEntity p "
            + "where p.subscribe.id in :subscribes "
            + "group by p.subscribe.id")
    List<SubscribePostCount> findSubscribeCounts(@Param("subscribes") List<Long> subscribes);

}

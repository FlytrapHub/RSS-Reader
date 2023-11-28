package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostEntityJpaRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findAllBySubscribeOrderByPubDateDesc(SubscribeEntity subscribe);
    @Query("select p.subscribe.id as subscribeId, count(p) as postCount from PostEntity p where p.subscribe.id in :subscribes group by p.subscribe.id")
    List<Object[]> findSubscribeCounts(@Param("subscribes") List<Long> subscribes);

    default Map<Long, Integer> countsGroupBySubscribeId(List<Long> subscribes) {
        List<Object[]> resultList = findSubscribeCounts(subscribes);

        Map<Long, Integer> result = new HashMap<>();
        for (Object[] row : resultList) {
            Long subscribeId = (Long) row[0];
            Integer postCount = ((Number) row[1]).intValue(); // count(p)는 Number 타입이므로 적절한 형변환 필요
            result.put(subscribeId, postCount);
        }

        return result;
    }
}

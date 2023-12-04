package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.post.OpenEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostOpenRepository extends JpaRepository<OpenEntity, Long> {

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    @Query("select o.postId, count(o.postId) "
            + "from OpenEntity o "
            + "where o.memberId = :id and o.postId in :subscribes "
            + "group by o.postId")
    List<Object[]> countOpens(@Param("id") long id, @Param("subscribes") List<Long> subscribes);

    default Map<Long, Integer> countsGroupBySubscribeId(long id, List<Long> subscribes) {
        List<Object[]> resultList = countOpens(id, subscribes);

        Map<Long, Integer> result = new HashMap<>();
        for (Object[] row : resultList) {
            Long subscribeId = (Long) row[0];
            Integer postCount = ((Number) row[1]).intValue(); // count(p)는 Number 타입이므로 적절한 형변환 필요
            result.put(subscribeId, postCount);
        }

        return result;
    }

    void deleteByMemberIdAndPostId(long memberId, Long postId);
}

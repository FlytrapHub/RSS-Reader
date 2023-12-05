package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.post.OpenEntity;
import com.flytrap.rssreader.infrastructure.entity.post.OpenPostCount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostOpenRepository extends JpaRepository<OpenEntity, Long> {

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    @Query("select p.subscribe.id as subscribeId, count(o.postId) as postCount "
            + "from OpenEntity o "
            + "inner join PostEntity p on o.postId = p.id "
            + "where o.memberId = :id and p.subscribe.id in :subscribes "
            + "group by o.postId")
    List<OpenPostCount> countOpens(@Param("id") long id,
            @Param("subscribes") List<Long> subscribes);

    int deleteByMemberIdAndPostId(long memberId, Long postId);
}

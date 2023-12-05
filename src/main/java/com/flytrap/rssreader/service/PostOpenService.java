package com.flytrap.rssreader.service;

import com.flytrap.rssreader.infrastructure.entity.post.OpenPostCount;
import com.flytrap.rssreader.infrastructure.repository.PostOpenRepository;
import com.flytrap.rssreader.service.dto.PostOpenParam;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostOpenService {

    private final PostOpenRepository postOpenRepository;

    @Transactional
    public void open(PostOpenParam value) {

        if (postOpenRepository.existsByMemberIdAndPostId(value.memberId(), value.postId())) {
            return;
        }

        postOpenRepository.save(value.toEntity());
    }

    @Transactional(readOnly = true)
    public Map<Long, OpenPostCount> countOpens(long id, List<Long> subscribes) {
        return postOpenRepository.countOpens(id, subscribes).stream()
                .collect(Collectors.toMap(OpenPostCount::getSubscribeId, it -> it));
    }

    @Transactional
    public void deleteRead(long memberId, Long postId) {
        postOpenRepository.deleteByMemberIdAndPostId(memberId, postId);
    }

}

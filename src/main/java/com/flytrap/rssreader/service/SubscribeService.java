package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.subscribe.Subscribe;
import com.flytrap.rssreader.infrastructure.api.RssChecker;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.SubscribeEntityJpaRepository;
import com.flytrap.rssreader.presentation.dto.RssFeedData;
import com.flytrap.rssreader.presentation.dto.SubscribeRequest.CreateRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class SubscribeService {

    //TODO:
    // step1검색을 누르면 유효한 RSS인지 검증한다
    // step2 이미 구독한 블로그라면 즉 DB에 저장되어있다면
    // 새로 구독하지말고 DB에있는 url(정보를) 가져다써라
    //  step3하지만 새로운 블로그라면 새로 추가해주자
    //  새로운 구독이라면 DB 관계형테이블 구독 에 넣어라

    private final SubscribeEntityJpaRepository subscribeRepository;
    private final RssChecker rssChecker;

    @Transactional
    public Subscribe subscribe(CreateRequest request) {
        RssFeedData rssFeedData = rssChecker.parseRssDocuments(request).orElseThrow();

        if (!subscribeRepository.existsByUrl(request.blogUrl())) {
            //TODO: 없으면 새로 저장한다.
            return subscribeRepository.save(SubscribeEntity.from(rssFeedData))
                    .toDomain(rssFeedData);
        }
        //TODO: 도메인을 DB에 넣을거면 꺼내 써는 방향?, 일단은 도메인을 만들어 리턴한다.
        return subscribeRepository.findByUrl(request.blogUrl()).orElseThrow().toDomain(rssFeedData);
    }

    @Transactional
    public void unsubscribe(Long subscribeId) {
        subscribeRepository.delete(findByIdSubscribed(subscribeId));
    }

    private SubscribeEntity findByIdSubscribed(Long subscribedId) {
        return subscribeRepository.findById(subscribedId).orElseThrow();
    }

    public List<Subscribe> read(Collection<Long> subscribeIds) {
        return subscribeIds.stream()
                .map(subscribeRepository::findById)
                .filter(Optional::isPresent)
                .map(entity -> entity.get().toDomain())
                .toList();
    }

}

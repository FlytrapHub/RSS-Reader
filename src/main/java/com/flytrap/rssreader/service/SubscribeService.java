package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.subscribe.Subscribe;
import com.flytrap.rssreader.infrastructure.api.RssChecker;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.SubscribeEntityJpaRepository;
import com.flytrap.rssreader.presentation.dto.RssFeedData;
import com.flytrap.rssreader.presentation.dto.SubscribeRequest.CreateRequest;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class SubscribeService {

    private final SubscribeEntityJpaRepository subscribeRepository;
    private final RssChecker rssChecker;

    @Transactional
    public Subscribe subscribe(CreateRequest request) {
        RssFeedData rssFeedData = rssChecker.parseRssDocuments(request).orElseThrow();

        if (!subscribeRepository.existsByUrl(request.blogUrl())) {
            return subscribeRepository.save(SubscribeEntity.from(rssFeedData))
                .toNewSubscribeDomain();
        }

        return subscribeRepository.findByUrl(request.blogUrl()).orElseThrow()
            .toExistingSubscribeDomain();
    }

    @Transactional
    public void unsubscribe(Long subscribeId) {
        subscribeRepository.delete(findByIdSubscribed(subscribeId));
    }

    private SubscribeEntity findByIdSubscribed(Long subscribedId) {
        return subscribeRepository.findById(subscribedId).orElseThrow();
    }

    public List<Subscribe> read(Collection<Long> subscribeIds) {
        return subscribeRepository.findAllById(subscribeIds).stream()
                .map(SubscribeEntity::toExistingSubscribeDomain)
                .toList();
    }

    public List<SubscribeEntity> findSubscribeList() {
        return subscribeRepository.findAll();
    }
}

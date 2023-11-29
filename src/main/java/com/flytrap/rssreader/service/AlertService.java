package com.flytrap.rssreader.service;

import com.flytrap.rssreader.infrastructure.entity.alert.AlertEntity;
import com.flytrap.rssreader.infrastructure.repository.AlertEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertEntityJpaRepository alertRepository;

    public Long on(Long folderId, Long memberId, Integer platformNum) {
        return alertRepository.save(AlertEntity.create(memberId, folderId, platformNum))
                .getId();
    }

    public void off(Long folderId, Long memberId) {
        AlertEntity alert = findByAlert(folderId, memberId);
        alertRepository.delete(alert);
    }

    private AlertEntity findByAlert(Long folderId, Long memberId) {
        return alertRepository.findByFolderIdAndMemberId(folderId, memberId).orElseThrow();
    }
}

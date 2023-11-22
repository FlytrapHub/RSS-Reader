package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.subscribe.Subscribe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FolderSubscribeService {


    public Subscribe folderSubscribe(Long subscribeId, Long id) {
        return Subscribe.builder().build();
    }
}

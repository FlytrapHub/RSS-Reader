package com.flytrap.rssreader.presentation.facade;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.service.PostOpenService;
import com.flytrap.rssreader.service.PostReadService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenCheckFacade {

    private final PostReadService postReadService;
    private final PostOpenService postOpenService;


    public List<Folder> addUnreadCountInSubscribes(long id, List<Folder> foldersWithSubscribe) {
        List<Long> subscribes = foldersWithSubscribe.stream().map(Folder::getSubscribeIds)
                .flatMap(List::stream).toList();

        Map<Long, Integer> countsPost = postReadService.countPosts(subscribes);
        Map<Long, Integer> countsOpen = postOpenService.countOpens(id, subscribes);

        for (Folder folder : foldersWithSubscribe) {
            folder.addUnreadCountsBySubscribes(countsPost, countsOpen);
        }

        return foldersWithSubscribe;
    }
}

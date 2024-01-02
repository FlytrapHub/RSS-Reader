package com.flytrap.rssreader.presentation.facade;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.folder.FolderSubscribe;
import com.flytrap.rssreader.domain.subscribe.Subscribe;
import com.flytrap.rssreader.infrastructure.entity.post.EmptyOpenPostCount;
import com.flytrap.rssreader.infrastructure.entity.post.EmptySubscribePostCount;
import com.flytrap.rssreader.infrastructure.entity.post.OpenPostCount;
import com.flytrap.rssreader.infrastructure.entity.post.SubscribePostCount;
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

    public FolderSubscribe addUnreadCountInFolderSubscribe(long memberId, Subscribe subscribe,
        FolderSubscribe folderSubscribe) {
        Long subscribeId = subscribe.getId();
        Map<Long, SubscribePostCount> countsPost = postReadService.countPosts(List.of(subscribeId));
        Map<Long, OpenPostCount> countsOpen = postOpenService.countOpens(memberId,
            List.of(subscribeId));

        folderSubscribe.addUnreadCount(
            countsPost.getOrDefault(subscribeId, new EmptySubscribePostCount()).getPostCount(),
            countsOpen.getOrDefault(subscribeId, new EmptyOpenPostCount()).getPostCount());

        return folderSubscribe;
    }

    public List<? extends Folder> addUnreadCountInSubscribes(long id,
        List<? extends Folder> foldersWithSubscribe) {
        List<Long> subscribes = foldersWithSubscribe.stream().map(Folder::getSubscribeIds)
            .flatMap(List::stream).toList();

        Map<Long, SubscribePostCount> countsPost = postReadService.countPosts(subscribes);
        Map<Long, OpenPostCount> countsOpen = postOpenService.countOpens(id, subscribes);

        for (Folder folder : foldersWithSubscribe) {
            folder.addUnreadCountsBySubscribes(countsPost, countsOpen);
        }

        return foldersWithSubscribe;
    }
}

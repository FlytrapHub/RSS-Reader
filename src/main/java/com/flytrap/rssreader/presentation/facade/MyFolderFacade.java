package com.flytrap.rssreader.presentation.facade;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.service.folder.FolderReadService;
import com.flytrap.rssreader.service.SharedFolderReadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyFolderFacade {

    private final FolderReadService folderReadService;
    private final SharedFolderReadService sharedFolderReadService;

    public List<Folder> getMyFolders(long id) {
        List<Folder> myFolders = folderReadService.findAllByMemberId(id);

        // 내가 초대받은 폴더 목록
        List<Long> invitedFolderIds = sharedFolderReadService.findFoldersInvited(id);
        List<Folder> invitedFolders = folderReadService.findAllByIds(invitedFolderIds);

        myFolders.addAll(invitedFolders);

        return myFolders;
    }
}

package com.flytrap.rssreader.presentation.dto;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.shared.SharedFolder;
import java.util.List;
import java.util.Map;

@Deprecated
public record FolderListSummary(long id,
                                String name,
                                int unreadCount,
                                List<SubscribeListSummary> blogs,
                                List<MemberSummary> invitedMembers) {

    public static FolderListSummary from(Folder folder) {

        if (folder instanceof SharedFolder) {
            return new FolderListSummary(folder.getId(),
                    folder.getName(),
                    folder.getUnreadCount(),
                    SubscribeListSummary.from(folder.getSubscribes()),
                    MemberSummary.from(((SharedFolder) folder).getInvitedMembers()));
        }

        return new FolderListSummary(folder.getId(),
                folder.getName(),
                folder.getUnreadCount(),
                SubscribeListSummary.from(folder.getSubscribes()),
                List.of());
    }
}

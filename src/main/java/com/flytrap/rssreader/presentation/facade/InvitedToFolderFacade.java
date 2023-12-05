package com.flytrap.rssreader.presentation.facade;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.folder.SharedStatus;
import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.domain.shared.SharedFolder;
import com.flytrap.rssreader.service.MemberService;
import com.flytrap.rssreader.service.SharedFolderReadService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvitedToFolderFacade {

    private final SharedFolderReadService sharedFolderReadService;
    private final MemberService memberService;

    public List<Folder> addInvitedMembersInFolder(List<Folder> folders) {
        List<Long> folderIds = folders.stream().map(Folder::getId).toList();
        Map<Long, List<Long>> membersInFolders =    // 폴더마다 초대된 멤버 목록
                sharedFolderReadService.findMembersInFolders(folderIds);

        Map<Long, Member> memberInfo = getMemberInfomations(membersInFolders);

        return folders.stream()
                .map(folder -> injectMemberInfo(membersInFolders, memberInfo, folder))
                .toList();
    }

    private Folder injectMemberInfo(Map<Long, List<Long>> membersInFolders,
            Map<Long, Member> memberInfo,
            Folder folder) {
        if (folder.getSharedStatus() == SharedStatus.PRIVATE) {
            return folder;
        }
        List<Member> members = membersInFolders.get(folder.getId()).stream()
                .map(memberInfo::get)
                .toList();
        return SharedFolder.of(folder, members);
    }

    private Map<Long, Member> getMemberInfomations(Map<Long, List<Long>> membersInFolders) {
        Set<Long> memberSet = membersInFolders.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        return memberService.findAllByIds(memberSet).stream()
                .collect(Collectors.toMap(Member::getId, member -> member));
    }

}

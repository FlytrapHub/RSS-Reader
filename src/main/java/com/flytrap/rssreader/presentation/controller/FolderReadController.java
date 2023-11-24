package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.folder.SharedStatus;
import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.domain.shared.SharedFolder;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.presentation.dto.Folders;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import com.flytrap.rssreader.service.FolderReadService;
import com.flytrap.rssreader.service.MemberService;
import com.flytrap.rssreader.service.SharedFolderReadService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderReadController {

    private final FolderReadService folderReadService;
    private final MemberService memberReadService;
    private final SharedFolderReadService sharedFolderReadService;

    @GetMapping
    public ApplicationResponse<Folders> getFolders(@Login SessionMember member) {

        // 내가 생성한 폴더 목록
        Map<SharedStatus, List<Folder>> folders = folderReadService.findAllByMemberIdGroupByShared(
                member.id());
        // 내가 초대받은 폴더 목록
        List<Long> invitedFolders = sharedFolderReadService.findFoldersInvited(member.id());
        Map<Long, Folder> foldersGroupById = folderReadService.findAllByIds(invitedFolders)
                .stream()
                .collect(Collectors.toMap(Folder::getId, f -> f));

        // TODO: 폴더에 블로그 목록 추가
        // 감자가 하고있는 거
        Map<Long, List<SubscribeEntity>> blogMaps = null;

        // 폴더Id마다 초대된 멤버리스트
        Map<Long, List<Long>> membersInFolders = sharedFolderReadService.findMembersInFolders(
                invitedFolders);
        Set<Long> memberIds = membersInFolders.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        Map<Long, Member> membersGroupById = memberReadService.findAllByIds(memberIds)
                .stream()
                .collect(Collectors.toMap(Member::getId, m -> m));

        List<SharedFolder> sharedFolders = invitedFolders.stream()
                .map(e -> {
                    SharedFolder folder = (SharedFolder) foldersGroupById.get(e);
                    membersInFolders.get(e).forEach(m -> folder.invite(membersGroupById.get(m)));
                    return folder;
                })
                .toList();

        // TODO 읽지 않은 글 개수, 블로그마다...
        Map<Long, Long> unreadCountMap = null;

        return new ApplicationResponse(Folders.from(folders, sharedFolders, blogMaps, unreadCountMap));
    }
}

package com.flytrap.rssreader.domain.shared;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.folder.FolderSubscribe;
import com.flytrap.rssreader.domain.member.Member;
import java.util.ArrayList;
import java.util.List;

public class SharedFolder extends Folder {

    private List<Member> invitedMembers = new ArrayList<>();

    protected SharedFolder(Long id, String name, Long memberId, Boolean isDeleted, List<Member> invitedMembers, List<FolderSubscribe> subscribes) {
        super(id, name, memberId, true, isDeleted);
        super.addAllSubscribes(subscribes);
        this.invitedMembers = invitedMembers;
    }

    public void invite(Member member) {
        invitedMembers.add(member);
    }

    public List<Member> getInvitedMembers() {
        return List.of(invitedMembers.toArray(new Member[0]));
    }

    public static SharedFolder of (Folder folder, List<Member> invitedMembers) {
        return new SharedFolder(folder.getId(), folder.getName(), folder.getMemberId(), folder.getIsDeleted(), invitedMembers, folder.getSubscribes());
    }

}

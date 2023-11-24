package com.flytrap.rssreader.domain.shared;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.member.Member;
import java.util.ArrayList;
import java.util.List;

public class SharedFolder extends Folder {

    private List<Member> invitedMembers = new ArrayList<>();

    public SharedFolder(Long id, String name, Long memberId, Boolean isDeleted, List<Member> invitedMembers) {
        super(id, name, memberId, true, isDeleted);
        this.invitedMembers = invitedMembers;
    }

    public void invite(Member member) {
        invitedMembers.add(member);
    }

    public List<Member> getInvitedMembers() {
        return List.of(invitedMembers.toArray(new Member[0]));
    }

}

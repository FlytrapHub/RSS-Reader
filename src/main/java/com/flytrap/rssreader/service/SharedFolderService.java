package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.infrastructure.entity.shared.SharedFolderMembers;
import com.flytrap.rssreader.infrastructure.repository.SharedFolderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SharedFolderService {

    private final SharedFolderJpaRepository sharedFolderJpaRepository;

    @Transactional
    public void invite(Folder folder, long inviteeId) {

        if(folder.isOwner(inviteeId)) {
            throw new IllegalArgumentException("폴더의 소유자는 폴더를 공유할 수 없습니다.");
        }

        if (isExists(folder, inviteeId)) {
            throw new DuplicateKeyException("이미 공유된 폴더입니다.");
        }

        sharedFolderJpaRepository.save(
                SharedFolderMembers.of(folder.getId(), inviteeId));
    }

    @Transactional(readOnly = true)
    public boolean isExists(Folder folder, long inviteeId) {
        return sharedFolderJpaRepository.existsByFolderIdAndMemberId(folder.getId(),
                inviteeId);
    }

    @Transactional
    public void leave(Folder folder, long id) {

        if (!isExists(folder, id)) {
            throw new IllegalArgumentException("공유된 폴더가 아닙니다.");
        }
        if (folder.isOwner(id)) {
            throw new IllegalArgumentException("폴더의 소유자는 폴더를 나갈 수 없습니다.");
        }

        sharedFolderJpaRepository.delete(
                SharedFolderMembers.of(folder.getId(), id));
    }

    @Transactional
    public void makeOut(Folder folder, Long folderMemberId, long ownerId) {

        if (!folder.isOwner(ownerId)) {
            throw new IllegalArgumentException("폴더의 소유자가 아닙니다.");
        }
        if (!isExists(folder, folderMemberId)) {
            throw new IllegalArgumentException("공유된 폴더가 아닙니다.");
        }

        sharedFolderJpaRepository.delete(
                SharedFolderMembers.of(folder.getId(), folderMemberId));
    }
}

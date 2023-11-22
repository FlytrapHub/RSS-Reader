package com.flytrap.rssreader.infrastructure.repository;

import com.flytrap.rssreader.infrastructure.entity.shared.SharedFolderMembers;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedFolderJpaRepository extends JpaRepository<SharedFolderMembers, Long> {

    List<SharedFolderMembers> findAllByFolderId(long folderId);
    boolean existsByFolderIdAndMemberId(long folderId, long memberId);

}

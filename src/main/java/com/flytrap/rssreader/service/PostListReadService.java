package com.flytrap.rssreader.service;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.domain.post.Post;
import com.flytrap.rssreader.infrastructure.repository.PostListReadRepository;
import com.flytrap.rssreader.infrastructure.repository.output.PostOutput;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor
@Service
public class PostListReadService {

    private final PostListReadRepository postListReadRepository;

    public List<Post> getPostsBySubscribe(SessionMember member, Long subscribeId, PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllBySubscribe(subscribeId, postFilter, pageable)
            .stream()
            .map(PostOutput::toDomain)
            .toList();
    }

    public List<Post> getPostsByFolder(SessionMember member, Folder folder, PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllByFolder(folder.getId(), postFilter, pageable)
            .stream()
            .map(PostOutput::toDomain)
            .toList();
    }

    public List<Post> getPostsByMember(SessionMember member, PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllByMember(member.id(), postFilter, pageable)
            .stream()
            .map(PostOutput::toDomain)
            .toList();
    }
}

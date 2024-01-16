package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.domain.folder.Folder;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.utill.pagination.PageResponse;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import com.flytrap.rssreader.presentation.dto.PostResponse;
import com.flytrap.rssreader.presentation.dto.PostResponse.PostListResponse;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import com.flytrap.rssreader.service.PostListReadService;
import com.flytrap.rssreader.service.folder.FolderVerifyOwnerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostListReadController {

    private final PostListReadService postListReadService;
    private final FolderVerifyOwnerService folderVerifyOwnerService;

    @GetMapping("/subscribes/{subscribeId}/posts")
    public ApplicationResponse<PostListResponse> getPostsBySubscribe(
            @PathVariable Long subscribeId,
            PostFilter postFilter,
            @PageableDefault(page = 0, size = 15) Pageable pageable,
            @Login SessionMember member) {

        List<PostResponse> posts = postListReadService.getPostsBySubscribe(member, subscribeId,
                        postFilter, pageable)
                .stream()
                .map(PostResponse::from)
                .toList();

        return new ApplicationResponse<>(
                new PostListResponse(posts));
    }

    @GetMapping("/folders/{folderId}/posts")
    public ApplicationResponse<PostListResponse> getPostsByFolder(
            @PathVariable Long folderId,
            PostFilter postFilter,
            @PageableDefault(page = 0, size = 15) Pageable pageable,
            @Login SessionMember member) {

        Folder verifyFolder = folderVerifyOwnerService.getVerifiedFolder(folderId, member.id());

        List<PostResponse> posts = postListReadService.getPostsByFolder(member, verifyFolder,
                        postFilter, pageable)
                .stream()
                .map(PostResponse::from)
                .toList();

        return new ApplicationResponse<>(
                new PostListResponse(posts));
    }

    @GetMapping("/posts")
    public ApplicationResponse<PageResponse<PostListResponse>> getPostsByMember(
            @RequestParam String cursor,
            @RequestParam(required = false, defaultValue = "15") int size,
            PostFilter postFilter,
            @Login SessionMember member) {

        PageResponse<PostListResponse> pageResponse = postListReadService.getPostsByMember(cursor,
                size, postFilter, member);

        return new ApplicationResponse<>(pageResponse);
    }


    @GetMapping("/postsV1")
    public ApplicationResponse<PostListResponse> getPostsByMember(
            PostFilter postFilter,
            @PageableDefault(page = 0, size = 15) Pageable pageable,
            @Login SessionMember member) {

        List<PostResponse> posts = postListReadService.getPostsByMemberV1(member, postFilter,
                        pageable)
                .stream()
                .map(PostResponse::from)
                .toList();

        return new ApplicationResponse<>(
                new PostListResponse(posts));
    }
}

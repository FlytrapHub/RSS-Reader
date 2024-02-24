package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.domain.bookmark.Bookmark;
import com.flytrap.rssreader.domain.post.Post;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.controller.api.BookmarkControllerApi;
import com.flytrap.rssreader.presentation.dto.BookmarkRequest;
import com.flytrap.rssreader.presentation.dto.PostFilter;
import com.flytrap.rssreader.presentation.dto.PostResponse;
import com.flytrap.rssreader.presentation.dto.PostResponse.PostListResponse;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.Login;
import com.flytrap.rssreader.service.BookmarkService;
import com.flytrap.rssreader.service.BookmarkVerifyOwnerService;
import com.flytrap.rssreader.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookmarkController implements BookmarkControllerApi {

    private static final String DELETE_BOOKMARK_MESSAGE = "북마크가 삭제되었습니다. postId = ";

    private final PostService postService;
    private final BookmarkService bookmarkService;
    private final BookmarkVerifyOwnerService bookmarkVerifyOwnerService;

    @GetMapping("/bookmarks")
    public ApplicationResponse<PostListResponse> getBookmarks(
        PostFilter postFilter,
        @PageableDefault(page = 0, size = 15) Pageable pageable,
        @Login SessionMember member
    ) {

        List<PostResponse> posts = bookmarkService.getBookmarks(member, postFilter, pageable)
            .stream()
            .map(PostResponse::from)
            .toList();

        return new ApplicationResponse<>(
            new PostListResponse(posts));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/posts/{postId}/bookmarks")
    public ApplicationResponse<BookmarkRequest.Response> addBookmark(
        @PathVariable Long postId,
        @Login SessionMember member
    ) {

        Post post = postService.get(postId);
        Bookmark bookmark = bookmarkService.addBookmark(member, post);

        return new ApplicationResponse<>(BookmarkRequest.Response.from(bookmark));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/posts/{postId}/bookmarks")
    public ApplicationResponse<String> removeBookmark(
        @PathVariable Long postId,
        @Login SessionMember member
    ) {

        bookmarkService.removeBookmark(member, postId);

        return new ApplicationResponse<>(DELETE_BOOKMARK_MESSAGE + postId);
    }

}

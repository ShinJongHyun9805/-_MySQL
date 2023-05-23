package com.example.fastcampusmysql.application.controller;

import com.example.fastcampusmysql.application.usacase.CreatePostLikeUsecase;
import com.example.fastcampusmysql.application.usacase.CreatePostUsecase;
import com.example.fastcampusmysql.application.usacase.GetTimelinePostsUsecase;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.PostDto;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostWriteService postWriteService;

    private final PostReadService postReadService;

    private final GetTimelinePostsUsecase getTimelinePostsUsecase;

    private final CreatePostUsecase createPostUsecase;

    private final CreatePostLikeUsecase createPostLikeUsecase;

    @PostMapping("")
    public Long create(PostCommand postCommand){
        return createPostUsecase.execute(postCommand);
    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCount> getDailyPostCount(DailyPostCountRequest request){
        return postReadService.getDailyPostCounts(request);
    }

    @GetMapping("/members/{memberId}")
    public Page<PostDto> getPosts(@PathVariable Long memberId, Pageable pageable){
        return postReadService.getPosts(memberId, pageable);
    }

    @GetMapping("/members/{memberId}/by-cursor")
    public PageCursor<Post> getPostsByCursor(@PathVariable Long memberId, CursorRequest request){
        return postReadService.getPosts(memberId, request);
    }

    @GetMapping("/members/{memberId}/timeLine")
    public PageCursor<Post> getTimeLine(@PathVariable Long memberId, CursorRequest cursorRequest){
        return getTimelinePostsUsecase.executeByTimeline(memberId, cursorRequest);
    }

    @PostMapping("/{postId}/like")
    public void likePost(@PathVariable Long postId){
        postWriteService.likePost(postId);
    }

    @PostMapping("/{postId}/like/optimistic")
    public void optimisticLikePost(@PathVariable Long postId){
        postWriteService.likePostByOptimisticLock(postId);
    }

    @PostMapping("/{postId}/like/v2")
    public void likePostV2(@PathVariable Long postId, @RequestParam Long memberId){
        createPostLikeUsecase.execute(postId, memberId);
    }

}

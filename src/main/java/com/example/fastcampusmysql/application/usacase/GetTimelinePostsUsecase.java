package com.example.fastcampusmysql.application.usacase;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.TimelineReadService;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetTimelinePostsUsecase {

    private final FollowReadService followReadService;

    private final PostReadService postReadService;

    private final TimelineReadService timelineReadService;

    public PageCursor<Post> execute(Long memberId, CursorRequest cursorRequest){
        /*
            1. memberId -> follow 조회
            2. 1번 결과로 게시물 조회.
         */
        var followings = followReadService.getFollowings(memberId);
        var followingMemberIds = followings.stream().map(Follow::getToMemberId).collect(Collectors.toList());

        return postReadService.getPosts(followingMemberIds, cursorRequest);
    }

    public PageCursor<Post> executeByTimeline(Long memberId, CursorRequest cursorRequest){
        /*
            1. TimeLine 조회 (Entity 구현해야함.)
            2. 1번에 해당하는 게시물을 조회.
         */
        var pageTimelines = timelineReadService.getTimeline(memberId, cursorRequest);
        var postIds = pageTimelines.getBody().stream()
                .map(Timeline::getPostId)
                .collect(Collectors.toList());
        var posts = postReadService.getPosts(postIds);

        return new PageCursor(pageTimelines.getNextCursorRequest(), posts);
    }
}

package com.example.fastcampusmysql.application.usacase;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import com.example.fastcampusmysql.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CreatePostUsecase {

    private final PostWriteService postWriteService;

    private final FollowReadService followReadService;

    private final TimelineWriteService timelineWriteService;

    public Long execute(PostCommand postCommand){
        var postId = postWriteService.create(postCommand);

        var followerMemberIds = followReadService.getFollowers(postCommand.getMemberId())
                .stream().map(Follow::getFromMemberId)
                .collect(Collectors.toList());

        timelineWriteService.deliveryToTimeline(postId, followerMemberIds);

        return postId;
    }

}

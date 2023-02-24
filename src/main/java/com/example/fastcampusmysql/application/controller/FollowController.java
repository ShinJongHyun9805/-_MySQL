package com.example.fastcampusmysql.application.controller;

import com.example.fastcampusmysql.application.usacase.CreateFollowMemberUsacase;
import com.example.fastcampusmysql.application.usacase.GetFollowMemberUsacase;
import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowWriteService;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final CreateFollowMemberUsacase createFollowMemberUsacasea;

    private final GetFollowMemberUsacase getFollowMemberUsacase;

    @PostMapping("/{fromId}/{toId}")
    public void register(@PathVariable Long fromId, @PathVariable Long toId){
        createFollowMemberUsacasea.execute(fromId, toId);
    }

    @GetMapping("/members/{fromId}")
    public List<MemberDto> create(@PathVariable Long fromId){
        return getFollowMemberUsacase.excute(fromId);
    }
}

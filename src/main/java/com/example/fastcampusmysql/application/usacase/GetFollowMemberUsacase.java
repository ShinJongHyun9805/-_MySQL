package com.example.fastcampusmysql.application.usacase;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.entitiy.Member;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetFollowMemberUsacase {
    private final MemberReadService memberReadService;

    private final FollowReadService followReadService;
    /*
    * 1. fromMemberId = memberId => Follow List
    * 2. 1번을 순회하면서 회원정보 get
    * */
    public List<MemberDto> excute(Long memberId){
        var followings = followReadService.getFollowings(memberId);
        var followingMemberIds = followings.stream().map(f -> f.getToMemberId()).collect(Collectors.toList());

        return memberReadService.getMembers(followingMemberIds);
    }
}

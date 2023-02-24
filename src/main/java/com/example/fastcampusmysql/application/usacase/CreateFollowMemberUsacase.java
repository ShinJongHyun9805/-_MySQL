package com.example.fastcampusmysql.application.usacase;

import com.example.fastcampusmysql.domain.follow.service.FollowWriteService;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateFollowMemberUsacase {

    private final MemberReadService memberReadService;

    private final FollowWriteService followWriteService;

    /*
     * 1. 입력받은 memberId로 회원조회
     * 2. FoolowWriteSerivce.create()
     * */
    public void execute(Long fromMemberId, Long toMemberId){
        var fromMember = memberReadService.getMember(fromMemberId);
        var toMember = memberReadService.getMember(toMemberId);

        followWriteService.create(fromMember, toMember);
    }
}

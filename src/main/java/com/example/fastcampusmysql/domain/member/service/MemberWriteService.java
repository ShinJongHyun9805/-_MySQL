package com.example.fastcampusmysql.domain.member.service;

import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.entitiy.Member;
import com.example.fastcampusmysql.domain.member.entitiy.MemberNicknameHistory;
import com.example.fastcampusmysql.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberWriteService {

    private final MemberRepository memberRepository;

    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;


    public Member create(RegisterMemberCommand command){
        Member member = Member.builder()
                .nickname(command.getNickname())
                .email(command.getEmail())
                .birthDay(command.getBirthDay())
                .build();

        var savedMember = memberRepository.save(member);
        saveMemberNicknameHistory(savedMember);

        return savedMember;
    }

    public void chgNickName(Long memberId, String nickname){
        /**
         *  1. 회원의 이름을 변경
         *  2. 변경 내역을 저장한다.
         * */
       var member = memberRepository.findById(memberId).orElseThrow();

        member.changeNickName(nickname);
        memberRepository.save(member);

        saveMemberNicknameHistory(member);
    }

    public void saveMemberNicknameHistory(Member member){
        var history = MemberNicknameHistory.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();

        memberNicknameHistoryRepository.save(history);
    }
}



package com.example.fastcampusmysql.domain.member.service;


import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.MemberNicknameHistoryDto;
import com.example.fastcampusmysql.domain.member.entitiy.Member;
import com.example.fastcampusmysql.domain.member.entitiy.MemberNicknameHistory;
import com.example.fastcampusmysql.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberReadService {

    private final MemberRepository memberRepository;

    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;

    public MemberDto getMember(Long id){
        var member = memberRepository.findById(id).orElseThrow();
        return toDto(member);
    }

    /*
     * 해당 유저의 Follow 회원을 찾음.
     * */
    public List<MemberDto> getMembers(List<Long> ids){
        var members = memberRepository.findAllByIdIn(ids);
        return members.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<MemberNicknameHistoryDto> getNicknameHistories(Long memberId){
        return memberNicknameHistoryRepository.findAllByMemberId(memberId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public MemberDto toDto(Member member){
        return new MemberDto(member.getId(), member.getEmail(), member.getBirthDay());
    }

    private MemberNicknameHistoryDto toDto(MemberNicknameHistory history){
        return new MemberNicknameHistoryDto(
          history.getId(),
          history.getMemberId(),
          history.getNickname(),
          history.getCreatedAt());
    }
}

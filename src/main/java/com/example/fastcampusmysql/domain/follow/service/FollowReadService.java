package com.example.fastcampusmysql.domain.follow.service;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.respository.FollowRepository;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.entitiy.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowReadService {

    private final FollowRepository followRepository;

    public List<Follow> getFollowings(Long fromMemberId){
        return followRepository.findAllByFromMemberId(fromMemberId);
    }

}

package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entitiy.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    public Member save(Member member){

        return member;
    }
}

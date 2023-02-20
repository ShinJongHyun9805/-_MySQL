package com.example.fastcampusmysql.domain.member.entitiy;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberNicknameHistory {

    private final Long id;

    private final Long memberId;

    private final String nickname;

    private final LocalDateTime createdAt;

    @Builder
    public MemberNicknameHistory(Long id, Long memberId, String nickname, LocalDateTime createdAt){
        this.id = id;
        this.memberId = memberId;
        this.nickname = nickname;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }
}

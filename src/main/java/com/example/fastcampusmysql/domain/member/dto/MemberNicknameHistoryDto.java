package com.example.fastcampusmysql.domain.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MemberNicknameHistoryDto {

    private Long id;
    private Long memberId;
    private String nickname;
    private LocalDateTime createdAt;

    public  MemberNicknameHistoryDto (Long id, Long memberId, String nickname, LocalDateTime createdAt){
        this.id = id;
        this.memberId = memberId;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

}

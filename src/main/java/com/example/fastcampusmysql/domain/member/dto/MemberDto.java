package com.example.fastcampusmysql.domain.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String email;
    private String nickname;
    private LocalDate birthDay;

    public MemberDto(Long id, String email, LocalDate birthDay) {
        this.id = id;
        this.email = email;
        this.birthDay = birthDay;
    }
}

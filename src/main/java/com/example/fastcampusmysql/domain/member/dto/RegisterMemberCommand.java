package com.example.fastcampusmysql.domain.member.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterMemberCommand {

    private String email;
    private String nickname;
    private LocalDate birthDay;
}

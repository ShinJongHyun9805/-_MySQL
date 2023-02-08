package com.example.fastcampusmysql.domain.member.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RegisterMemberCommand {

    private String email;
    private String nickname;
    private LocalDate birthDay;
    private LocalDateTime createdAt;
}

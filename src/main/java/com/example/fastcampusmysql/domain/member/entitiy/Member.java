package com.example.fastcampusmysql.domain.member.entitiy;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Member {

    private final Long id;

    private String nickname;

    private final String email;

    private final LocalDate birthDay;

    private final LocalDateTime createdAt;

    // 닉네임 제한
    private final static Long NAME_MAX_LEN = 10L;

    @Builder
    public Member(Long id, String nickname, String email, LocalDate birthDay, LocalDateTime createdAt) {
        this.id = id;
        this.email = Objects.requireNonNull(email);
        this.birthDay = Objects.requireNonNull(birthDay);

        validateNickname(nickname);
        this.nickname = Objects.requireNonNull(nickname);

        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    // 닉네임 변경
    public void changeNickName(String name) {
        Objects.requireNonNull(name);
        validateNickname(name);

        nickname = name;
    }

    // 닉네임 체크
    private void validateNickname(String nickname)
    {
        Assert.isTrue(nickname.length() <= NAME_MAX_LEN, "최대 길이 초과");
    }
}

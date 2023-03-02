package com.example.fastcampusmysql.domain.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class DailyPostCount {

    private Long memberId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private Long postCount;

    @Builder
    public DailyPostCount(Long memberId, LocalDate date, Long postCount) {
        this.memberId = memberId;
        this.date = date;
        this.postCount = postCount;
    }
}

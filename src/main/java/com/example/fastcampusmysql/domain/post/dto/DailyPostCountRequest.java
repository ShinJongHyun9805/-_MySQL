package com.example.fastcampusmysql.domain.post.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class DailyPostCountRequest {

    private Long memberId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate firstDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate lastDate;

//    @Builder
//    public DailyPostCountRequest(Long memberId, LocalDate firstDate, LocalDate lastDate) {
//        this.memberId = Objects.requireNonNull(memberId);
//        this.firstDate = firstDate == null ? LocalDate.now() : firstDate;
//        this.lastDate = lastDate == null ? LocalDate.now() : lastDate;
//    }
}

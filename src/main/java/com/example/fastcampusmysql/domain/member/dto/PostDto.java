package com.example.fastcampusmysql.domain.member.dto;

import com.example.fastcampusmysql.domain.post.entity.Post;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String contents;
    private LocalDateTime createdAt;
    private Long likeCount;

    public PostDto(Long id, String contents, LocalDateTime createdAt, Long likeCount) {
        this.id = id;
        this.contents = contents;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
    }
}

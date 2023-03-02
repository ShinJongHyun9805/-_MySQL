package com.example.fastcampusmysql.domain.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PostCommand {

    private Long memberId;

    private String content;
}

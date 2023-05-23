package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.respository.FollowRepository;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostWriteService {

    private final PostRepository postRepository;

    public Long create(PostCommand postCommand){
        var post = Post.builder()
                .memberId(postCommand.getMemberId())
                .contents(postCommand.getContent())
                .build();

       return postRepository.save(post).getId();
    }

    @Transactional
    public void likePost(Long postId){
        var post = postRepository.findById(postId, true).orElseThrow();
        post.incrementLikCount();

        postRepository.save(post);
    }

}

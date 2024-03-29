package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.member.dto.PostDto;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostLikeRepository;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostReadService {

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;

    public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request){
        return postRepository.groupByCreatedDate(request);
    }

    // offset 페이징
    public Page<PostDto> getPosts(Long memberId, Pageable pageable){
        return postRepository.findAllByMemberId(memberId, pageable).map(this::toDto);
    }

    private PostDto toDto(Post post){
        return new PostDto(post.getId(), post.getContents(), post.getCreatedAt(), postLikeRepository.count(post.getId()));
    }

    // cursor 페이징
    public PageCursor<Post> getPosts(Long memberId, CursorRequest request){
        var posts = findAllBy(memberId, request);

        var nextKey = posts.stream()
                .mapToLong(Post::getId)
                .min()
                .orElse(request.NONE_KEY);

        return new PageCursor<>(request.next(nextKey), posts);
    }

    public Post getPost(Long postId){
        return postRepository.findById(postId, false).orElseThrow();
    }

    private List<Post> findAllBy(Long memberId, CursorRequest request){
        if(request.hasKey()) {
            return postRepository.findAllBylessThenIdAndMemberIdOrderByIdDesc(request.getKey(), memberId, request.getSize());
        }
        return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, request.getSize());
    }

    // cursor 페이징 다수 멤버Id
    public PageCursor<Post> getPosts(List<Long> memberIds, CursorRequest request){
        var posts = findAllBy(memberIds, request);

        var nextKey = posts.stream()
                .mapToLong(Post::getId)
                .min()
                .orElse(request.NONE_KEY);

        return new PageCursor<>(request.next(nextKey), posts);
    }

    private List<Post> findAllBy(List<Long> memberIds, CursorRequest request){
        if(request.hasKey()) {
            return postRepository.findAllByInlessThenIdAndMemberIdsOrderByIdDesc(request.getKey(), memberIds, request.getSize());
        }
        return postRepository.findAllByInMemberIdAndOrderByIdsDesc(memberIds, request.getSize());
    }

    public List<Post> getPosts(List<Long> ids){
        return postRepository.findAllyInId(ids);
    }

}
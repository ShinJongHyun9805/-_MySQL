package com.example.fastcampusmysql.domain.post;


import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.PostFixtureFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class PostBulkInsertTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void bulkInsert(){
        var easyRandom = PostFixtureFactory.get(
                3L,
                LocalDate.of(2023, 4, 1),
                LocalDate.of(2023,5,1)
        );

        // 시간재기
        var stopWatch = new StopWatch();
        stopWatch.start();

        // OutOfMemory 시, C+S+A Edit VM -> Xmx4096m
        // C+A+S gradle -> run tests using : IntelliJ으로 하여 늘려준 메모리 적용
        var posts = IntStream.range(0, 1000000)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Post.class))
                .collect(Collectors.toList());

        stopWatch.stop();
        System.out.println("객체 생성시간 : " + stopWatch.getTotalTimeSeconds());

        var queryStopWatch = new StopWatch();
        queryStopWatch.start();

        postRepository.bulkInsert(posts);

        queryStopWatch.stop();
        System.out.println("DB Insert 시간 : " + queryStopWatch.getTotalTimeSeconds());
    }
}

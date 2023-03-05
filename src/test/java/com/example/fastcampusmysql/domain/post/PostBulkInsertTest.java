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
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023,4,1)
        );

        // 시간재기
        var stopWatch = new StopWatch();
        stopWatch.start();

        // TODO : parallel() ?, Data 백만건 넣으면 OutOfMemory 뜸. 해결못해서 삼십만건씩 Insert
        var posts = IntStream.range(0, 200000)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Post.class))
                .collect(Collectors.toList());
        stopWatch.stop();
        System.out.println("객체 생성시간 : " + stopWatch.getTotalTimeSeconds()
            + " 몇개: " + posts.size()
                );

        postRepository.bulkInsert(posts);
    }
}

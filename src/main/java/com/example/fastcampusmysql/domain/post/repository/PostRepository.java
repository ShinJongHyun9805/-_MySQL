package com.example.fastcampusmysql.domain.post.repository;


import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostRepository {

    private final String TABLE = "Post";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static RowMapper<DailyPostCount> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> DailyPostCount.builder()
            .memberId(resultSet.getLong("memberId"))
            .date(resultSet.getObject("createdDate", LocalDate.class))
            .postCount(resultSet.getLong("count"))
            .build();

    public Post save(Post post){
        if (post.getId() == null){
            return insert(post);
        }

        throw new UnsupportedOperationException("Follow는 갱신을 지원하지 않습니다.");
    }

    private Post insert(Post post){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");


        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        var id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return Post.builder()
                .id(id)
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .createdDate(post.getCreatedDate())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request){
        var sql = String.format("SELECT " +
                "createdDate, memberId, count(id) AS count " +
                "FROM %s " +
                "WHERE memberId = :memberId AND createdDate BETWEEN :firstDate AND :lastDate " +
                "GROUP BY memberId, createdDate", TABLE);

        var params = new BeanPropertySqlParameterSource(request);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }
}
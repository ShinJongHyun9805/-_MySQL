package com.example.fastcampusmysql.domain.post.repository;


import com.example.fastcampusmysql.util.PageHelper;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PostRepository {

    private final String TABLE = "Post";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static RowMapper<Post> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Post.builder()
            .id(resultSet.getLong("id"))
            .memberId(resultSet.getLong("memberId"))
            .contents(resultSet.getString("contents"))
            .createdDate(resultSet.getObject("createdDate", LocalDate.class))
            .likeCount(resultSet.getLong("likeCount"))
            .version(resultSet.getLong("version"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();


    private final static RowMapper<DailyPostCount> DAILY_POST_COUNT_ROW_MAPPER = (ResultSet resultSet, int rowNum) -> DailyPostCount.builder()
            .memberId(resultSet.getLong("memberId"))
            .date(resultSet.getObject("createdDate", LocalDate.class))
            .postCount(resultSet.getLong("count"))
            .build();

    public Post save(Post post){
        if (post.getId() == null){
            return insert(post);
        }

        return update(post);
    }

    public void bulkInsert(List<Post> posts){
        var sql = String.format("INSERT INTO %s " +
                "(memberId, contents, createdDate, createdAt)" +
                " VALUES (:memberId, :contents, :createdDate, :createdAt)", TABLE);

        SqlParameterSource[] params = posts
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, params);
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

    private Post update(Post post){
        var sql = String.format("UPDATE %s SET " +
                "memberId = :memberId " +
                ",contents = :contents " +
                ",createdDate = :createdDate " +
                ",likeCount = :likeCount " +
                ",createdAt = :createdAt " +
                ",version = :version + 1 " +
                "WHERE id = :id AND version = :version", TABLE);

        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        var updatedCount = namedParameterJdbcTemplate.update(sql, params);

        if (updatedCount == 0){
            throw new RuntimeException("Update  실패");
        }

        return post;
    }

    public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request){
        var sql = String.format("SELECT " +
                "createdDate, memberId, count(id) AS count " +
                "FROM %s " +
                "WHERE memberId = :memberId AND createdDate BETWEEN :firstDate AND :lastDate " +
                "GROUP BY memberId, createdDate", TABLE);

        var params = new BeanPropertySqlParameterSource(request);

        return namedParameterJdbcTemplate.query(sql, params, DAILY_POST_COUNT_ROW_MAPPER);
    }

    // offset 페이징
    public Page<Post> findAllByMemberId(Long memberId, Pageable pageable){
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());

        var sql = String.format("SELECT * FROM %s " +
                "WHERE memberId = :memberId " +
                "ORDER BY %s " +
                "LIMIT :size " +
                "OFFSET :offset", TABLE, PageHelper.orderBy(pageable.getSort()));

        var posts =  namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);

        return new PageImpl(posts, pageable, getCount(memberId));
    }

    public Optional<Post> findById(Long postId, Boolean requiredLock){
        var sql = String.format("SELECT * FROM %s WHERE id = :postId ", TABLE);
        if (requiredLock){
            sql += "FOR UPDATE";
        }

        var params = new MapSqlParameterSource()
                .addValue("postId", postId);
        var nullablePost = namedParameterJdbcTemplate.queryForObject(sql, params, ROW_MAPPER);

        return Optional.ofNullable(nullablePost);
    }

    private Long getCount(Long memberId) {
        var sql = String.format("SELECT COUNT(id) " +
                "FROM %s " +
                "WHERE memberId = :memberId ", TABLE);

        var params = new MapSqlParameterSource().addValue("memberId", memberId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public List<Post> findAllyInId(List<Long> ids){
        if (ids.isEmpty()){
            return List.of();
        }

        var sql = String.format("SELECT * " +
                "FROM %s " +
                "WHERE id IN (:ids )", TABLE);

        var params = new MapSqlParameterSource()
                .addValue("ids", ids);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    // cursor 페이징
    public List<Post> findAllByMemberIdAndOrderByIdDesc(Long memberId, int size){
        var sql = String.format("SELECT * " +
                "FROM %s " +
                "WHERE memberId = :memberId " +
                "ORDER BY id DESC " +
                "LIMIT :size ", TABLE);

        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findAllBylessThenIdAndMemberIdOrderByIdDesc(Long id, Long memberId, int size){
        var sql = String.format("SELECT * " +
                "FROM %s " +
                "WHERE memberId = :memberId AND id < :id " +
                "ORDER BY id DESC " +
                "LIMIT :size ", TABLE);

        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("id", id)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    // cursor 페이징 다수 멤버id
    public List<Post> findAllByInMemberIdAndOrderByIdsDesc(List<Long> memberIds, int size){
        if(memberIds.isEmpty()){
            return List.of();
        }

        var sql = String.format("SELECT * " +
                "FROM %s " +
                "WHERE memberId IN (:memberIds )" +
                "ORDER BY id DESC " +
                "LIMIT :size ", TABLE);

        var params = new MapSqlParameterSource()
                .addValue("memberIds", memberIds)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findAllByInlessThenIdAndMemberIdsOrderByIdDesc(Long id, List<Long> memberIds, int size){
        if(memberIds.isEmpty()){
            return List.of();
        }

        var sql = String.format("SELECT * " +
                "FROM %s " +
                "WHERE memberId IN (:memberIds ) AND id < :id " +
                "ORDER BY id DESC " +
                "LIMIT :size ", TABLE);

        var params = new MapSqlParameterSource()
                .addValue("memberIds", memberIds)
                .addValue("id", id)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }
}

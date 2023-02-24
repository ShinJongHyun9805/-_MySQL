package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entitiy.Member;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
public class MemberRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String TABLE = "member";

    private final static RowMapper<Member> rowMapper = (ResultSet resultSet, int rowNum) -> Member.builder()
            .id(resultSet.getLong("id"))
            .email(resultSet.getString("email"))
            .nickname(resultSet.getString("nickname"))
            .birthDay(resultSet.getObject("birthday", LocalDate.class))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public Optional<Member> findById(Long id){
        var sql = String.format("SELECT * FROM %s WHERE id = :id", TABLE);
        var param = new MapSqlParameterSource()
                .addValue("id", id);

        var member = namedParameterJdbcTemplate.queryForObject(sql, param, rowMapper);
        return Optional.ofNullable(member);
    }

    /*
    * 해당 유저의 Follow 회원을 찾음.
    * */
    public List<Member> findAllByIdIn(List<Long> ids){
        // list 빈 값 처리
        if (ids.isEmpty()){
            return List.of();
        }

        var sql = String.format("SELECT * FROM %S WHERE id in (:ids)", TABLE);
        var params = new MapSqlParameterSource().addValue("ids", ids);

        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public Member save(Member member){
        if (member.getId() == null){
            return insert(member);
        }

        return update(member);
    }

    private Member insert(Member member){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("member")
                .usingGeneratedKeyColumns("id");


        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        var id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return Member.builder()
                .id(id)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .birthDay(member.getBirthDay())
                .createdAt(member.getCreatedAt())
                .build();
    }

    private Member update(Member member){
        var sql = String.format("UPDATE %s SET email = :email, nickname = :nickname," +
                "birthDay = :birthDay WHERE id = :id", TABLE);
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        namedParameterJdbcTemplate.update(sql, params);

        return member;
    }
}

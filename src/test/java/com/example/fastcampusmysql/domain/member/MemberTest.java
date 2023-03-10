package com.example.fastcampusmysql.domain.member;


import com.example.fastcampusmysql.util.MemberFlxtureFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTest {

    @DisplayName("회원은 닉네임을 변경할 수 있다.")
    @Test
    public void testChangeName() {
//        LongStream.range(0, 10)
//                .mapToObj(i -> MemberFlxtureFactory.create(i))
//                .forEach(member -> {
//                    System.out.println("memberName: " + member.getNickname());
//                });

        var member = MemberFlxtureFactory.create();
        var expected = "pnu";

        member.changeNickName(expected);

        Assertions.assertEquals(expected, member.getNickname());
    }

    @DisplayName("닉네임은 10자를 초과할 수 없다.")
    @Test
    public void testNickNameMaxLength() {
        var member = MemberFlxtureFactory.create();
        var overMaxLengthName = "qwerasdfzxcvq";

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            member.changeNickName(overMaxLengthName);
        });
    }
}

package com.example.fastcampusmysql.util;

import com.example.fastcampusmysql.domain.member.entitiy.Member;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class MemberFlxtureFactory {

    public static Member create(){
        var param = new EasyRandomParameters();

        return new EasyRandom(param).nextObject(Member.class);
    }

    public static Member create(Long seed){
        // EasyRandom Test 시 변수를 알아서 set 해줌.
        var param = new EasyRandomParameters().seed(seed);

        return new EasyRandom(param).nextObject(Member.class);
    }

}

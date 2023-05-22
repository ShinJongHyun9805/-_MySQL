package com.example.fastcampusmysql.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursorRequest {
    public static final Long NONE_KEY = -1L;

    private Long key;
    private int size;

    public CursorRequest(Long key) {
        this.key = key;
    }

    public Boolean hasKey(){
        return key != null;
    }

    public CursorRequest next(Long key, int size){
        return new CursorRequest(key, size);
    }

    public CursorRequest next(Long key){
        return new CursorRequest(key);
    }
}

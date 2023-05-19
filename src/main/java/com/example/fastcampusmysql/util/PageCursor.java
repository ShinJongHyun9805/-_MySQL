package com.example.fastcampusmysql.util;

import lombok.RequiredArgsConstructor;

import java.util.List;


public class PageCursor<T> {
    private final CursorRequest nextCursorRequest;
    private final List<T> body;

    public PageCursor(CursorRequest nextCursorRequest, List<T> body) {
        this.nextCursorRequest = nextCursorRequest;
        this.body = body;
    }

    public CursorRequest getNextCursorRequest() {
        return nextCursorRequest;
    }

    public List<T> getBody() {
        return body;
    }

}

package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.domain.post.repository.TimelineRepository;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TimelineReadService {

    private final TimelineRepository timelineRepository;

    public PageCursor<Timeline> getTimeline(Long memberId, CursorRequest request){
        var timelines =  findAllBy(memberId, request);
        var nextKey = timelines.stream()
                .mapToLong(Timeline::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);

        return new PageCursor<>(request.next(nextKey), timelines);
    }

    private List<Timeline> findAllBy(Long memberIds, CursorRequest request){
        if(request.hasKey()) {
            return timelineRepository.findAllBylessThenIdAndMemberIdOrderByIdDesc(request.getKey(), memberIds, request.getSize());
        }
        return timelineRepository.findAllByMemerIdAndOrderyIdDesc(memberIds, request.getSize());
    }
}

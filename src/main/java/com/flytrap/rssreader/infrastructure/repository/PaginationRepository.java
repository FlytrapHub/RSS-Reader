package com.flytrap.rssreader.infrastructure.repository;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public interface PaginationRepository {

    default <T> Slice<T> checkLastPage(int pageSize, List<T> results) {

        boolean hasNext = false; //다음으로 가져올 데이터가 있는 지 여부를 알려줌

        if (results.size() > pageSize) {
            hasNext = true;  //읽어 올 데이터가 있다면 true를 반환
            results.remove(pageSize);
        }

        return new SliceImpl<>(results, PageRequest.ofSize(pageSize), hasNext);
    }
}


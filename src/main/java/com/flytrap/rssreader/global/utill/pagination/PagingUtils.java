package com.flytrap.rssreader.global.utill.pagination;

import com.flytrap.rssreader.infrastructure.repository.output.PostPaginationOutput;
import java.util.List;

public final class PagingUtils {

    private PagingUtils() {
    }

    public static String setNextCursor(List<PostPaginationOutput> content, boolean hasNext) {
        String nextCursor = null;
        if (hasNext) {
            //공백같은게 생깁니다 trim을사용
            String cursor = content.get(content.size() - 1).cursor();
            nextCursor = cursor.trim();
        }
        return nextCursor;
    }
}

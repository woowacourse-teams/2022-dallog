package com.allog.dallog.global.dto;

import java.util.List;

public class FindSliceResponse<T> extends ListResponse<T> {

    private int page;
    private long totalCount;

    private FindSliceResponse() {
        super();
    }

    public FindSliceResponse(final int page, final long totalCount, final List<T> categoryResponses) {
        super(categoryResponses);
        this.page = page;
        this.totalCount = totalCount;
    }

    public int getPage() {
        return page;
    }

    public long getTotalCount() {
        return totalCount;
    }
}

package com.allog.dallog.global.dto;

import java.util.List;

public class FindByPageResponse<T> extends ListResponse<T> {

    private static final int START_PAGE_INDEX = 1;

    private int page;

    private FindByPageResponse() {
        super();
    }

    public FindByPageResponse(final int page, final List<T> responses) {
        super(responses);
        this.page = page + START_PAGE_INDEX;
    }

    public int getPage() {
        return page;
    }
}

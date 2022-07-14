package com.allog.dallog.global.dto;

import java.util.List;

public class FindByPageResponse<T> extends ListResponse<T> {

    private int page;

    private FindByPageResponse() {
        super();
    }

    public FindByPageResponse(final int page, final List<T> responses) {
        super(responses);
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}

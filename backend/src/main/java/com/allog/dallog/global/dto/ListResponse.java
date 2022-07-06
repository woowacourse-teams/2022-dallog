package com.allog.dallog.global.dto;

import java.util.List;

public class ListResponse<T> {

    private List<T> data;

    private ListResponse() {
    }

    public ListResponse(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }
}

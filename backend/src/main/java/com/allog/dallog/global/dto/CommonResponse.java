package com.allog.dallog.global.dto;

public class CommonResponse<T> {

    private T data;

    private CommonResponse() {
    }

    public CommonResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}

package com.allog.dallog.categoryrole.domain;

public enum CategoryAuthority {
    UPDATE_CATEGORY("카테고리 수정"),
    DELETE_CATEGORY("카테고리 제거"),
    ADD_SCHEDULE("일정 추가"),
    UPDATE_SCHEDULE("일정 수정"),
    DELETE_SCHEDULE("일정 제거"),
    CHANGE_ROLE_OF_SUBSCRIBER("역할 변경"),
    FIND_SUBSCRIBERS("카테고리 구독자 조회");

    private final String name;

    CategoryAuthority(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

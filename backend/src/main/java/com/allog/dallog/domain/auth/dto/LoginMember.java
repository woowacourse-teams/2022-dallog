package com.allog.dallog.domain.auth.dto;

public class LoginMember {

    private Long id;

    private LoginMember() {
    }

    public LoginMember(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

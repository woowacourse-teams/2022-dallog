package com.allog.dallog.auth.dto;

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

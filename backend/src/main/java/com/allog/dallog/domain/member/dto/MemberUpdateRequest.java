package com.allog.dallog.domain.member.dto;

public class MemberUpdateRequest {

    private String displayName;

    private MemberUpdateRequest() {
    }

    public MemberUpdateRequest(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

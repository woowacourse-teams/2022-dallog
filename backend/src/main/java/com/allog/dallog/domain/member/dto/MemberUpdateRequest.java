package com.allog.dallog.domain.member.dto;

import javax.validation.constraints.NotBlank;

public class MemberUpdateRequest {

    @NotBlank(message = "공백일 수 없습니다.")
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

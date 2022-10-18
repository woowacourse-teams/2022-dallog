package com.allog.dallog.domain.member.dto.request;

import javax.validation.constraints.NotBlank;

public class MemberUpdateRequest {

    @NotBlank(message = "회원 이름이 공백일 수 없습니다.")
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

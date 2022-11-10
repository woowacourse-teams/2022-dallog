package com.allog.dallog.domain.categoryrole.dto.response;

import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleType;
import com.allog.dallog.domain.member.dto.response.MemberResponse;

public class MemberWithRoleTypeResponse {

    private MemberResponse member;
    private CategoryRoleType categoryRoleType;

    private MemberWithRoleTypeResponse() {
    }

    public MemberWithRoleTypeResponse(final CategoryRole categoryRole) {
        this.member = new MemberResponse(categoryRole.getMember());
        this.categoryRoleType = categoryRole.getCategoryRoleType();
    }

    public MemberResponse getMember() {
        return member;
    }

    public CategoryRoleType getCategoryRoleType() {
        return categoryRoleType;
    }
}

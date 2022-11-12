package com.allog.dallog.categoryrole.dto.response;

import com.allog.dallog.categoryrole.domain.CategoryRoleType;
import com.allog.dallog.categoryrole.domain.CategoryRole;
import com.allog.dallog.member.dto.response.MemberResponse;

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

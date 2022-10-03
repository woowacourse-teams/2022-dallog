package com.allog.dallog.domain.member.dto.response;

import com.allog.dallog.domain.categoryrole.domain.CategoryRoleType;
import com.allog.dallog.domain.member.domain.Member;

public class MemberWithRoleTypeResponse {

    private MemberResponse member;
    private CategoryRoleType categoryRoleType;

    private MemberWithRoleTypeResponse() {
    }

    public MemberWithRoleTypeResponse(final Member member, final CategoryRoleType categoryRoleType) {
        this.member = new MemberResponse(member);
        this.categoryRoleType = categoryRoleType;
    }

    public MemberResponse getMember() {
        return member;
    }

    public CategoryRoleType getCategoryRoleType() {
        return categoryRoleType;
    }
}

package com.allog.dallog.domain.member.dto.response;

import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import java.util.List;
import java.util.stream.Collectors;

public class SubscribersResponse {

    private List<MemberWithRoleTypeResponse> subscribers;

    private SubscribersResponse() {
    }

    public SubscribersResponse(final List<CategoryRole> categoryRoles) {
        this.subscribers = toResponses(categoryRoles);
    }

    private List<MemberWithRoleTypeResponse> toResponses(final List<CategoryRole> categoryRoles) {
        return categoryRoles.stream()
                .map(MemberWithRoleTypeResponse::new)
                .collect(Collectors.toList());
    }

    public List<MemberWithRoleTypeResponse> getSubscribers() {
        return subscribers;
    }
}

package com.allog.dallog.domain.member.dto.response;

import com.allog.dallog.domain.member.domain.Member;
import java.util.List;
import java.util.stream.Collectors;

public class SubscribersResponse {

    private List<MemberWithRoleTypeResponse> subscribers;

    private SubscribersResponse() {
    }

    public SubscribersResponse(final List<MemberWithRoleTypeResponse> subscribers) {
        this.subscribers = subscribers;
    }

    private List<MemberResponse> toMemberResponses(final List<Member> members) {
        return members.stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());
    }

    public List<MemberWithRoleTypeResponse> getSubscribers() {
        return subscribers;
    }
}

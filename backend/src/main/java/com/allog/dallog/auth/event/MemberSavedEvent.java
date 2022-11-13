package com.allog.dallog.auth.event;

public class MemberSavedEvent {

    private final Long memberId;

    public MemberSavedEvent(final Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}

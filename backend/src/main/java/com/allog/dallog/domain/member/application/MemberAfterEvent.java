package com.allog.dallog.domain.member.application;

public interface MemberAfterEvent {

    void process(final Object... args);
}

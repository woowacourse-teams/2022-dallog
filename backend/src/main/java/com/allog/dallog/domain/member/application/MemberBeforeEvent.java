package com.allog.dallog.domain.member.application;

public interface MemberBeforeEvent {

    void process(final Object... args);
}

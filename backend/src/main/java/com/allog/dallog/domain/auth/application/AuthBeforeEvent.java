package com.allog.dallog.domain.auth.application;

public interface AuthBeforeEvent {

    void process(Object... args);
}

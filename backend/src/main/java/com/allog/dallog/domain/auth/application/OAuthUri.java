package com.allog.dallog.domain.auth.application;

@FunctionalInterface
public interface OAuthUri {

    String generate(final String redirectUri);
}

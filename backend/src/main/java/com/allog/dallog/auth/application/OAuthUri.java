package com.allog.dallog.auth.application;

@FunctionalInterface
public interface OAuthUri {

    String generate(final String redirectUri);
}

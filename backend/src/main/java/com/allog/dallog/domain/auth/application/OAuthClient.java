package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.dto.OAuthMember;

@FunctionalInterface
public interface OAuthClient {

    OAuthMember getOAuthMember(final String code);
}

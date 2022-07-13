package com.allog.dallog.infrastructure.oauth.client;

import com.allog.dallog.infrastructure.oauth.dto.OAuthMember;

@FunctionalInterface
public interface OAuthClient {

    OAuthMember getOAuthMember(final String code);
}

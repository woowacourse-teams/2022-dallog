package com.allog.dallog.auth.support;

import com.allog.dallog.auth.dto.OAuthMember;

@FunctionalInterface
public interface OAuthClient {

    OAuthMember getOAuthMember(final String code);
}

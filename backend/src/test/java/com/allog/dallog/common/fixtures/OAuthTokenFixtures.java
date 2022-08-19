package com.allog.dallog.common.fixtures;

import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.member.domain.Member;

public class OAuthTokenFixtures {

    public static final String REFRESH_TOKEN = "adasdqwrwggsdfsdfaasfadfsdvsvzsdrw";

    public static OAuthToken OAUTH_TOKEN(final Member member) {
        return new OAuthToken(member, REFRESH_TOKEN);
    }
}

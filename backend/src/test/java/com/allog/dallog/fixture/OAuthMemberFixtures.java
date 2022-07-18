package com.allog.dallog.fixture;

import com.allog.dallog.auth.dto.OAuthMember;

public class OAuthMemberFixtures {

    public static final String CODE = "authorization code";

    public static final String EMAIL = "example@email.com";
    public static final String DISPLAY_NAME = "example";
    public static final String PROFILE_IMAGE_URI = "/image.png";

    public static final OAuthMember OAUTH_MEMBER = new OAuthMember(EMAIL, DISPLAY_NAME, PROFILE_IMAGE_URI);
}

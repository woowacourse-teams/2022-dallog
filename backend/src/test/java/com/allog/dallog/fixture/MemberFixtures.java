package com.allog.dallog.fixture;

import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.SocialType;

public class MemberFixtures {

    public static final String EMAIL = "example@email.com";
    public static final String PROFILE_IMAGE_URI = "/image.png";
    public static final String DISPLAY_NAME = "example";

    public static final Member MEMBER = new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE);
}

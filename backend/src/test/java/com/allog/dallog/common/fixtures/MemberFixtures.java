package com.allog.dallog.common.fixtures;

import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.SocialType;

public class MemberFixtures {

    public static final String 파랑_이메일 = "parang@email.com";
    public static final String 파랑_프로필 = "/parang.png";
    public static final String 파랑_이름 = "파랑";

    public static final String EMAIL = "example@email.com";
    public static final String PROFILE_IMAGE_URI = "/image.png";
    public static final String DISPLAY_NAME = "example";

    public static final Member MEMBER = new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE);
    public static final Member CREATOR = new Member("creator@email.com", "/image.png", "creator", SocialType.GOOGLE);
    public static final Member CREATOR2 = new Member("creator2@eamil.com", "/image.png", "creator2", SocialType.GOOGLE);
}

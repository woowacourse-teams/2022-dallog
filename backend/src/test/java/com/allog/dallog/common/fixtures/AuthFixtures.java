package com.allog.dallog.common.fixtures;

import static com.allog.dallog.common.fixtures.MemberFixtures.리버_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.리버_이메일;
import static com.allog.dallog.common.fixtures.MemberFixtures.리버_프로필;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_이메일;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_프로필;

import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.TokenRequest;
import com.allog.dallog.domain.auth.dto.TokenResponse;

public class AuthFixtures {

    public static final String GOOGLE_PROVIDER = "google";
    public static final String OAUTH_PROVIDER = "oauthProvider";

    public static final String STUB_MEMBER_인증_코드 = "member authorization code";
    public static final String STUB_CREATOR_인증_코드 = "creator authorization code";

    public static TokenRequest MEMBER_인증_코드_토큰_요청() {
        return new TokenRequest(STUB_MEMBER_인증_코드);
    }

    public static TokenResponse MEMBER_인증_코드_토큰_응답() {
        return new TokenResponse(STUB_MEMBER_인증_코드);
    }

    public static OAuthMember STUB_OAUTH_MEMBER_파랑() {
        return new OAuthMember(파랑_이메일, 파랑_이름, 파랑_프로필);
    }

    public static OAuthMember STUB_OAUTH_CREATOR_리버() {
        return new OAuthMember(리버_이메일, 리버_이름, 리버_프로필);
    }

    public static final String 더미_엑세스_토큰 = "aaaaa.bbbbb.ccccc";
    public static final String 토큰_정보 = "Bearer " + 더미_엑세스_토큰;

    public static final String OAuth_로그인_링크 = "https://accounts.google.com/o/oauth2/v2/auth";
}

package com.allog.dallog.common.fixtures;

import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.TokenRequest;
import com.allog.dallog.domain.auth.dto.TokenResponse;

public class AuthFixtures {

    public static final String GOOGLE_PROVIDER = "google";
    public static final String OAUTH_PROVIDER = "oauthProvider";

    public static final String 인증_코드 = "authorization code";

    public static TokenRequest 인증_코드_토큰_요청() {
        return new TokenRequest(인증_코드);
    }

    public static TokenResponse 인증_코드_토큰_응답() {
        return new TokenResponse(인증_코드);
    }

    public static final String STUB_이메일 = "stub@email.com";
    public static final String STUB_이름 = "stub";
    public static final String STUB_프로필 = "/image.png";
    public static final OAuthMember STUB_OAUTH_회원 = new OAuthMember(STUB_이메일, STUB_이름, STUB_프로필);

    public static final String 더미_엑세스_토큰 = "aaaaa.bbbbb.ccccc";
    public static final String 토큰_정보 = "Bearer " + 더미_엑세스_토큰;

    public static final String OAuth_로그인_링크 = "https://accounts.google.com/o/oauth2/v2/auth";
}

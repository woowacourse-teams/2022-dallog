package com.allog.dallog.common.fixtures;

import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.request.TokenRequest;
import com.allog.dallog.domain.auth.dto.response.TokenResponse;

public class AuthFixtures {

    public static final String GOOGLE_PROVIDER = "google";
    public static final String OAUTH_PROVIDER = "oauthProvider";

    public static final String STUB_MEMBER_인증_코드 = "member authorization code";
    public static final String STUB_CREATOR_인증_코드 = "creator authorization code";

    public static final String 더미_엑세스_토큰 = "aaaaa.bbbbb.ccccc";
    public static final String 토큰_정보 = "Bearer " + 더미_엑세스_토큰;
    public static final String OAuth_로그인_링크 = "https://accounts.google.com/o/oauth2/v2/auth";

    public static final String MEMBER_이메일 = "member@email.com";
    public static final String MEMBER_이름 = "member";
    public static final String MEMBER_프로필 = "/member.png";
    public static final String MEMBER_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.ccccccccc";

    public static final String CREATOR_이메일 = "creator@email.com";
    public static final String CREATOR_이름 = "creator";
    public static final String CREATOR_프로필 = "/creator.png";
    public static final String CREATOR_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.ccccccccc";

    public static final String 더미_시크릿_키 = "asdfasarspofjkosdfasdjkflikasndflkasndsdfjkadsnfkjasdn";

    public static final String STUB_OAUTH_ACCESS_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.cccccccccc";
    public static final String STUB_OAUTH_EXPIRES_IN = "3599";
    public static final String STUB_OAUTH_SCOPE = "openid";
    public static final String STUB_OAUTH_TOKEN_TYPE = "Bearer";

    public static TokenRequest MEMBER_인증_코드_토큰_요청() {
        return new TokenRequest(STUB_MEMBER_인증_코드, "https://dallog.me/oauth");
    }

    public static TokenResponse MEMBER_인증_코드_토큰_응답() {
        return new TokenResponse(STUB_MEMBER_인증_코드);
    }

    public static OAuthMember STUB_OAUTH_MEMBER() {
        return new OAuthMember(MEMBER_이메일, MEMBER_이름, MEMBER_프로필, MEMBER_REFRESH_TOKEN);
    }

    public static OAuthMember STUB_OAUTH_CREATOR() {
        return new OAuthMember(CREATOR_이메일, CREATOR_이름, CREATOR_프로필, CREATOR_REFRESH_TOKEN);
    }
}

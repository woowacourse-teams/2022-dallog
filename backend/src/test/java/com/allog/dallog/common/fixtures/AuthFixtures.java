package com.allog.dallog.common.fixtures;

import static com.allog.dallog.common.fixtures.OAuthFixtures.관리자;
import static com.allog.dallog.common.fixtures.OAuthFixtures.리버;
import static com.allog.dallog.common.fixtures.OAuthFixtures.매트;
import static com.allog.dallog.common.fixtures.OAuthFixtures.파랑;
import static com.allog.dallog.common.fixtures.OAuthFixtures.후디;

import com.allog.dallog.domain.auth.dto.request.TokenRenewalRequest;
import com.allog.dallog.domain.auth.dto.request.TokenRequest;
import com.allog.dallog.domain.auth.dto.response.AccessAndRefreshTokenResponse;
import com.allog.dallog.domain.auth.dto.response.AccessTokenResponse;

public class AuthFixtures {

    public static final String GOOGLE_PROVIDER = "google";
    public static final String OAUTH_PROVIDER = "oauthProvider";

    public static final String STUB_MEMBER_인증_코드 = "member authorization code";
    public static final String STUB_MEMBER_REFRESH_인증_코드 = "member refresh authorization code";
    public static final String STUB_CREATOR_인증_코드 = "creator authorization code";

    public static final String 더미_엑세스_토큰 = "aaaaa.bbbbb.ccccc";
    public static final String 더미_리프레시_토큰 = "ccccc.bbbbb.aaaaa";
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

    public static TokenRequest 관리자_인증_코드_토큰_요청() {
        return new TokenRequest(관리자.getCode(), "https://dallog.me/oauth");
    }

    public static TokenRequest 파랑_인증_코드_토큰_요청() {
        return new TokenRequest(파랑.getCode(), "https://dallog.me/oauth");
    }

    public static TokenRequest 리버_인증_코드_토큰_요청() {
        return new TokenRequest(리버.getCode(), "https://dallog.me/oauth");
    }

    public static TokenRequest 후디_인증_코드_토큰_요청() {
        return new TokenRequest(후디.getCode(), "https://dallog.me/oauth");
    }

    public static TokenRequest 매트_인증_코드_토큰_요청() {
        return new TokenRequest(매트.getCode(), "https://dallog.me/oauth");
    }

    public static TokenRequest MEMBER_인증_코드_토큰_요청() {
        return new TokenRequest(STUB_MEMBER_인증_코드, "https://dallog.me/oauth");
    }

    public static AccessAndRefreshTokenResponse MEMBER_인증_코드_토큰_응답() {
        return new AccessAndRefreshTokenResponse(STUB_MEMBER_인증_코드, STUB_MEMBER_REFRESH_인증_코드);
    }

    public static TokenRenewalRequest MEMBER_리뉴얼_토큰_요청() {
        return new TokenRenewalRequest(더미_리프레시_토큰);
    }

    public static AccessTokenResponse MEMBER_리뉴얼_토큰_응답() {
        return new AccessTokenResponse(더미_엑세스_토큰);
    }
}

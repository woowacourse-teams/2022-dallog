package com.allog.dallog.common.fixtures;

import com.allog.dallog.domain.auth.dto.OAuthMember;
import java.util.Arrays;
import java.util.NoSuchElementException;

public enum OAuthFixtures {

    관리자("관리자", 관리자()),
    파랑("파랑", 파랑()),
    리버("리버", 리버()),
    후디("후디", 후디()),
    매트("매트", 매트()),
    MEMBER("member authorization code", MEMBER()),
    CREATOR("creator authorization code", CREATOR());

    private String code;
    private OAuthMember oAuthMember;

    OAuthFixtures(final String code, final OAuthMember oAuthMember) {
        this.code = code;
        this.oAuthMember = oAuthMember;
    }

    public static OAuthMember parseOAuthMember(final String code) {
        OAuthFixtures oAuthFixtures = Arrays.stream(values())
                .filter(value -> value.code.equals(code))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        return oAuthFixtures.oAuthMember;
    }

    private static OAuthMember 관리자() {
        String 관리자_이메일 = "dallog.admin@gmail.com";
        String 관리자_이름 = "관리자";
        String 관리자_프로필_URL = "/admin.png";
        String 관리자_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.cccccccccc";
        return new OAuthMember(관리자_이메일, 관리자_이름, 관리자_프로필_URL, 관리자_REFRESH_TOKEN);
    }

    private static OAuthMember 파랑() {
        String 파랑_이메일 = "parang@email.com";
        String 파랑_이름 = "파랑";
        String 파랑_프로필 = "/parang.png";
        String 파랑_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.cccccccccc";
        return new OAuthMember(파랑_이메일, 파랑_이름, 파랑_프로필, 파랑_REFRESH_TOKEN);
    }

    private static OAuthMember 리버() {
        String 리버_이메일 = "leaver@email.com";
        String 리버_이름 = "리버";
        String 리버_프로필 = "/leaver.png";
        String 리버_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.cccccccccc";
        return new OAuthMember(리버_이메일, 리버_이름, 리버_프로필, 리버_REFRESH_TOKEN);
    }

    private static OAuthMember 후디() {
        String 후디_이메일 = "devhudi@gmail.com";
        String 후디_이름 = "후디";
        String 후디_프로필 = "/hudi.png";
        String 후디_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.cccccccccc";
        return new OAuthMember(후디_이메일, 후디_이름, 후디_프로필, 후디_REFRESH_TOKEN);
    }

    private static OAuthMember 매트() {
        String 매트_이메일 = "dev.hyeonic@gmail.com";
        String 매트_이름 = "매트";
        String 매트_프로필 = "/mat.png";
        String 매트_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.cccccccccc";
        return new OAuthMember(매트_이메일, 매트_이름, 매트_프로필, 매트_REFRESH_TOKEN);
    }

    private static OAuthMember MEMBER() {
        String MEMBER_이메일 = "member@email.com";
        String MEMBER_이름 = "member";
        String MEMBER_프로필 = "/member.png";
        String MEMBER_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.ccccccccc";
        return new OAuthMember(MEMBER_이메일, MEMBER_이름, MEMBER_프로필, MEMBER_REFRESH_TOKEN);
    }

    private static OAuthMember CREATOR() {
        String CREATOR_이메일 = "creator@email.com";
        String CREATOR_이름 = "creator";
        String CREATOR_프로필 = "/creator.png";
        String CREATOR_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.ccccccccc";
        return new OAuthMember(CREATOR_이메일, CREATOR_이름, CREATOR_프로필, CREATOR_REFRESH_TOKEN);
    }

    public OAuthMember OAuth_인증을_한다() {
        return oAuthMember;
    }

    public String getCode() {
        return code;
    }
}

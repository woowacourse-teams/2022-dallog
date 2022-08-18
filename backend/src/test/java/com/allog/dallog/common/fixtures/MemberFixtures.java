package com.allog.dallog.common.fixtures;

import static com.allog.dallog.domain.member.domain.SocialType.GOOGLE;

import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.dto.MemberResponse;

public class MemberFixtures {

    /* 관리자 */
    public static final String 관리자_이메일 = "dallog.admin@gmail.com";
    public static final String 관리자_이름 = "관리자";
    public static final String 관리자_프로필 = "/admin.png";
    public static final MemberResponse 관리자_응답 = new MemberResponse(1L, 관리자_이메일, 관리자_이름, 관리자_프로필, GOOGLE);

    /* 파랑 */
    public static final String 파랑_이메일 = "parang@email.com";
    public static final String 파랑_이름 = "파랑";
    public static final String 파랑_프로필 = "/parang.png";
    public static final MemberResponse 파랑_응답 = new MemberResponse(2L, 파랑_이메일, 파랑_이름, 파랑_프로필, GOOGLE);

    /* 리버 */
    public static final String 리버_이메일 = "leaver@email.com";
    public static final String 리버_이름 = "리버";
    public static final String 리버_프로필 = "/leaver.png";
    public static final MemberResponse 리버_응답 = new MemberResponse(3L, 리버_이메일, 리버_이름, 리버_프로필, GOOGLE);

    /* 후디 */
    public static final String 후디_이메일 = "devhudi@gmail.com";
    public static final String 후디_이름 = "후디";
    public static final String 후디_프로필 = "/hudi.png";
    public static final MemberResponse 후디_응답 = new MemberResponse(4L, 후디_이메일, 후디_이름, 후디_프로필, GOOGLE);

    /* 매트 */
    public static final String 매트_이메일 = "dev.hyeonic@gmail.com";
    public static final String 매트_이름 = "매트";
    public static final String 매트_프로필 = "/mat.png";
    public static final MemberResponse 매트_응답 = new MemberResponse(5L, 매트_이메일, 매트_이름, 매트_프로필, GOOGLE);

    public static Member 관리자() {
        return new Member(관리자_이메일, 관리자_이름, 관리자_프로필, GOOGLE);
    }

    public static Member 파랑() {
        return new Member(파랑_이메일, 파랑_이름, 파랑_프로필, GOOGLE);
    }

    public static Member 리버() {
        return new Member(리버_이메일, 리버_이름, 리버_프로필, GOOGLE);
    }

    public static Member 후디() {
        return new Member(후디_이메일, 후디_이름, 후디_프로필, GOOGLE);
    }

    public static Member 매트() {
        return new Member(매트_이메일, 매트_이름, 매트_프로필, GOOGLE);
    }
}

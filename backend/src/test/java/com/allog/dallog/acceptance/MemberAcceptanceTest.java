package com.allog.dallog.acceptance;

import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;

import com.allog.dallog.acceptance.builder.AuthAssuredBuilder;
import com.allog.dallog.acceptance.builder.AuthAssuredBuilder.AuthResponseBuilder;
import com.allog.dallog.acceptance.builder.MemberAssuredBuilder;
import com.allog.dallog.domain.member.dto.request.MemberUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("회원 관련 기능")
public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("등록된 회원이 자신의 정보를 조회하면 상태코드 200을 반환한다.")
    @Test
    void 등록된_회원이_자신의_정보를_조회하면_상태코드_200_을_반환한다() {
        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .상태코드_200을_받는다()
                .토큰들을_발급_받는다();

        MemberAssuredBuilder.요청()
                .내_정보를_조회한다(회원.accessToken())
                .응답()
                .상태코드_200을_받는다()
                .내_정보를_받는다();
    }

    @DisplayName("등록된 회원이 자신의 이름을 변경하면 상태코드 204를 반환한다.")
    @Test
    void 등록된_회원이_자신의_이름을_변경하면_상태코드_204를_반환한다() {
        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .상태코드_200을_받는다()
                .토큰들을_발급_받는다();

        MemberAssuredBuilder.요청()
                .내_이름을_변경한다(회원.accessToken(), new MemberUpdateRequest("아칸"))
                .응답()
                .상태코드_204을_받는다();
    }
}

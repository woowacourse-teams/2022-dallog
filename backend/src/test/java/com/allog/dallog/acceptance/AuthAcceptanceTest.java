package com.allog.dallog.acceptance;

import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;

import com.allog.dallog.acceptance.builder.AuthAssuredBuilder;
import com.allog.dallog.common.config.TokenConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

@Import(TokenConfig.class)
@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("구글 OAuth 인증 URI를 요청하면 링크를 받는다.")
    @Test
    void 구글_OAuth_인증_URI를_요청하면_링크를_받는다() {
        AuthAssuredBuilder.요청()
                .OAuth_인증_URI을_요청한다(GOOGLE_PROVIDER)
                .응답()
                .상태코드_200을_받는다()
                .OAuth_인증_URL에_접속한다();
    }

    @DisplayName("회원가입을 하면 토큰들을 발급 받는다.")
    @Test
    void 회원가입을_하면_토큰들을_발급_받는다() {
        AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .상태코드_200을_받는다()
                .토큰들을_발급_받는다();
    }

    @DisplayName("로그인을 하면 토큰들을 발급 받는다.")
    @Test
    void 로그인을_하면_토큰들을_발급_받는다() {
        AuthAssuredBuilder.요청()
                .로그인_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .상태코드_200을_받는다()
                .토큰들을_발급_받는다();
    }

    @DisplayName("만료된 엑세스_토큰으로 웹페이지를 로드하면 상태코드 401을 받는다.")
    @Test
    void 만료된_엑세스_토큰으로_웹페이지를_로드하면_상태코드_401을_받는다() {
        AuthAssuredBuilder.요청()
                .로그인_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .만료된_토큰으로_API를_요청한다()
                .응답()
                .상태코드_401을_받는다();
    }

    @DisplayName("리프레시 토큰으로 새로운 엑세스 토큰을 발급한다.")
    @Test
    void 리프레시_토큰으로_새로운_엑세스_토큰을_발급한다() {
        AuthAssuredBuilder.요청()
                .로그인_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .엑세스_토큰_재발급을_요청한다()
                .응답()
                .상태코드_200을_받는다()
                .엑세스_토큰을_발급_받는다();
    }
}

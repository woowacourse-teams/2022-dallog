package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성하고_토큰을_반환한다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static com.allog.dallog.acceptance.fixtures.MemberAcceptanceFixtures.자신의_정보를_조회한다;
import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_이름;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_이메일;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_프로필;
import static com.allog.dallog.common.fixtures.AuthFixtures.인증_코드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.config.TestConfig;
import com.allog.dallog.domain.member.dto.MemberResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

@DisplayName("회원 관련 기능")
@Import(TestConfig.class)
public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("등록된 회원이 자신의 정보를 조회하면 상태코드 200을 반환한다.")
    @Test
    void 등록된_회원이_자신의_정보를_조회하면_상태코드_200_을_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(GOOGLE_PROVIDER, 인증_코드);

        // when
        ExtractableResponse<Response> response = 자신의_정보를_조회한다(accessToken);
        MemberResponse memberResponse = response.as(MemberResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(memberResponse.getEmail()).isEqualTo(STUB_이메일);
            assertThat(memberResponse.getDisplayName()).isEqualTo(STUB_이름);
            assertThat(memberResponse.getProfileImageUrl()).isEqualTo(STUB_프로필);
        });
    }
}

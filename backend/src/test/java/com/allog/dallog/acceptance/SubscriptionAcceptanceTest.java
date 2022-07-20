package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.새로운_카테고리를_등록한다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_201이_반환된다;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.CODE;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.OAUTH_PROVIDER;

import com.allog.dallog.auth.dto.TokenResponse;
import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.common.config.TestConfig;
import com.allog.dallog.common.fixtures.SubscriptionFixtures;
import com.allog.dallog.subscription.dto.request.SubscriptionCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("구독 관련 기능")
@Import(TestConfig.class)
public class SubscriptionAcceptanceTest extends AcceptanceTest {

    @DisplayName("인증된 회원이 카테고리를 구독하면 201을 반환한다.")
    @Test
    void 인증된_회원이_카테고리를_구독하면_201을_반환한다() {
        // given
        ExtractableResponse<Response> tokenCreateResponse = 자체_토큰을_생성한다(OAUTH_PROVIDER, CODE);
        TokenResponse tokenResponse = tokenCreateResponse.as(TokenResponse.class);
        CategoryResponse categoryResponse = 새로운_카테고리를_등록한다(tokenResponse, "BE 공식일정").as(CategoryResponse.class);

        // when
        ExtractableResponse<Response> response = 카테고리를_구독한다(tokenResponse, categoryResponse);

        // then
        상태코드_201이_반환된다(response);
    }

    private ExtractableResponse<Response> 카테고리를_구독한다(final TokenResponse tokenResponse,
                                                     final CategoryResponse categoryResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SubscriptionCreateRequest(SubscriptionFixtures.COLOR_RED))
                .when().post("/api/members/me/categories/{categoryId}/subscriptions", categoryResponse.getId())
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }
}

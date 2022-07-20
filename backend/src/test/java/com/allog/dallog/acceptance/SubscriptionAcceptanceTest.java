package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_201이_반환된다;

import com.allog.dallog.auth.dto.TokenRequest;
import com.allog.dallog.auth.dto.TokenResponse;
import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.common.config.TestConfig;
import com.allog.dallog.common.fixtures.OAuthMemberFixtures;
import com.allog.dallog.fixture.SubscriptionFixtures;
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
        TokenResponse tokenResponse = 자체_토큰을_생성한다(OAuthMemberFixtures.OAUTH_PROVIDER, OAuthMemberFixtures.CODE);
        CategoryResponse categoryResponse = 새로운_카테고리를_등록한다("BE 공식일정");

        // when
        ExtractableResponse<Response> response = 카테고리를_구독한다(tokenResponse, categoryResponse);

        // then
        상태코드_201이_반환된다(response);
    }

    private TokenResponse 자체_토큰을_생성한다(final String oauthProvider, final String code) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(code))
                .when().post("/api/auth/{oauthProvider}/token", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(TokenResponse.class);
    }

    private CategoryResponse 새로운_카테고리를_등록한다(final String name) {
        CategoryCreateRequest request = new CategoryCreateRequest(name);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/categories")
                .then().log().all()
                .extract()
                .as(CategoryResponse.class);
    }

    private ExtractableResponse<Response> 카테고리를_구독한다(final TokenResponse tokenResponse,
                                                     final CategoryResponse categoryResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SubscriptionCreateRequest(SubscriptionFixtures.COLOR))
                .when().post("/api/members/me/categories/{categoryId}/subscriptions", categoryResponse.getId())
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }
}

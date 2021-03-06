package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성하고_토큰을_반환한다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_201이_반환된다;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.CODE;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.OAUTH_PROVIDER;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.COLOR_RED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.common.config.TestConfig;
import com.allog.dallog.subscription.dto.request.SubscriptionCreateRequest;
import com.allog.dallog.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.subscription.dto.response.SubscriptionsResponse;
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
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(OAUTH_PROVIDER, CODE);
        CategoryResponse categoryResponse = 새로운_카테고리를_등록한다(accessToken, "BE 공식일정");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SubscriptionCreateRequest(COLOR_RED))
                .when().post("/api/members/me/categories/{categoryId}/subscriptions", categoryResponse.getId())
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        // then
        상태코드_201이_반환된다(response);
    }

    @DisplayName("인증된 회원이 구독 목록을 조회하면 200을 반환한다.")
    @Test
    void 인증된_회원이_구독_목록을_조회하면_200을_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(OAUTH_PROVIDER, CODE);
        CategoryResponse categoryResponse1 = 새로운_카테고리를_등록한다(accessToken, "BE 일정");
        CategoryResponse categoryResponse2 = 새로운_카테고리를_등록한다(accessToken, "FE 일정");
        CategoryResponse categoryResponse3 = 새로운_카테고리를_등록한다(accessToken, "공통 일정");

        카테고리를_구독한다(accessToken, categoryResponse1);
        카테고리를_구독한다(accessToken, categoryResponse2);
        카테고리를_구독한다(accessToken, categoryResponse3);

        // when
        ExtractableResponse<Response> response = 구독_목록을_조회한다(accessToken);
        SubscriptionsResponse subscriptionsResponse = response.as(SubscriptionsResponse.class);

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(subscriptionsResponse.getSubscriptions()).hasSize(3);
        });
    }

    @DisplayName("구독을 취소할 경우 204를 반환한다.")
    @Test
    void 구독을_취소할_경우_204를_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(OAUTH_PROVIDER, CODE);
        CategoryResponse categoryResponse1 = 새로운_카테고리를_등록한다(accessToken, "BE 일정");
        CategoryResponse categoryResponse2 = 새로운_카테고리를_등록한다(accessToken, "FE 일정");
        CategoryResponse categoryResponse3 = 새로운_카테고리를_등록한다(accessToken, "공통 일정");

        SubscriptionResponse subscriptionResponse = 카테고리를_구독한다(accessToken, categoryResponse1);
        카테고리를_구독한다(accessToken, categoryResponse2);
        카테고리를_구독한다(accessToken, categoryResponse3);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/members/me/subscriptions/{subscriptionId}", subscriptionResponse.getId())
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private CategoryResponse 새로운_카테고리를_등록한다(final String accessToken, final String name) {
        CategoryCreateRequest request = new CategoryCreateRequest(name);
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/categories")
                .then().log().all()
                .extract()
                .as(CategoryResponse.class);
    }

    private SubscriptionResponse 카테고리를_구독한다(final String accessToken, final CategoryResponse categoryResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SubscriptionCreateRequest(COLOR_RED))
                .when().post("/api/members/me/categories/{categoryId}/subscriptions", categoryResponse.getId())
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(SubscriptionResponse.class);
    }

    private ExtractableResponse<Response> 구독_목록을_조회한다(final String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/members/me/subscriptions")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}

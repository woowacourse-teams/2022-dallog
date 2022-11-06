package com.allog.dallog.acceptance.builder;

import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.deleteWithToken;
import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.patchWithToken;
import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.postWithToken;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateCreatedStatus;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateNoContentStatus;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateOkStatus;

import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class SubscritptionAssuredBuilder {

    private SubscritptionAssuredBuilder() {
    }

    public static SubscritptionRequestBuilder 요청() {
        return new SubscritptionRequestBuilder();
    }

    public static class SubscritptionRequestBuilder {

        private ExtractableResponse<Response> response;

        public SubscritptionRequestBuilder 카테고리를_구독한다(final String accessToken, final Long categoryId) {
            response = postWithToken(accessToken, "/api/members/me/categories/{categoryId}/subscriptions", "",
                    String.valueOf(categoryId));
            return this;
        }

        public SubscritptionRequestBuilder 구독_정보를_변경한다(final String accessToken, final Object body) {
            Long subscriptionId = response.as(SubscriptionResponse.class).getId();
            response = patchWithToken(accessToken, body, "/api/members/me/subscriptions/{subscriptionId}",
                    String.valueOf(subscriptionId));
            return this;
        }

        public SubscritptionRequestBuilder 구독을_취소한다(final String accessToken) {
            Long subscriptionId = response.as(SubscriptionResponse.class).getId();
            response = deleteWithToken(accessToken, "/api/members/me/subscriptions/{subscriptionId}",
                    String.valueOf(subscriptionId));
            return this;
        }

        public SubscritptionResponseBuilder 응답() {
            return new SubscritptionResponseBuilder(response);
        }
    }

    public static class SubscritptionResponseBuilder {

        private final ExtractableResponse<Response> response;

        public SubscritptionResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public SubscritptionResponseBuilder 상태코드_200을_받는다() {
            validateOkStatus(response);
            return this;
        }

        public SubscritptionResponseBuilder 상태코드_201을_받는다() {
            validateCreatedStatus(response);
            return this;
        }

        public SubscritptionResponseBuilder 상태코드_204을_받는다() {
            validateNoContentStatus(response);
            return this;
        }
    }
}

package com.allog.dallog.acceptance.builder;

import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.deleteWithToken;
import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.get;
import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.patchWithToken;
import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.postWithToken;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateCreatedStatus;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateNoContentStatus;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateOkStatus;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CategoryAssuredBuilder {

    private CategoryAssuredBuilder() {
    }

    public static CategoryReqeustBuilder 요청() {
        return new CategoryReqeustBuilder();
    }

    public static class CategoryReqeustBuilder {

        private ExtractableResponse<Response> response;

        public CategoryReqeustBuilder 카테고리를_등록한다(final String accessToken, final Object body) {
            response = postWithToken(accessToken, "/api/categories", body);
            return this;
        }

        public CategoryReqeustBuilder NORMAL_카테고리_전체를_조회한다() {
            response = get("/api/categories");
            return this;
        }

        public CategoryReqeustBuilder 검색어가_포함된_NORMAL_카테고리_전체를_조회한다(final String name) {
            response = get("/api/categories?name={name}", name);
            return this;
        }

        public CategoryReqeustBuilder 카테고리를_수정한다(final String accessToken, final Object body) {
            response = patchWithToken(accessToken, body, "/api/categories/{categoryId}", getCategoryId());
            return this;
        }

        public CategoryReqeustBuilder 카테고리를_삭제한다(final String accessToken) {
            response = deleteWithToken(accessToken, "/api/categories/{categoryId}", getCategoryId());
            return this;
        }

        public CategoryReqeustBuilder 카테고리를_구독한다(final String accessToken, final Long categoryId) {
            response = postWithToken(accessToken, "/api/members/me/categories/{categoryId}/subscriptions", "",
                    String.valueOf(categoryId));
            return this;
        }

        public CategoryReqeustBuilder 구독자의_권한을_변경한다(final String accessToken, final Object body,
                                                    final String categoryId, final String memberId) {
            response = patchWithToken(accessToken, body, "/api/categories/{categoryId}/subscribers/{memberId}/role",
                    categoryId, memberId);
            return this;
        }

        public CategoryResponseBuilder 응답() {
            return new CategoryResponseBuilder(response);
        }

        private String getCategoryId() {
            String categoryId = String.valueOf(response.as(CategoryResponse.class).getId());
            return categoryId;
        }

        public Long getId() {
            return response.as(CategoryResponse.class).getId();
        }
    }

    public static class CategoryResponseBuilder {

        private final ExtractableResponse<Response> response;

        public CategoryResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public CategoryResponseBuilder 상태코드_201을_받는다() {
            validateCreatedStatus(response);
            return this;
        }

        public CategoryResponseBuilder 상태코드_200을_받는다() {
            validateOkStatus(response);
            return this;
        }

        public CategoryResponseBuilder 상태코드_204를_받는다() {
            validateNoContentStatus(response);
            return this;
        }

        public CategoryResponseBuilder NORMAL_카테고리_전체를_받는다(final int categoryCount) {
            CategoriesResponse actual = response.as(CategoriesResponse.class);
            assertThat(actual.getCategories()).hasSize(categoryCount);
            return this;
        }
    }
}

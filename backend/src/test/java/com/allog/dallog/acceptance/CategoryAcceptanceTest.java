package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성하고_토큰을_반환한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.내가_등록한_카테고리를_페이징을_통해_조회한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.새로운_카테고리를_등록한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.카테고리를_페이징을_통해_조회한다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_201이_반환된다;
import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_NAME;
import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_NUMBER;
import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_SIZE;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.CODE;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.OAUTH_PROVIDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.category.dto.response.CategoriesResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("카테고리 관련 기능")
public class CategoryAcceptanceTest extends AcceptanceTest {

    @DisplayName("정상적인 카테고리 정보를 등록하면 상태코드 201을 반환한다.")
    @Test
    void 정상적인_카테고리_정보를_등록하면_상태코드_201을_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(OAUTH_PROVIDER, CODE);

        // when
        ExtractableResponse<Response> response = 새로운_카테고리를_등록한다(accessToken, CATEGORY_NAME);

        // then
        상태코드_201이_반환된다(response);
    }

    @DisplayName("카테고리를 등록하고 페이징을 통해 나누어 조회한다.")
    @Test
    void 카테고리를_등록하고_페이징을_통해_나누어_조회한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(OAUTH_PROVIDER, CODE);
        새로운_카테고리를_등록한다(accessToken, CATEGORY_NAME);
        새로운_카테고리를_등록한다(accessToken, CATEGORY_NAME);
        새로운_카테고리를_등록한다(accessToken, CATEGORY_NAME);
        새로운_카테고리를_등록한다(accessToken, CATEGORY_NAME);

        // when
        ExtractableResponse<Response> response = 카테고리를_페이징을_통해_조회한다(PAGE_NUMBER, PAGE_SIZE);
        CategoriesResponse categoriesResponse = response.as(CategoriesResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(categoriesResponse.getPage()).isEqualTo(PAGE_NUMBER);
            assertThat(categoriesResponse.getCategories()).hasSize(PAGE_SIZE);
        });
    }

    @DisplayName("카테고리를 등록하고 내가 등록한 카테고리를 페이징을 통해 나누어 조회한다.")
    @Test
    void 카테고리를_등록하고_내가_등록한_카테고리를_페이징을_통해_나누어_조회한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(OAUTH_PROVIDER, CODE);
        새로운_카테고리를_등록한다(accessToken, CATEGORY_NAME);
        새로운_카테고리를_등록한다(accessToken, CATEGORY_NAME);
        새로운_카테고리를_등록한다(accessToken, CATEGORY_NAME);

        int page = 0;

        // when
        ExtractableResponse<Response> response = 내가_등록한_카테고리를_페이징을_통해_조회한다(accessToken, page, PAGE_SIZE);
        CategoriesResponse categoriesResponse = response.as(CategoriesResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(categoriesResponse.getPage()).isEqualTo(page);
            assertThat(categoriesResponse.getCategories()).hasSize(PAGE_SIZE);
        });
    }
}

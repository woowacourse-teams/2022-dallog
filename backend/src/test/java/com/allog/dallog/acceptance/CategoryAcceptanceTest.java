package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성하고_토큰을_반환한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.id를_통해_카테고리를_가져온다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.내가_등록한_카테고리를_삭제한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.내가_등록한_카테고리를_수정한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.내가_등록한_카테고리를_페이징을_통해_조회한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.새로운_카테고리를_등록한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.카테고리를_페이징을_통해_조회한다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_201이_반환된다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_204가_반환된다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_404가_반환된다;
import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_NAME;
import static com.allog.dallog.common.fixtures.CategoryFixtures.MODIFIED_CATEGORY_NAME;
import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_NUMBER_1;
import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_SIZE_2;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.CODE;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.OAUTH_PROVIDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.category.dto.response.CategoriesResponse;
import com.allog.dallog.category.dto.response.CategoryResponse;
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
        ExtractableResponse<Response> response = 카테고리를_페이징을_통해_조회한다(PAGE_NUMBER_1, PAGE_SIZE_2);
        CategoriesResponse categoriesResponse = response.as(CategoriesResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(categoriesResponse.getPage()).isEqualTo(PAGE_NUMBER_1);
            assertThat(categoriesResponse.getCategories()).hasSize(PAGE_SIZE_2);
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
        ExtractableResponse<Response> response = 내가_등록한_카테고리를_페이징을_통해_조회한다(accessToken, page, PAGE_SIZE_2);
        CategoriesResponse categoriesResponse = response.as(CategoriesResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(categoriesResponse.getPage()).isEqualTo(page);
            assertThat(categoriesResponse.getCategories()).hasSize(PAGE_SIZE_2);
        });
    }

    @DisplayName("카테고리를 등록하고 내가 등록한 카테고리를 수정하면 상태코드 204를 반환한다.")
    @Test
    void 카테고리를_등록하고_내가_등록한_카테고리를_수정하면_상태코드_204를_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(OAUTH_PROVIDER, CODE);
        CategoryResponse savedCategory = 새로운_카테고리를_등록한다(accessToken, CATEGORY_NAME)
                .as(CategoryResponse.class);

        // when
        ExtractableResponse<Response> response
                = 내가_등록한_카테고리를_수정한다(accessToken, savedCategory.getId(), MODIFIED_CATEGORY_NAME);
        CategoryResponse categoryResponse = id를_통해_카테고리를_가져온다(savedCategory.getId()).as(CategoryResponse.class);

        // then
        assertAll(() -> {
            상태코드_204가_반환된다(response);
            assertThat(categoryResponse.getName()).isEqualTo(MODIFIED_CATEGORY_NAME);
        });
    }

    @DisplayName("카테고리를 등록하고 내가 등록한 카테고리를 삭제하면 상태코드 204를 반환한다.")
    @Test
    void 카테고리를_등록하고_내가_등록한_카테고리를_삭제하면_상태코드_204를_반환한다() {
        // given
        TokenResponse tokenResponse = 자체_토큰을_생성한다(OAUTH_PROVIDER, CODE);
        CategoryResponse savedCategory = 새로운_카테고리를_등록한다(tokenResponse, CATEGORY_NAME)
                .as(CategoryResponse.class);

        // when
        ExtractableResponse<Response> response
                = 내가_등록한_카테고리를_삭제한다(tokenResponse.getAccessToken(), savedCategory.getId());
        ExtractableResponse<Response> categoryResponse = id를_통해_카테고리를_가져온다(savedCategory.getId());

        // then
        assertAll(() -> {
            상태코드_204가_반환된다(response);
            상태코드_404가_반환된다(categoryResponse);
        });
    }
}

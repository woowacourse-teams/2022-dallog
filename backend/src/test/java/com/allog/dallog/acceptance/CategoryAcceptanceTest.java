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
import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.인증_코드;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디_생성_요청;
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
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(GOOGLE_PROVIDER, 인증_코드);

        // when
        ExtractableResponse<Response> response = 새로운_카테고리를_등록한다(accessToken, 매트_아고라_생성_요청);

        // then
        상태코드_201이_반환된다(response);
    }

    @DisplayName("카테고리를 등록하고 페이징을 통해 나누어 조회한다.")
    @Test
    void 카테고리를_등록하고_페이징을_통해_나누어_조회한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(GOOGLE_PROVIDER, 인증_코드);
        새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, BE_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, FE_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, 매트_아고라_생성_요청);
        새로운_카테고리를_등록한다(accessToken, 후디_JPA_스터디_생성_요청);

        // when
        ExtractableResponse<Response> response = 카테고리를_페이징을_통해_조회한다(1, 3);
        CategoriesResponse categoriesResponse = response.as(CategoriesResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(categoriesResponse.getPage()).isEqualTo(1);
            assertThat(categoriesResponse.getCategories()).hasSize(2);
        });
    }

    @DisplayName("카테고리를 등록하고 내가 등록한 카테고리를 페이징을 통해 나누어 조회한다.")
    @Test
    void 카테고리를_등록하고_내가_등록한_카테고리를_페이징을_통해_나누어_조회한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(GOOGLE_PROVIDER, 인증_코드);
        새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, BE_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, FE_일정_생성_요청);

        // when
        ExtractableResponse<Response> response = 내가_등록한_카테고리를_페이징을_통해_조회한다(accessToken, 0, 2);
        CategoriesResponse categoriesResponse = response.as(CategoriesResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(categoriesResponse.getPage()).isEqualTo(0);
            assertThat(categoriesResponse.getCategories()).hasSize(2);
        });
    }

    @DisplayName("카테고리를 등록하고 내가 등록한 카테고리를 수정하면 상태코드 204를 반환한다.")
    @Test
    void 카테고리를_등록하고_내가_등록한_카테고리를_수정하면_상태코드_204를_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(GOOGLE_PROVIDER, 인증_코드);
        CategoryResponse savedCategory = 새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청).as(CategoryResponse.class);
        String newCategoryName = "우테코 공통 일정";

        // when
        ExtractableResponse<Response> response = 내가_등록한_카테고리를_수정한다(accessToken, savedCategory.getId(), newCategoryName);
        CategoryResponse categoryResponse = id를_통해_카테고리를_가져온다(savedCategory.getId()).as(CategoryResponse.class);

        // then
        assertAll(() -> {
            상태코드_204가_반환된다(response);
            assertThat(categoryResponse.getName()).isEqualTo(newCategoryName);
        });
    }

    @DisplayName("카테고리를 등록하고 내가 등록한 카테고리를 삭제하면 상태코드 204를 반환한다.")
    @Test
    void 카테고리를_등록하고_내가_등록한_카테고리를_삭제하면_상태코드_204를_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(GOOGLE_PROVIDER, 인증_코드);
        CategoryResponse savedCategory = 새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청).as(CategoryResponse.class);

        // when
        ExtractableResponse<Response> response = 내가_등록한_카테고리를_삭제한다(accessToken, savedCategory.getId());

        // then
        상태코드_204가_반환된다(response);
    }

    // todo: ExceptionHandler 구현 이후 카테고리 단건 조회 응답 상태코드 404인지 확인
}

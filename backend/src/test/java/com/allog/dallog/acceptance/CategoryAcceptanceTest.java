package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성하고_엑세스_토큰을_반환한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.id를_통해_카테고리를_조회한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.내가_등록한_카테고리를_삭제한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.내가_등록한_카테고리를_수정한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.새로운_카테고리를_등록한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.전체_카테고리를_제목_검색을_통해_조회한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.전체_카테고리를_조회한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.회원의_카테고리_역할을_변경한다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.validateOkStatus;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_201이_반환된다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_204가_반환된다;
import static com.allog.dallog.acceptance.fixtures.MemberAcceptanceFixtures.자신의_정보를_조회한다;
import static com.allog.dallog.acceptance.fixtures.SubscriptionAcceptanceFixtures.카테고리를_구독한다;
import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.내_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디_생성_요청;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.fixtures.OAuthFixtures;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryDetailResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.categoryrole.dto.request.CategoryRoleUpdateRequest;
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
        String accessToken = 자체_토큰을_생성하고_엑세스_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);

        // when
        ExtractableResponse<Response> response = 새로운_카테고리를_등록한다(accessToken, 매트_아고라_생성_요청);

        // then
        상태코드_201이_반환된다(response);
    }

    @DisplayName("개인 카테고리를 생성하면 201을 반환한다.")
    @Test
    void 개인_카테고리를_생성하면_201을_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_엑세스_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);

        // when
        ExtractableResponse<Response> response = 새로운_카테고리를_등록한다(accessToken, 내_일정_생성_요청);

        // then
        상태코드_201이_반환된다(response);
    }

    @DisplayName("카테고리를 등록하고 일반 카테고리 전체를 조회한다.")
    @Test
    void 카테고리를_등록하고_일반_카테고리_전체를_조회한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_엑세스_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);
        새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, BE_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, FE_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, 매트_아고라_생성_요청);
        새로운_카테고리를_등록한다(accessToken, 후디_JPA_스터디_생성_요청);

        // when
        ExtractableResponse<Response> response = 전체_카테고리를_조회한다();
        CategoriesResponse categoriesResponse = response.as(CategoriesResponse.class);

        // then
        assertAll(() -> {
            validateOkStatus(response);
            assertThat(categoriesResponse.getCategories()).hasSize(5);
        });
    }

    @DisplayName("카테고리를 등록하고 제목 검색을 통해 해당하는 카테고리를 조회한다.")
    @Test
    void 카테고리를_등록하고_제목_검색을_통해_해당하는_카테고리를_조회한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_엑세스_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);
        새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, BE_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, FE_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, 매트_아고라_생성_요청);
        새로운_카테고리를_등록한다(accessToken, 후디_JPA_스터디_생성_요청);

        // when
        ExtractableResponse<Response> response = 전체_카테고리를_제목_검색을_통해_조회한다("일");
        CategoriesResponse categoriesResponse = response.as(CategoriesResponse.class);

        // then
        assertAll(() -> {
            validateOkStatus(response);
            assertThat(categoriesResponse.getCategories()).hasSize(3);
        });
    }

    @DisplayName("등록된 개인 카테고리는 카테고리 목록에서 조회할 수 없다.")
    @Test
    void 등록된_개인_카테고리는_카테고리_목록에서_조회할_수_없다() {
        // given
        String accessToken = 자체_토큰을_생성하고_엑세스_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);
        /* 공개 카테고리 */
        새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, BE_일정_생성_요청);
        새로운_카테고리를_등록한다(accessToken, FE_일정_생성_요청);
        /* 개인 카테고리 */
        새로운_카테고리를_등록한다(accessToken, 내_일정_생성_요청);

        // when
        ExtractableResponse<Response> response = 전체_카테고리를_제목_검색을_통해_조회한다("");
        CategoriesResponse categoriesResponse = response.as(CategoriesResponse.class);

        // then
        assertAll(() -> {
            validateOkStatus(response);
            assertThat(categoriesResponse.getCategories()).hasSize(3);
        });
    }

    @DisplayName("카테고리를 등록하고 내가 등록한 카테고리를 수정하면 상태코드 204를 반환한다.")
    @Test
    void 카테고리를_등록하고_내가_등록한_카테고리를_수정하면_상태코드_204를_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_엑세스_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);
        CategoryResponse savedCategory = 새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청).as(CategoryResponse.class);
        String newCategoryName = "우테코 공통 일정";

        // when
        ExtractableResponse<Response> response = 내가_등록한_카테고리를_수정한다(accessToken, savedCategory.getId(), newCategoryName);
        CategoryDetailResponse actual = id를_통해_카테고리를_조회한다(savedCategory.getId())
                .as(CategoryDetailResponse.class);

        // then
        assertAll(() -> {
            상태코드_204가_반환된다(response);
            assertThat(actual.getName()).isEqualTo(newCategoryName);
        });
    }

    @DisplayName("카테고리를 등록하고 내가 등록한 카테고리를 삭제하면 상태코드 204를 반환한다.")
    @Test
    void 카테고리를_등록하고_내가_등록한_카테고리를_삭제하면_상태코드_204를_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_엑세스_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);
        CategoryResponse savedCategory = 새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청).as(CategoryResponse.class);

        // when
        ExtractableResponse<Response> response = 내가_등록한_카테고리를_삭제한다(accessToken, savedCategory.getId());

        // then
        상태코드_204가_반환된다(response);
    }

    @DisplayName("특정 구독자의 카테고리 역할을 수정하면 상태코드 204를 반환한다.")
    @Test
    void 특정_구독자의_카테고리_역할을_수정하면_상태코드_204를_반환한다() {
        // given
        String 관리자_토큰 = 자체_토큰을_생성하고_엑세스_토큰을_반환한다(GOOGLE_PROVIDER, OAuthFixtures.후디.getCode());
        CategoryResponse 카테고리 = 새로운_카테고리를_등록한다(관리자_토큰, 공통_일정_생성_요청).as(CategoryResponse.class);

        String 구독자_토큰 = 자체_토큰을_생성하고_엑세스_토큰을_반환한다(GOOGLE_PROVIDER, OAuthFixtures.매트.getCode());
        ExtractableResponse<Response> 회원정보 = 자신의_정보를_조회한다(구독자_토큰);
        long 구독자_id = 회원정보.body().jsonPath().getLong("id");

        카테고리를_구독한다(구독자_토큰, 카테고리.getId());

        // when
        ExtractableResponse<Response> response = 회원의_카테고리_역할을_변경한다(관리자_토큰, 카테고리.getId(), 구독자_id,
                new CategoryRoleUpdateRequest(ADMIN));

        // then
        상태코드_204가_반환된다(response);
    }
}

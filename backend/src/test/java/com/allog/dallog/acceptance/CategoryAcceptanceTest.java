package com.allog.dallog.acceptance;

import static com.allog.dallog.common.Constants.개인_카테고리_이름;
import static com.allog.dallog.common.Constants.대학입시_카테고리_이름;
import static com.allog.dallog.common.Constants.박람회_카테고리_이름;
import static com.allog.dallog.common.Constants.취업_카테고리_이름;
import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.category.domain.CategoryType.PERSONAL;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;

import com.allog.dallog.acceptance.builder.AuthAssuredBuilder;
import com.allog.dallog.acceptance.builder.AuthAssuredBuilder.AuthResponseBuilder;
import com.allog.dallog.acceptance.builder.CategoryAssuredBuilder;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.domain.categoryrole.dto.request.CategoryRoleUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("카테고리 관련 기능")
public class CategoryAcceptanceTest extends AcceptanceTest {

    private final CategoryCreateRequest 취업_카테고리_생성_요청 = new CategoryCreateRequest(취업_카테고리_이름, NORMAL);
    private final CategoryCreateRequest 박람회_카테고리_생성_요청 = new CategoryCreateRequest(박람회_카테고리_이름, NORMAL);
    private final CategoryCreateRequest 대학입시_카테고리_생성_요청 = new CategoryCreateRequest(대학입시_카테고리_이름, NORMAL);
    private final CategoryCreateRequest 개인_카테고리_생성_요청 = new CategoryCreateRequest(개인_카테고리_이름, PERSONAL);
    private final CategoryUpdateRequest 카테고리_이름_수정_요청 = new CategoryUpdateRequest("대학 박람회");
    private final CategoryRoleUpdateRequest 카테고리_권한_수정_요청 = new CategoryRoleUpdateRequest(ADMIN);

    @DisplayName("카테고리 등록하면 상태코드 201을 받는다.")
    @Test
    void 카테고리_등록하면_상태코드_201을_받는다() {
        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .토큰들을_발급_받는다();

        CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(회원.accessToken(), 취업_카테고리_생성_요청)
                .응답()
                .상태코드_201을_받는다();
    }

    @DisplayName("개인 카테고리를 등록하면 상태코드 201을 받는다.")
    @Test
    void 개인_카테고리를_등록하면_상태코드_201을_받는다() {
        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .토큰들을_발급_받는다();

        CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(회원.accessToken(), 개인_카테고리_생성_요청)
                .응답()
                .상태코드_201을_받는다();
    }

    @DisplayName("NORMAL 카테고리 전체를 조회한다.")
    @Test
    void NORMAL_카테고리_전체를_조회한다() {
        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .토큰들을_발급_받는다();

        CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(회원.accessToken(), 취업_카테고리_생성_요청)
                .카테고리를_등록한다(회원.accessToken(), 박람회_카테고리_생성_요청)
                .카테고리를_등록한다(회원.accessToken(), 대학입시_카테고리_생성_요청)
                .NORMAL_카테고리_전체를_조회한다()
                .응답()
                .상태코드_200을_받는다()
                .NORMAL_카테고리_전체를_받는다(3);
    }

    @DisplayName("검색어가 포함된 NORMAL 카테고리 전체를 조회한다.")
    @Test
    void 검색어가_포함된_NORMAL_카테고리_전체를_조회한다() {
        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .토큰들을_발급_받는다();

        CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(회원.accessToken(), 취업_카테고리_생성_요청)
                .카테고리를_등록한다(회원.accessToken(), 박람회_카테고리_생성_요청)
                .카테고리를_등록한다(회원.accessToken(), 대학입시_카테고리_생성_요청)
                .검색어가_포함된_NORMAL_카테고리_전체를_조회한다("대학입시")
                .응답()
                .상태코드_200을_받는다()
                .NORMAL_카테고리_전체를_받는다(1);
    }

    @DisplayName("개인 카테고리는 NORMAL_카테고리 전체 조회에 포함되지 않는다.")
    @Test
    void 개인_카테고리는_NORMAL_카테고리_전체_조회에_포함되지_않는다() {
        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .토큰들을_발급_받는다();

        CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(회원.accessToken(), 개인_카테고리_생성_요청)
                .카테고리를_등록한다(회원.accessToken(), 박람회_카테고리_생성_요청)
                .카테고리를_등록한다(회원.accessToken(), 대학입시_카테고리_생성_요청)
                .NORMAL_카테고리_전체를_조회한다()
                .응답()
                .상태코드_200을_받는다()
                .NORMAL_카테고리_전체를_받는다(2);
    }

    @DisplayName("권한이 ADMIN인 카테고리를 수정하면 상태코드 204를 받는다.")
    @Test
    void 권한이_ADMIN인_카테고리를_수정하면_상태코드_204를_받는다() {
        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .토큰들을_발급_받는다();

        CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(회원.accessToken(), 대학입시_카테고리_생성_요청)
                .카테고리를_수정한다(회원.accessToken(), 카테고리_이름_수정_요청)
                .응답()
                .상태코드_204를_받는다();
    }

    @DisplayName("권한이 ADMIN인 카테고리를 삭제하면 상태코드 204를 받는다.")
    @Test
    void 권한이_ADMIN인_카테고리를_삭제하면_상태코드_204를_받는다() {
        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .토큰들을_발급_받는다();

        CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(회원.accessToken(), 대학입시_카테고리_생성_요청)
                .카테고리를_삭제한다(회원.accessToken())
                .응답()
                .상태코드_204를_받는다();
    }

    // TO_DO: 레거시 코드입니다. API 스펙변경과 함께 기능 수정이 필요할것 같습니다.
    @DisplayName("권한이 ADMIN인 카테고리의 구독자 권한을 수정하면 상태코드 204를 받는다.")
    @Test
    void 권한이_ADMIN인_카테고리의_구독자_권한을_수정하면_상태코드_204를_받는다() {
        // given
        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .토큰들을_발급_받는다();

        CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(회원.accessToken(), 대학입시_카테고리_생성_요청);

        AuthResponseBuilder 구독자 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, "creator authorization code")
                .응답()
                .토큰들을_발급_받는다();

        CategoryAssuredBuilder.요청()
                .카테고리를_구독한다(구독자.accessToken(), 2L)
                .구독자의_권한을_변경한다(회원.accessToken(), 카테고리_권한_수정_요청, "2", "2")
                .응답()
                .상태코드_204를_받는다();
    }
}

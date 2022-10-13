package com.allog.dallog.presentation;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_세부_응답;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_응답;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라;
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.리버;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디_응답;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.NONE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoriesWithPageResponse;
import com.allog.dallog.domain.category.dto.response.CategoryDetailResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.category.exception.InvalidCategoryException;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.categoryrole.application.CategoryRoleService;
import com.allog.dallog.domain.categoryrole.domain.CategoryAuthority;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleType;
import com.allog.dallog.domain.categoryrole.dto.request.CategoryRoleUpdateRequest;
import com.allog.dallog.domain.categoryrole.exception.NoCategoryAuthorityException;
import com.allog.dallog.domain.categoryrole.exception.NoSuchCategoryRoleException;
import com.allog.dallog.domain.categoryrole.exception.NotAbleToChangeRoleException;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.response.SubscribersResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";
    private static final String INVALID_CATEGORY_NAME = "20글자를 초과하는 유효하지 않은 카테고리 이름";
    private static final String CATEGORY_NAME_OVER_LENGTH_EXCEPTION_MESSAGE = "카테고리 이름의 길이는 20을 초과할 수 없습니다.";

    @MockBean
    private AuthService authService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryRoleService categoryRoleService;

    @MockBean
    private MemberService memberService;

    @DisplayName("카테고리를 생성한다.")
    @Test
    void 카테고리를_생성한다() throws Exception {
        // given
        CategoryResponse 카테고리 = BE_일정_응답(후디_응답);
        given(categoryService.save(any(), any(CategoryCreateRequest.class))).willReturn(카테고리);

        // when & then
        mockMvc.perform(post("/api/categories")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BE_일정_생성_요청))
                )
                .andDo(print())
                .andDo(document("category/save",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT 토큰")),
                                requestFields(
                                        fieldWithPath("name").description("카테고리 이름 (최대 20글자)"),
                                        fieldWithPath("categoryType").description("카테고리 타입 (NORMAL | PERSONAL | GOOGLE)")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("카테고리 ID"),
                                        fieldWithPath("name").description("카테고리 이름"),
                                        fieldWithPath("categoryType").description("카테고리 타입 (NORMAL | PERSONAL | GOOGLE)"),
                                        fieldWithPath("creator.id").description("카테고리 생성자 ID"),
                                        fieldWithPath("creator.email").description("카테고리 생성자 이메일"),
                                        fieldWithPath("creator.displayName").description("카테고리 생성자 이름"),
                                        fieldWithPath("creator.profileImageUrl").description("카테고리 생성자 프로필 이미지 URL"),
                                        fieldWithPath("creator.socialType").description("카테고리 생성자의 소셜 타입"),
                                        fieldWithPath("createdAt").description("카테고리 생성일자")
                                )
                        )
                )
                .andExpect(status().isCreated());
    }

    @DisplayName("잘못된 이름 형식으로 카테고리를 생성하면 400 Bad Request가 발생한다.")
    @Test
    void 잘못된_이름_형식으로_카테고리를_생성하면_400_Bad_Request가_발생한다() throws Exception {
        // given
        CategoryCreateRequest 잘못된_카테고리_생성_요청 = new CategoryCreateRequest(INVALID_CATEGORY_NAME, NORMAL);

        willThrow(new InvalidCategoryException(CATEGORY_NAME_OVER_LENGTH_EXCEPTION_MESSAGE))
                .given(categoryService)
                .save(any(), any(CategoryCreateRequest.class));

        // when & then
        mockMvc.perform(post("/api/categories")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(잘못된_카테고리_생성_요청))
                )
                .andDo(print())
                .andDo(document("category/save/failByInvalidNameFormat",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT 토큰"))
                        )
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("생성된 카테고리를 전부 조회한다.")
    @Test
    void 생성된_카테고리를_전부_조회한다() throws Exception {
        // given
        int page = 0;
        int size = 10;

        List<Category> 일정_목록 = List.of(공통_일정(관리자()), BE_일정(관리자()), FE_일정(관리자()), 후디_JPA_스터디(후디()), 매트_아고라(매트()));
        CategoriesWithPageResponse categoriesWithPageResponse = new CategoriesWithPageResponse(page, 일정_목록);
        given(categoryService.findNormalByName(any(), any())).willReturn(categoriesWithPageResponse);

        // when & then
        mockMvc.perform(get("/api/categories?page={page}&size={size}", page, size)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("category/findAllByName/allByNoName",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 크기")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("카테고리 제목을 활용하여 조회한다.")
    @Test
    void 카테고리_제목을_활용하여_조회한다() throws Exception {
        // given
        int page = 0;
        int size = 10;

        List<Category> 일정_목록 = List.of(BE_일정(관리자()), FE_일정(관리자()));
        CategoriesWithPageResponse categoriesWithPageResponse = new CategoriesWithPageResponse(page, 일정_목록);
        given(categoryService.findNormalByName(any(), any())).willReturn(categoriesWithPageResponse);

        // when & then
        mockMvc.perform(get("/api/categories?name={name}&page={page}&size={size}", "E", page, size)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("category/findAllByName/fileterByName",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("name").description("카테고리 검색어"),
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 크기")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("내 카테고리를 전부 조회한다.")
    @Test
    void 내_카테고리를_전부_조회한다() throws Exception {
        // given
        int page = 0;
        int size = 10;

        List<Category> 일정_목록 = List.of(공통_일정(관리자()), BE_일정(관리자()), FE_일정(관리자()));
        CategoriesWithPageResponse categoriesWithPageResponse = new CategoriesWithPageResponse(page, 일정_목록);
        given(categoryService.findMyCategories(any(), any(), any())).willReturn(categoriesWithPageResponse);

        // when & then
        mockMvc.perform(get("/api/categories/me?name={name}&page={page}&size={size}", "", page, size)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("category/findMineByName/allByNoName",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("name").description("카테고리 검색어"),
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 크기")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("내가 일정을 편집할 수 있는 카테고리를 전부 조회한다.")
    @Test
    void 내가_일정을_편집할_수_있는_카테고리를_전부_조회한다() throws Exception {
        // given
        List<Category> 일정_목록 = List.of(공통_일정(관리자()), BE_일정(관리자()), FE_일정(관리자()));
        CategoriesResponse categoriesResponse = new CategoriesResponse(일정_목록);
        given(categoryService.findScheduleEditableCategories(any())).willReturn(categoriesResponse);

        // when & then
        mockMvc.perform(get("/api/categories/me/schedule-editable")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("category/findScheduleEditableCategories",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("내가 ADMIN으로 있는 카테고리를 전부 조회한다.")
    @Test
    void 내가_ADMIN으로_있는_카테고리를_전부_조회한다() throws Exception {
        // given
        List<Category> 일정_목록 = List.of(공통_일정(관리자()), BE_일정(관리자()), FE_일정(관리자()));
        CategoriesResponse categoriesResponse = new CategoriesResponse(일정_목록);
        given(categoryService.findAdminCategories(any())).willReturn(categoriesResponse);

        // when & then
        mockMvc.perform(get("/api/categories/me/admin")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("category/findAdminCategories",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("내 카테고리를 제목을 활용하여 조회한다.")
    @Test
    void 내_카테고리를_제목을_활용하여_조회한다() throws Exception {
        // given
        int page = 0;
        int size = 10;

        List<Category> 일정_목록 = List.of(공통_일정(관리자()), BE_일정(관리자()), FE_일정(관리자()));
        CategoriesWithPageResponse categoriesWithPageResponse = new CategoriesWithPageResponse(page, 일정_목록);
        given(categoryService.findMyCategories(any(), any(), any())).willReturn(categoriesWithPageResponse);

        // when & then
        mockMvc.perform(get("/api/categories/me?name={name}&page={page}&size={size}", "E", page, size)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("category/findMineByName/fileterByName",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("name").description("카테고리 검색어"),
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 크기")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("카테고리 ID로 카테고리를 단건 조회한다.")
    @Test
    void 카테고리_ID로_카테고리를_단건_조회한다() throws Exception {
        // given
        Long categoryId = 1L;
        CategoryDetailResponse BE_일정_응답 = BE_일정_세부_응답(후디_응답, 150);
        given(categoryService.findDetailCategoryById(any())).willReturn(BE_일정_응답);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("category/findDetailCategoryById",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("카테고리 ID로 카테고리를 단건 조회시 존재하지 않으면 404 Not Found가 발생한다.")
    @Test
    void 카테고리_ID로_카테고리를_단건_조회시_존재하지_않으면_404_Not_Found를_반환한다() throws Exception {
        // given
        Long categoryId = 1L;
        given(categoryService.findDetailCategoryById(any()))
                .willThrow(new NoSuchCategoryException());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("category/findDetailCategoryById/failByNoCategory",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID")
                                )
                        )
                )
                .andExpect(status().isNotFound());
    }

    @DisplayName("카테고리를 수정한다.")
    @Test
    void 카테고리를_수정한다() throws Exception {
        // given
        Long categoryId = 1L;
        willDoNothing()
                .given(categoryService)
                .update(any(), any(), any());
        CategoryUpdateRequest 카테고리_수정_요청 = new CategoryUpdateRequest(BE_일정_이름);

        // when & then
        mockMvc.perform(patch("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .content(objectMapper.writeValueAsString(카테고리_수정_요청))
                )
                .andDo(print())
                .andDo(document("category/update",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID")
                                )
                        )
                )
                .andExpect(status().isNoContent());
    }

    @DisplayName("카테고리 수정 시 존재하지 않으면 404 Not Found가 발생한다.")
    @Test
    void 카테고리_수정_시_존재하지_않으면_404_Not_Found를_반환한다() throws Exception {
        // given
        Long categoryId = 1L;
        willThrow(NoSuchCategoryException.class)
                .willDoNothing()
                .given(categoryService)
                .update(any(), any(), any());
        CategoryUpdateRequest 카테고리_수정_요청 = new CategoryUpdateRequest(BE_일정_이름);

        // when & then
        mockMvc.perform(patch("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .content(objectMapper.writeValueAsString(카테고리_수정_요청))
                )
                .andDo(print())
                .andDo(document("category/update/failByNoCategory",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID")
                                )
                        )
                )
                .andExpect(status().isNotFound());
    }

    @DisplayName("잘못된 이름 형식으로 카테고리를 수정하면 400 Bad Request가 발생한다.")
    @Test
    void 잘못된_이름_형식으로_카테고리를_수정하면_400_Bad_Request가_발생한다() throws Exception {
        // given
        Long categoryId = 1L;
        willThrow(new InvalidCategoryException(CATEGORY_NAME_OVER_LENGTH_EXCEPTION_MESSAGE))
                .willDoNothing()
                .given(categoryService)
                .update(any(), any(), any());
        CategoryUpdateRequest 카테고리_수정_요청 = new CategoryUpdateRequest(INVALID_CATEGORY_NAME);

        // when & then
        mockMvc.perform(patch("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .content(objectMapper.writeValueAsString(카테고리_수정_요청))
                )
                .andDo(print())
                .andDo(document("category/update/failByInvalidNameFormat",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID")
                                )
                        )
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("카테고리를 제거한다.")
    @Test
    void 카테고리를_제거한다() throws Exception {
        // given
        Long categoryId = 1L;
        willDoNothing()
                .given(categoryService)
                .delete(any(), any());

        // when & then
        mockMvc.perform(delete("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("category/delete",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID")
                                )
                        )
                )
                .andExpect(status().isNoContent());
    }

    @DisplayName("카테고리 제거 시 존재하지 않으면 404 Not Found가 발생한다")
    @Test
    void 카테고리_제거_시_존재하지_않으면_404_Not_Found가_발생한다() throws Exception {
        // given
        Long categoryId = 1L;
        willThrow(new NoSuchCategoryException("존재하지 않는 카테고리를 삭제할 수 없습니다."))
                .willDoNothing()
                .given(categoryService)
                .delete(any(), any());

        // when & then
        mockMvc.perform(delete("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("category/delete/failByNoCategory",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID")
                                )
                        )
                )
                .andExpect(status().isNotFound());
    }

    @DisplayName("ADMIN은 다른 구독자의 카테고리 역할을 변경할 수 있다.")
    @Test
    void ADMIN은_다른_구독자의_카테고리_역할을_변경할_수_있다() throws Exception {
        // given
        Long categoryId = 1L;
        Long memberId = 2L;
        willDoNothing()
                .given(categoryRoleService)
                .updateRole(any(), any(), any(), any());

        CategoryRoleUpdateRequest 역할_수정_요청 = new CategoryRoleUpdateRequest(CategoryRoleType.ADMIN);

        // when & then
        mockMvc.perform(patch("/api/categories/{categoryId}/subscribers/{memberId}/role", categoryId, memberId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(역할_수정_요청))
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("category/updateRole",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID"),
                                        parameterWithName("memberId").description("회원 ID")
                                ),
                                requestFields(
                                        fieldWithPath("categoryRoleType").description("역할 (ADMIN | NONE)")
                                )
                        )
                )
                .andExpect(status().isNoContent());
    }

    @DisplayName("ADMIN이 아닌 회원은 다른 구독자의 카테고리 역할을 변경하면 403 Forbidden이 발생한다.")
    @Test
    void ADMIN이_아닌_회원은_다른_구독자의_카테고리_역할을_변경하면_403_Forbidden이_발생한다() throws Exception {
        // given
        Long categoryId = 1L;
        Long memberId = 2L;

        willThrow(new NoCategoryAuthorityException(CategoryAuthority.CHANGE_ROLE_OF_SUBSCRIBER.getName()))
                .willDoNothing()
                .given(categoryRoleService)
                .updateRole(any(), any(), any(), any());

        CategoryRoleUpdateRequest 역할_수정_요청 = new CategoryRoleUpdateRequest(CategoryRoleType.ADMIN);

        // when & then
        mockMvc.perform(patch("/api/categories/{categoryId}/subscribers/{memberId}/role", categoryId, memberId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(역할_수정_요청))
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("category/updateRole/failByNoPermission",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID"),
                                        parameterWithName("memberId").description("회원 ID")
                                )
                        )
                )
                .andExpect(status().isForbidden());
    }

    @DisplayName("카테고리 역할이 변경될 회원이 해당 카테고리를 구독하지 않은 상황이라면 404 NotFound가 발생한다.")
    @Test
    void 카테고리_역할이_변경될_회원이_해당_카테고리를_구독하지_않은_상황이라면_404_NotFound가_발생한다() throws Exception {
        // given
        Long categoryId = 1L;
        Long memberId = 2L;

        willThrow(new NoSuchCategoryRoleException())
                .willDoNothing()
                .given(categoryRoleService)
                .updateRole(any(), any(), any(), any());

        CategoryRoleUpdateRequest 역할_수정_요청 = new CategoryRoleUpdateRequest(CategoryRoleType.ADMIN);

        // when & then
        mockMvc.perform(patch("/api/categories/{categoryId}/subscribers/{memberId}/role", categoryId, memberId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(역할_수정_요청))
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("category/updateRole/failByCategoryRoleNotFound",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID"),
                                        parameterWithName("memberId").description("회원 ID")
                                )
                        )
                )
                .andExpect(status().isNotFound());
    }

    @DisplayName("자기 자신이 유일한 ADMIN이라면 자신의 역할을 변경할 수 없다.")
    @Test
    void 자기_자신이_유일한_ADMIN이라면_자신의_역할을_변경할_수_없다() throws Exception {
        // given
        Long categoryId = 1L;
        Long memberId = 2L;

        willThrow(new NotAbleToChangeRoleException("변경 대상 회원이 유일한 ADMIN이므로 다른 역할로 변경할 수 없습니다."))
                .willDoNothing()
                .given(categoryRoleService)
                .updateRole(any(), any(), any(), any());

        CategoryRoleUpdateRequest 역할_수정_요청 = new CategoryRoleUpdateRequest(CategoryRoleType.ADMIN);

        // when & then
        mockMvc.perform(patch("/api/categories/{categoryId}/subscribers/{memberId}/role", categoryId, memberId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(역할_수정_요청))
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("category/updateRole/failBySoleAdmin",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID"),
                                        parameterWithName("memberId").description("회원 ID")
                                )
                        )
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("특정 카테고리의 구독자 목록을 조회한다.")
    @Test
    void 특정_카테고리의_구독자_목록을_조회한다() throws Exception {
        // given
        long categoryId = 10;

        Category 카테고리 = 공통_일정(관리자());
        List<CategoryRole> categoryRoles = List.of(
                new CategoryRole(카테고리, 매트(), NONE),
                new CategoryRole(카테고리, 리버(), NONE),
                new CategoryRole(카테고리, 파랑(), NONE),
                new CategoryRole(카테고리, 후디(), NONE)
        );

        given(memberService.findSubscribers(any(), any())).willReturn(new SubscribersResponse(categoryRoles));

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/categories/{categoryId}/subscribers", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("category/findSubscribers",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("특정 카테고리의 구독자 목록을 ADMIN이 아닌 유저가 조회하는 경우 403에러가 발생한다.")
    @Test
    void 특정_카테고리의_구독자_목록을_ADMIN이_아닌_유저가_조회하는_경우_403에러가_발생한다() throws Exception {
        // given
        long categoryId = 10;

        given(memberService.findSubscribers(any(), any()))
                .willThrow(new NoCategoryAuthorityException("카테고리 구독자 조회 권한이 없습니다."));

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/categories/{categoryId}/subscribers", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("category/findSubscribers/failByNoAuthority",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID")
                                )
                        )
                )
                .andExpect(status().isForbidden());
    }
}

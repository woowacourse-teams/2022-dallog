package com.allog.dallog.domain.category.presentation;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_응답;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라;
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디_응답;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.common.config.TestConfig;
import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.category.exception.InvalidCategoryException;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(CategoryController.class)
@Import(TestConfig.class)
class CategoryControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";
    private static final String INVALID_CATEGORY_NAME = "20글자를 초과하는 유효하지 않은 카테고리 이름";
    private static final String CATEGORY_NAME_OVER_LENGTH_EXCEPTION_MESSAGE = "카테고리 이름의 길이는 20을 초과할 수 없습니다.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private CategoryService categoryService;

    @DisplayName("카테고리를 생성한다.")
    @Test
    void 카테고리를_생성한다() throws Exception {
        // given
        CategoryResponse 카테고리 = BE_일정_응답(후디_응답);
        given(categoryService.save(any(), any())).willReturn(카테고리);

        // when & then
        mockMvc.perform(post("/api/categories")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BE_일정_생성_요청))
                )
                .andDo(print())
                .andDo(document("categories/save",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT 토큰"))
                        )
                )
                .andExpect(status().isCreated());
    }

    @DisplayName("잘못된 이름 형식으로 카테고리를 생성하면 400 Bad Request가 발생한다.")
    @Test
    void 잘못된_이름_형식으로_카테고리를_생성하면_400_Bad_Request가_발생한다() throws Exception {
        // given
        willThrow(new InvalidCategoryException(CATEGORY_NAME_OVER_LENGTH_EXCEPTION_MESSAGE))
                .given(categoryService)
                .save(any(), any());

        CategoryCreateRequest 잘못된_카테고리_생성_요청 = new CategoryCreateRequest(INVALID_CATEGORY_NAME);

        // when & then
        mockMvc.perform(post("/api/categories")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(잘못된_카테고리_생성_요청))
                )
                .andDo(print())
                .andDo(document("categories/save/badRequest",
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
        CategoriesResponse categoriesResponse = new CategoriesResponse(page, 일정_목록);
        given(categoryService.findAllByName(any(), any())).willReturn(categoriesResponse);

        // when & then
        mockMvc.perform(get("/api/categories?page={page}&size={size}", page, size)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("categories/findAll",
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

    @DisplayName("멤버 자신이 생성한 카테고리를 전부 조회한다.")
    @Test
    void 멤버_자신이_생성한_카테고리를_전부_조회한다() throws Exception {
        // given
        int page = 0;
        int size = 10;

        List<Category> 일정_목록 = List.of(공통_일정(관리자()), BE_일정(관리자()), FE_일정(관리자()));
        CategoriesResponse categoriesResponse = new CategoriesResponse(page, 일정_목록);
        given(categoryService.findMine(any(), any())).willReturn(categoriesResponse);

        // when & then
        mockMvc.perform(get("/api/categories/me?page={page}&size={size}", page, size)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("categories/findMine",
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

    @DisplayName("카테고리 ID로 카테고리를 단건 조회한다.")
    @Test
    void 카테고리_ID로_카테고리를_단건_조회한다() throws Exception {
        // given
        Long categoryId = 1L;
        CategoryResponse BE_일정_응답 = BE_일정_응답(후디_응답);
        given(categoryService.findById(any())).willReturn(BE_일정_응답);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("categories/findById",
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
        given(categoryService.findById(any()))
                .willThrow(new NoSuchCategoryException());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("categories/findById/notFound",
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
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .content(objectMapper.writeValueAsString(카테고리_수정_요청))
                )
                .andDo(print())
                .andDo(document("categories/update",
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
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .content(objectMapper.writeValueAsString(카테고리_수정_요청))
                )
                .andDo(print())
                .andDo(document("categories/update/notFound",
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
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .content(objectMapper.writeValueAsString(카테고리_수정_요청))
                )
                .andDo(print())
                .andDo(document("categories/update/badRequest",
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
                .update(any(), any(), any());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("categories/delete",
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
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/categories/{categoryId}", categoryId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("categories/delete/notFound",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID")
                                )
                        )
                )
                .andExpect(status().isNotFound());
    }
}

package com.allog.dallog.domain.category.presentation;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라;
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

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
        MemberResponse 후디 = new MemberResponse(후디());
        CategoryResponse 카테고리 = new CategoryResponse(1L, BE_일정_이름, 후디, LocalDateTime.now());
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

    @DisplayName("생성된 카테고리를 전부 조회한다.")
    @Test
    void 생성된_카테고리를_전부_조회한다() throws Exception {
        // given
        int page = 0;
        int size = 10;

        List<Category> 일정_목록 = List.of(공통_일정(관리자()), BE_일정(관리자()), FE_일정(관리자()), 후디_JPA_스터디(후디()), 매트_아고라(매트()));
        CategoriesResponse categoriesResponse = new CategoriesResponse(page, 일정_목록);
        given(categoryService.findAll(any())).willReturn(categoriesResponse);

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

        CategoryResponse BE_일정 = new CategoryResponse(BE_일정(후디()));
        given(categoryService.findById(any())).willReturn(BE_일정);

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
}

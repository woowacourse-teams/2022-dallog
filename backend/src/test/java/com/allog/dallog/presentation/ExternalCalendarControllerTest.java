package com.allog.dallog.presentation;

import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_응답;
import static com.allog.dallog.common.fixtures.ExternalCalendarFixtures.대한민국_공휴일;
import static com.allog.dallog.common.fixtures.ExternalCalendarFixtures.우아한테크코스;
import static com.allog.dallog.common.fixtures.ExternalCategoryFixtures.우아한테크코스_생성_요청;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디_응답;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.request.ExternalCategoryCreateRequest;
import com.allog.dallog.domain.category.exception.ExistExternalCategoryException;
import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarService;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendar;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendarsResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

class ExternalCalendarControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @DisplayName("외부 캘린더의 일정을 조회하면 상태코드 200을 반환한다.")
    @Test
    void 외부_캘린더의_일정을_조회하면_상태코드_200을_반환한다() throws Exception {
        // given
        List<ExternalCalendar> ExternalCalendars = List.of(대한민국_공휴일, 우아한테크코스, 대한민국_공휴일);
        given(externalCalendarService.findByMemberId(any())).willReturn(
                new ExternalCalendarsResponse(ExternalCalendars));

        // when & then
        mockMvc.perform(get("/api/external-calendars/me")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("externalCalendar/get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("외부 캘린더를 카테고리로 저장하면 상태코드 201을 반환한다.")
    @Test
    void 외부_캘린더를_카테고리로_저장하면_상태코드_201을_반환한다() throws Exception {
        // given
        given(categoryService.save(any(), any(ExternalCategoryCreateRequest.class))).willReturn(공통_일정_응답(후디_응답));

        // when & then
        mockMvc.perform(post("/api/external-calendars/me")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(우아한테크코스_생성_요청))
                )
                .andDo(print())
                .andDo(document("externalCalendar/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("externalId").type(JsonFieldType.STRING).description("외부 캘린더 id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("캘린더 이름")
                        )))
                .andExpect(status().isCreated());
    }

    @DisplayName("외부 캘린더를 중복하여 저장하면 상태코드 400을 반환한다.")
    @Test
    void 외부_캘린더를_중복하여_저장하면_상태코드_400을_반환한다() throws Exception {
        // given
        willThrow(new ExistExternalCategoryException())
                .given(categoryService)
                .save(any(), any(ExternalCategoryCreateRequest.class));

        // when & then
        mockMvc.perform(post("/api/external-calendars/me")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(우아한테크코스_생성_요청))
                )
                .andDo(print())
                .andDo(document("externalCalendar/duplicated-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("externalId").type(JsonFieldType.STRING).description("외부 캘린더 id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("캘린더 이름")
                        )))
                .andExpect(status().isBadRequest());
    }
}

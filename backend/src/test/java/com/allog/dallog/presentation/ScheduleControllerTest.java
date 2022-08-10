package com.allog.dallog.presentation;

import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_시작일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_종료일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_시작일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_응답;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_종료일시;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.domain.schedule.application.ScheduleService;
import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.common.config.TestConfig;
import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponse;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponses;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import com.allog.dallog.domain.subscription.domain.Color;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
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
@WebMvcTest(ScheduleController.class)
@Import(TestConfig.class)
class ScheduleControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private ScheduleService scheduleService;

    @DisplayName("일정 정보를 등록하면 상태코드 201을 반환한다.")
    @Test
    void 일정_정보를_등록하면_상태코드_201을_반환한다() throws Exception {
        // given
        Long categoryId = 1L;
        ScheduleCreateRequest request = new ScheduleCreateRequest(알록달록_회의_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시, 알록달록_회의_메모);

        given(scheduleService.save(any(), any(), any())).willReturn(1L);

        // when & then
        mockMvc.perform(post("/api/categories/{categoryId}/schedules", categoryId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("schedules/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isCreated());
    }

    @DisplayName("일정 정보를 등록할때 해당 카테고리에 권한이 없으면 403을 반환한다.")
    @Test
    void 일정_정보를_등록할때_해당_카테고리에_권한이_없으면_403을_반환한다() throws Exception {
        // given
        Long categoryId = 1L;
        ScheduleCreateRequest request = new ScheduleCreateRequest(알록달록_회의_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시, 알록달록_회의_메모);

        given(scheduleService.save(any(), any(), any())).willThrow(new NoPermissionException());

        // when & then
        mockMvc.perform(post("/api/categories/{categoryId}/schedules", categoryId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("schedules/save/forbidden",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isForbidden());
    }

    @DisplayName("일정 생성시 전달한 카테고리가 존재하지 않는다면 404를 반환한다.")
    @Test
    void 일정_생성시_전달한_카테고리가_존재하지_않는다면_404를_반환한다() throws Exception {
        // given
        Long categoryId = 0L;
        ScheduleCreateRequest request = new ScheduleCreateRequest(알록달록_회의_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시, 알록달록_회의_메모);

        given(scheduleService.save(any(), any(), any())).willThrow(new NoSuchCategoryException());

        // when & then
        mockMvc.perform(post("/api/categories/{categoryId}/schedules", categoryId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("schedules/save/notfound",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isNotFound());
    }

    @DisplayName("일정을 단건 조회 하면 상태코드 200을 반환한다")
    @Test
    void 일정을_단건_조회_하면_상태코드_200을_반환한다() throws Exception {
        // given
        Long scheduleId = 1L;

        given(scheduleService.findById(scheduleId)).willReturn(알록달록_회의_응답());

        // when & then
        mockMvc.perform(get("/api/schedules/{scheduleId}", scheduleId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("schedules/findone",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("일정을 단건 조회 할 때 일정이 존재하지 않으면 상태코드 404를 반환한다.")
    @Test
    void 일정을_단건_조회_할_때_일정이_존재하지_않으면_상태코드_404를_반환한다() throws Exception {
        // given
        Long scheduleId = 1L;

        given(scheduleService.findById(scheduleId)).willThrow(new NoSuchScheduleException());

        // when & then
        mockMvc.perform(get("/api/schedules/{scheduleId}", scheduleId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("schedules/findone/notfound",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isNotFound());
    }


    @DisplayName("일정을 수정하는데 성공하면 204를 반환한다.")
    @Test
    void 일정을_수정하는데_성공하면_204를_반환한다() throws Exception {
        // given
        Long scheduleId = 1L;
        ScheduleUpdateRequest 수정_요청 = new ScheduleUpdateRequest(레벨_인터뷰_제목, 레벨_인터뷰_시작일시, 레벨_인터뷰_종료일시, 레벨_인터뷰_메모);
        willDoNothing()
                .given(scheduleService)
                .update(any(), any(), any());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/schedules/{scheduleId}", scheduleId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(수정_요청)))
                .andDo(print())
                .andDo(document("schedules/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId").description("일정 ID")
                        )
                ))
                .andExpect(status().isNoContent());
    }

    @DisplayName("일정을 수정하는데 해당 일정의 카테고리에 대한 권한이 없다면 403을 반환한다.")
    @Test
    void 일정을_수정하는데_해당_일정의_카테고리에_대한_권한이_없다면_403을_반환한다() throws Exception {
        // given
        Long scheduleId = 1L;
        ScheduleUpdateRequest 수정_요청 = new ScheduleUpdateRequest(레벨_인터뷰_제목, 레벨_인터뷰_시작일시, 레벨_인터뷰_종료일시, 레벨_인터뷰_메모);
        willThrow(new NoPermissionException())
                .given(scheduleService)
                .update(any(), any(), any());

        // when & then
        mockMvc.perform(patch("/api/schedules/{scheduleId}", scheduleId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(수정_요청)))
                .andDo(print())
                .andDo(document("schedules/update/forbidden",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isForbidden());
    }

    @DisplayName("일정을 수정하는데 일정이 존재하지 않는 경우 404를 반환한다")
    @Test
    void 일정을_수정하는데_일정이_존재하지_않는_경우_404를_반환한다() throws Exception {
        // given
        Long scheduleId = 1L;
        ScheduleUpdateRequest 수정_요청 = new ScheduleUpdateRequest(레벨_인터뷰_제목, 레벨_인터뷰_시작일시, 레벨_인터뷰_종료일시, 레벨_인터뷰_메모);
        willThrow(new NoSuchScheduleException())
                .given(scheduleService)
                .update(any(), any(), any());

        // when & then
        mockMvc.perform(patch("/api/schedules/{scheduleId}", scheduleId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(수정_요청)))
                .andDo(print())
                .andDo(document("schedules/update/notfound",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isNotFound());
    }

    @DisplayName("일정을 제거하는데 성공하면 204를 반환한다.")
    @Test
    void 일정을_제거하는데_성공하면_204를_반환한다() throws Exception {
        // given
        Long scheduleId = 1L;
        willDoNothing()
                .given(scheduleService)
                .deleteById(any(), any());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/schedules/{scheduleId}", scheduleId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE))
                .andDo(print())
                .andDo(document("schedules/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId").description("일정 ID")
                        )
                ))
                .andExpect(status().isNoContent());
    }

    @DisplayName("일정을 제거하는데 해당 일정의 카테고리에 대한 권한이 없다면 403을 반환한다.")
    @Test
    void 일정을_제거하는데_해당_일정의_카테고리에_대한_권한이_없다면_403을_반환한다() throws Exception {
        // given
        Long scheduleId = 1L;
        willThrow(new NoPermissionException())
                .given(scheduleService)
                .deleteById(any(), any());

        // when & then
        mockMvc.perform(delete("/api/schedules/{scheduleId}", scheduleId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE))
                .andDo(print())
                .andDo(document("schedules/delete/forbidden",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isForbidden());
    }

    @DisplayName("일정을 제거하는데 일정이 존재하지 않는 경우 404를 반환한다")
    @Test
    void 일정을_제거하는데_일정이_존재하지_않는_경우_404를_반환한다() throws Exception {
        // given
        Long scheduleId = 1L;
        willThrow(new NoSuchScheduleException())
                .given(scheduleService)
                .deleteById(any(), any());

        // when & then
        mockMvc.perform(delete("/api/schedules/{scheduleId}", scheduleId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE))
                .andDo(print())
                .andDo(document("schedules/delete/notfound",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isNotFound());
    }

    @DisplayName("회원의 일정 목록을 정상적으로 조회하면 200을 반환한다.")
    @Test
    void 회원의_일정_목록을_정상적으로_조회하면_200을_반환한다() throws Exception {
        // given
        String startDate = "2022-07-31";
        String endDate = "2022-09-03";

        MemberScheduleResponse 장기간_일정_1 = new MemberScheduleResponse(1L, "장기간 일정 1", LocalDateTime.of(2022, 8, 1, 0, 0),
                LocalDateTime.of(2022, 8, 3, 0, 0), "장기간 일정 1의 메모", 1L, Color.COLOR_1);
        MemberScheduleResponse 장기간_일정_2 = new MemberScheduleResponse(1L, "장기간 일정 2", LocalDateTime.of(2022, 8, 3, 0, 0),
                LocalDateTime.of(2022, 8, 10, 0, 0), "장기간 일정 2의 메모", 3L, Color.COLOR_2);

        MemberScheduleResponse 종일_일정_1 = new MemberScheduleResponse(1L, "종일 일정 1", LocalDateTime.of(2022, 8, 1, 0, 0),
                LocalDateTime.of(2022, 8, 2, 0, 0), "종일 일정 1의 메모", 1L, Color.COLOR_3);
        MemberScheduleResponse 종일_일정_2 = new MemberScheduleResponse(1L, "종일 일정 2", LocalDateTime.of(2022, 8, 5, 0, 0),
                LocalDateTime.of(2022, 8, 6, 0, 0), "종일 일정 2의 메모", 3L, Color.COLOR_4);

        MemberScheduleResponse 짧은_일정_1 = new MemberScheduleResponse(1L, "짧은 일정 1", LocalDateTime.of(2022, 8, 1, 0, 0),
                LocalDateTime.of(2022, 8, 1, 1, 0), "짧은 일정 1의 메모", 1L, Color.COLOR_5);
        MemberScheduleResponse 짧은_일정_2 = new MemberScheduleResponse(1L, "짧은 일정 2", LocalDateTime.of(2022, 8, 5, 17, 0),
                LocalDateTime.of(2022, 8, 5, 19, 0), "짧은 일정 2의 메모", 3L, Color.COLOR_6);

        MemberScheduleResponses memberScheduleResponses = new MemberScheduleResponses(List.of(장기간_일정_1, 장기간_일정_2),
                List.of(종일_일정_1, 종일_일정_2), List.of(짧은_일정_1, 짧은_일정_2));

        given(scheduleService.findSchedulesByMemberId(any(), any()))
                .willReturn(memberScheduleResponses);

        // when & then
        mockMvc.perform(get("/api/members/me/schedules?startDate={startDate}&endDate={endDate}", startDate, endDate)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE))
                .andDo(print())
                .andDo(document("schedules/findAllByMember",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("startDate").description("일정 조회 시작 범위 (yyyy-mm-dd)"),
                                parameterWithName("endDate").description("일정 조회 마지막 범위 (yyyy-mm-dd)")
                        )
                ))
                .andExpect(status().isOk());
    }
}

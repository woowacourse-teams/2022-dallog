package com.allog.dallog.domain.schedule.presentation;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.common.config.TestConfig;
import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.schedule.application.ScheduleService;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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

    @DisplayName("일정을 제거하는데 성공하면 204를 반환한다.")
    @Test
    void 일정을_제거하는데_성공하면_204를_반환한다() throws Exception {
        // given
        Long scheduleId = 1L;
        willDoNothing()
                .given(scheduleService)
                .deleteById(any(), any());

        // when & then
        mockMvc.perform(delete("/api/schedules/{scheduleId}", scheduleId)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE))
                .andDo(print())
                .andDo(document("schedules/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isNoContent());
    }

    @DisplayName("일정을 제거하는데 해당 일정의 카테고리에 대한 권한이 없다면 403을 반환한다.")
    @Test
    void 일정을_제거하는데_해당_일정의_카테고리에_대한_권한이_없다면_403을_반환한다() throws Exception {
        // given
        Long scheduleId = 1L;
        willThrow(NoPermissionException.class)
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
        willThrow(NoSuchScheduleException.class)
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
}

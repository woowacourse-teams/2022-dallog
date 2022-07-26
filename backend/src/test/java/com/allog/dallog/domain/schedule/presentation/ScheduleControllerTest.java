package com.allog.dallog.domain.schedule.presentation;

import static com.allog.dallog.common.fixtures.ScheduleFixtures.END_DATE_TIME;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.MEMO;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.MONTH;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.START_DATE_TIME;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.TITLE;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.YEAR;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.schedule.application.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper ObjectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private ScheduleService scheduleService;

    @DisplayName("일정 정보를 등록한다.")
    @Test
    void 일정_정보를_등록한다() throws Exception {
        // given
        ScheduleCreateRequest request = new ScheduleCreateRequest(TITLE, START_DATE_TIME, END_DATE_TIME, MEMO);

        given(scheduleService.save(request)).willReturn(1L);

        // when & then
        mockMvc.perform(post("/api/schedules")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("schedule/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isCreated());
    }

    @DisplayName("월별 일정 정보를 조회한다.")
    @Test
    void 월별_일정_정보를_조회한다() throws Exception {
        //given
        given(scheduleService.findByYearAndMonth(YEAR, MONTH))
                .willReturn(List.of(new ScheduleResponse(1L, TITLE, START_DATE_TIME, END_DATE_TIME, MEMO)));

        // when & then
        mockMvc.perform(get("/api/schedules?year=2022&month=7")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("schedule/find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("year").description("년도"),
                                parameterWithName("month").description("월")
                        )
                ))
                .andExpect(status().isOk());
    }
}

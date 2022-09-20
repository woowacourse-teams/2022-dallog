package com.allog.dallog.presentation;

import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_10일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_15일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_18시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_20시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_20일_11시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_27일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_7일_16시_0분;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.schedule.application.AvailablePeriodsFinder;
import com.allog.dallog.domain.schedule.domain.Period;
import com.allog.dallog.domain.schedule.dto.response.PeriodResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

@WebMvcTest(SchedulerController.class)
class SchedulerControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @MockBean
    private AuthService authService;

    @MockBean
    private AvailablePeriodsFinder availablePeriodsFinder;

    @DisplayName("일정 조율 결과를 반환한다.")
    @Test
    void 일정_조율_결과를_반환한다() throws Exception {
        // given
        String startDateTime = "2022-07-01T00:00";
        String endDateTime = "2022-07-31T00:00";

        given(availablePeriodsFinder.getAvailablePeriods(any(), any()))
                .willReturn(List.of(
                        new PeriodResponse(new Period(날짜_2022년_7월_7일_16시_0분, 날짜_2022년_7월_10일_0시_0분)),
                        new PeriodResponse(new Period(날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분)),
                        new PeriodResponse(new Period(날짜_2022년_7월_16일_18시_0분, 날짜_2022년_7월_16일_20시_0분)),
                        new PeriodResponse(new Period(날짜_2022년_7월_20일_11시_59분, 날짜_2022년_7월_27일_0시_0분))
                ));

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get(
                                "/api/scheduler/categories/{categoryId}/available-periods?startDateTime={startDateTime}&endDateTime={endDateTime}",
                                1L, startDateTime, endDateTime)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE))
                .andDo(print())
                .andDo(document("scheduler/scheduleByCategory",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 ID")
                        ),
                        requestParameters(
                                parameterWithName("startDateTime").description("일정 조회 시작 범위 (yyyy-mm-dd'T'HH:mm)"),
                                parameterWithName("endDateTime").description("일정 조회 마지막 범위 (yyyy-mm-dd'T'HH:mm)")
                        )
                ))
                .andExpect(status().isOk());
    }

}

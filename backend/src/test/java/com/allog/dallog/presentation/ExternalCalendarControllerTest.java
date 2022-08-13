package com.allog.dallog.presentation;

import static com.allog.dallog.common.fixtures.ExternalCalendarFixtures.대한민국_공휴일;
import static com.allog.dallog.common.fixtures.ExternalCalendarFixtures.우아한테크코스;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(ExternalCalendarController.class)
class ExternalCalendarControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @MockBean
    private AuthService authService;

    @MockBean
    private ExternalCalendarService externalCalendarService;

    @DisplayName("외부 캘린더의 일정을 조회하면 상태코드 200을 반환한다.")
    @Test
    void 외부_캘린더의_일정을_조회하면_상태코드_200을_반환한다() throws Exception {
        // given
        given(externalCalendarService.findByMemberId(any())).willReturn(List.of(대한민국_공휴일, 우아한테크코스, 대한민국_공휴일));

        // when & then
        mockMvc.perform(get("/api/external-calendars/me")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("external-calendars/get",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT 토큰"))
                        )
                )
                .andExpect(status().isOk());
    }
}

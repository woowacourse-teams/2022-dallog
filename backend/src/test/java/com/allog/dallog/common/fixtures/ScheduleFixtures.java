package com.allog.dallog.common.fixtures;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import java.time.LocalDateTime;

public class ScheduleFixtures {

    /* 날짜 */
    public static final LocalDateTime 날짜_2022년_7월_1일_0시_0분 = LocalDateTime.of(2022, 7, 1, 0, 0);
    public static final LocalDateTime 날짜_2022년_7월_7일_16시_0분 = LocalDateTime.of(2022, 7, 7, 16, 0);
    public static final LocalDateTime 날짜_2022년_7월_10일_0시_0분 = LocalDateTime.of(2022, 7, 10, 0, 0);
    public static final LocalDateTime 날짜_2022년_7월_10일_11시_59분 = LocalDateTime.of(2022, 7, 10, 23, 59);
    public static final LocalDateTime 날짜_2022년_7월_11일_0시_0분 = LocalDateTime.of(2022, 7, 11, 0, 0);
    public static final LocalDateTime 날짜_2022년_7월_15일_16시_0분 = LocalDateTime.of(2022, 7, 15, 16, 0);
    public static final LocalDateTime 날짜_2022년_7월_16일_16시_0분 = LocalDateTime.of(2022, 7, 16, 16, 0);
    public static final LocalDateTime 날짜_2022년_7월_16일_16시_1분 = LocalDateTime.of(2022, 7, 16, 16, 1);
    public static final LocalDateTime 날짜_2022년_7월_16일_18시_0분 = LocalDateTime.of(2022, 7, 16, 18, 0);
    public static final LocalDateTime 날짜_2022년_7월_16일_20시_0분 = LocalDateTime.of(2022, 7, 16, 20, 0);
    public static final LocalDateTime 날짜_2022년_7월_17일_23시_59분 = LocalDateTime.of(2022, 7, 17, 23, 59);
    public static final LocalDateTime 날짜_2022년_7월_20일_0시_0분 = LocalDateTime.of(2022, 7, 20, 0, 0);
    public static final LocalDateTime 날짜_2022년_7월_20일_11시_59분 = LocalDateTime.of(2022, 7, 20, 23, 59);
    public static final LocalDateTime 날짜_2022년_7월_21일_0시_0분 = LocalDateTime.of(2022, 7, 21, 0, 0);
    public static final LocalDateTime 날짜_2022년_7월_27일_0시_0분 = LocalDateTime.of(2022, 7, 27, 0, 0);
    public static final LocalDateTime 날짜_2022년_7월_27일_11시_59분 = LocalDateTime.of(2022, 7, 27, 23, 59);
    public static final LocalDateTime 날짜_2022년_7월_28일_0시_0분 = LocalDateTime.of(2022, 7, 28, 0, 0);
    public static final LocalDateTime 날짜_2022년_7월_31일_0시_0분 = LocalDateTime.of(2022, 7, 31, 0, 0);
    public static final LocalDateTime 날짜_2022년_8월_15일_14시_0분 = LocalDateTime.of(2022, 8, 15, 14, 0);
    public static final LocalDateTime 날짜_2022년_8월_15일_17시_0분 = LocalDateTime.of(2022, 8, 15, 17, 0);
    public static final LocalDateTime 날짜_2022년_8월_15일_23시_59분 = LocalDateTime.of(2022, 8, 15, 23, 59);

    /* 알록달록 회의 */
    public static final String 알록달록_회의_제목 = "알록달록 회의";
    public static final LocalDateTime 알록달록_회의_시작일시 = LocalDateTime.of(2022, 7, 15, 16, 0);
    public static final LocalDateTime 알록달록_회의_종료일시 = LocalDateTime.of(2022, 7, 16, 16, 0);
    public static final String 알록달록_회의_메모 = "알록달록 회의가 있어요";
    public static final ScheduleCreateRequest 알록달록_회의_생성_요청 = new ScheduleCreateRequest(알록달록_회의_제목, 알록달록_회의_시작일시,
            알록달록_회의_종료일시, 알록달록_회의_메모);
    public static final ScheduleResponse 알록달록_회의_응답 = new ScheduleResponse(1L, 1L, 알록달록_회의_제목, 알록달록_회의_시작일시,
            알록달록_회의_종료일시, 알록달록_회의_메모, "NORMAL");

    /* 알록달록 회식 */
    public static final String 알록달록_회식_제목 = "알록달록 회식";
    public static final LocalDateTime 알록달록_회식_시작일시 = LocalDateTime.of(2022, 7, 7, 16, 0);
    public static final LocalDateTime 알록달록_회식_종료일시 = LocalDateTime.of(2022, 7, 9, 16, 0);
    public static final String 알록달록_회식_메모 = "알록달록 회식이 있어요";
    public static final ScheduleCreateRequest 알록달록_회식_생성_요청 = new ScheduleCreateRequest(알록달록_회식_제목, 알록달록_회식_시작일시,
            알록달록_회식_종료일시, 알록달록_회식_메모);

    /* 레벨 인터뷰 */
    public static final String 레벨_인터뷰_제목 = "레벨 인터뷰";
    public static final LocalDateTime 레벨_인터뷰_시작일시 = LocalDateTime.of(2022, 8, 7, 13, 0);
    public static final LocalDateTime 레벨_인터뷰_종료일시 = LocalDateTime.of(2022, 8, 7, 15, 0);
    public static final String 레벨_인터뷰_메모 = "레벨 인터뷰가 예정되어 있습니다.";
    public static final ScheduleCreateRequest 레벨_인터뷰_생성_요청 = new ScheduleCreateRequest(레벨_인터뷰_제목, 레벨_인터뷰_시작일시,
            레벨_인터뷰_종료일시, 레벨_인터뷰_메모);

    public static final String 매고라_제목 = "매고라";
    public static final String 매고라_메모 = "매고라가 예정되어 있습니다.";

    /* 장기간 일정 */
    public static final ScheduleCreateRequest 장기간_첫번째_요청 = new ScheduleCreateRequest("장기간 첫번째", 날짜_2022년_7월_1일_0시_0분,
            날짜_2022년_8월_15일_14시_0분, "");
    public static final ScheduleCreateRequest 장기간_두번째_요청 = new ScheduleCreateRequest("장기간 두번째", 날짜_2022년_7월_1일_0시_0분,
            날짜_2022년_7월_31일_0시_0분, "");
    public static final ScheduleCreateRequest 장기간_세번째_요청 = new ScheduleCreateRequest("장기간 세번째", 날짜_2022년_7월_1일_0시_0분,
            날짜_2022년_7월_16일_16시_1분, "");
    public static final ScheduleCreateRequest 장기간_네번째_요청 = new ScheduleCreateRequest("장기간 네번째", 날짜_2022년_7월_7일_16시_0분,
            날짜_2022년_7월_15일_16시_0분, "");
    public static final ScheduleCreateRequest 장기간_다섯번째_요청 = new ScheduleCreateRequest("장기간 다섯번째", 날짜_2022년_7월_31일_0시_0분,
            날짜_2022년_8월_15일_17시_0분, "");

    /* 종일 일정 */
    public static final ScheduleCreateRequest 종일_첫번째_일정 = new ScheduleCreateRequest("종일 첫번째", 날짜_2022년_7월_10일_0시_0분,
            날짜_2022년_7월_11일_0시_0분, "");
    public static final ScheduleCreateRequest 종일_두번째_일정 = new ScheduleCreateRequest("종일 두번째", 날짜_2022년_7월_20일_0시_0분,
            날짜_2022년_7월_21일_0시_0분, "");
    public static final ScheduleCreateRequest 종일_세번째_일정 = new ScheduleCreateRequest("종일 세번째", 날짜_2022년_7월_27일_0시_0분,
            날짜_2022년_7월_28일_0시_0분, "");

    /* 몇시간 일정 */
    public static final ScheduleCreateRequest 몇시간_첫번째_일정 = new ScheduleCreateRequest("몇시간 첫번째", 날짜_2022년_7월_16일_16시_0분,
            날짜_2022년_7월_16일_20시_0분, "");
    public static final ScheduleCreateRequest 몇시간_두번째_일정 = new ScheduleCreateRequest("몇시간 두번째", 날짜_2022년_7월_16일_16시_0분,
            날짜_2022년_7월_16일_18시_0분, "");
    public static final ScheduleCreateRequest 몇시간_세번째_일정 = new ScheduleCreateRequest("몇시간 세번째", 날짜_2022년_7월_16일_16시_0분,
            날짜_2022년_7월_16일_16시_1분, "");
    public static final ScheduleCreateRequest 몇시간_네번째_일정 = new ScheduleCreateRequest("몇시간 네번째", 날짜_2022년_7월_16일_18시_0분,
            날짜_2022년_7월_16일_18시_0분, "");

}

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
    public static final LocalDateTime 날짜_2022년_7월_15일_16시_0분 = LocalDateTime.of(2022, 7, 15, 16, 0);
    public static final LocalDateTime 날짜_2022년_7월_16일_16시_0분 = LocalDateTime.of(2022, 7, 16, 16, 0);
    public static final LocalDateTime 날짜_2022년_7월_16일_16시_1분 = LocalDateTime.of(2022, 7, 16, 16, 1);
    public static final LocalDateTime 날짜_2022년_7월_16일_18시_0분 = LocalDateTime.of(2022, 7, 16, 18, 0);
    public static final LocalDateTime 날짜_2022년_7월_16일_20시_0분 = LocalDateTime.of(2022, 7, 16, 20, 0);
    public static final LocalDateTime 날짜_2022년_7월_20일_0시_0분 = LocalDateTime.of(2022, 7, 20, 0, 0);
    public static final LocalDateTime 날짜_2022년_7월_20일_11시_59분 = LocalDateTime.of(2022, 7, 20, 23, 59);
    public static final LocalDateTime 날짜_2022년_7월_27일_0시_0분 = LocalDateTime.of(2022, 7, 27, 0, 0);
    public static final LocalDateTime 날짜_2022년_7월_27일_11시_59분 = LocalDateTime.of(2022, 7, 27, 23, 59);
    public static final LocalDateTime 날짜_2022년_7월_31일_0시_0분 = LocalDateTime.of(2022, 7, 31, 0, 0);
    public static final LocalDateTime 날짜_2022년_8월_15일_14시_0분 = LocalDateTime.of(2022, 8, 15, 14, 0);
    public static final LocalDateTime 날짜_2022년_8월_15일_17시_0분 = LocalDateTime.of(2022, 8, 15, 17, 0);

    /* 알록달록 회의 */
    public static final String 알록달록_회의_제목 = "알록달록 회의";
    public static final LocalDateTime 알록달록_회의_시작일시 = LocalDateTime.of(2022, 7, 15, 16, 0);
    public static final LocalDateTime 알록달록_회의_종료일시 = LocalDateTime.of(2022, 7, 16, 16, 0);
    public static final String 알록달록_회의_메모 = "알록달록 회의가 있어요";
    public static final ScheduleCreateRequest 알록달록_회의_생성_요청 = new ScheduleCreateRequest(알록달록_회의_제목, 알록달록_회의_시작일시,
            알록달록_회의_종료일시, 알록달록_회의_메모);

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

    /* 테코톡 */
    public static final String 테코톡_제목 = "테코톡";
    public static final LocalDateTime 테코톡_시작일시 = LocalDateTime.of(2022, 8, 4, 14, 0);
    public static final LocalDateTime 테코톡_종료일시 = LocalDateTime.of(2022, 8, 4, 15, 0);
    public static final String 테코톡_메모 = "매주 목요일 테코톡";
    public static final ScheduleCreateRequest 테코톡_생성_요청 = new ScheduleCreateRequest(테코톡_제목, 테코톡_시작일시,
            테코톡_종료일시, 테코톡_메모);

    /* 학습 */
    public static final String 학습_제목 = "학습";
    public static final LocalDateTime 학습_시작일시 = LocalDateTime.of(2022, 8, 1, 10, 0);
    public static final LocalDateTime 학습_종료일시 = LocalDateTime.of(2022, 8, 1, 18, 0);
    public static final String 학습_메모 = "프로그래밍 학습";
    public static final ScheduleCreateRequest 학습_생성_요청 = new ScheduleCreateRequest(학습_제목, 학습_시작일시, 학습_종료일시, 학습_메모);

    public static Schedule 알록달록_회의(final Category category) {
        return new Schedule(category, 알록달록_회의_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시, 알록달록_회의_메모);
    }

    public static Schedule 알록달록_회식(final Category category) {
        return new Schedule(category, 알록달록_회식_제목, 알록달록_회식_시작일시, 알록달록_회식_종료일시, 알록달록_회식_메모);
    }

    public static Schedule 레벨_인터뷰(final Category category) {
        return new Schedule(category, 레벨_인터뷰_제목, 레벨_인터뷰_시작일시, 레벨_인터뷰_종료일시, 레벨_인터뷰_메모);
    }

    public static Schedule 테코톡(final Category category) {
        return new Schedule(category, 테코톡_제목, 테코톡_시작일시, 테코톡_종료일시, 테코톡_메모);
    }

    public static Schedule 학습(final Category category) {
        return new Schedule(category, 학습_제목, 학습_시작일시, 학습_종료일시, 학습_메모);
    }

    public static ScheduleResponse 알록달록_회의_응답() {
        return new ScheduleResponse(1L, 1L, 알록달록_회의_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시, 알록달록_회의_메모);
    }
}

package com.allog.dallog.domain.schedule.application;

import static com.allog.dallog.common.fixtures.AuthFixtures.관리자_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.리버_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.매트_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.파랑_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.후디_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.ExternalCategoryFixtures.대한민국_공휴일_생성_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_10일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_10일_11시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_15일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_1분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_18시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_20시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_1일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_20일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_20일_11시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_27일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_27일_11시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_31일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_7일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_14시_0분;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.response.PeriodResponse;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionUpdateRequest;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AvailablePeriodsFinderTest extends ServiceTest {

    @Autowired
    private AvailablePeriodsFinder availablePeriodsFinder;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubscriptionService subscriptionService;

    @DisplayName("비어있는 기간 목록을 반환한다.")
    @Test
    void 비어있는_기간_목록을_반환한다() {
        // given
        /* 관리자 및 카테고리 생성 */
        Long 관리자_id = parseMemberId(관리자_인증_코드_토큰_요청());
        CategoryResponse 공통_일정 = categoryService.save(관리자_id, 공통_일정_생성_요청);
        CategoryResponse BE_일정 = categoryService.save(관리자_id, BE_일정_생성_요청);
        CategoryResponse FE_일정 = categoryService.save(관리자_id, FE_일정_생성_요청);

        /* 카테고리에 일정 추가 */
        scheduleService.save(관리자_id, 공통_일정.getId(),
                new ScheduleCreateRequest("공통 일정 1", 날짜_2022년_7월_7일_16시_0분, 날짜_2022년_7월_10일_0시_0분, ""));
        scheduleService.save(관리자_id, 공통_일정.getId(),
                new ScheduleCreateRequest("공통 일정 2", 날짜_2022년_7월_10일_11시_59분, 날짜_2022년_7월_15일_16시_0분, ""));
        scheduleService.save(관리자_id, 공통_일정.getId(),
                new ScheduleCreateRequest("공통 일정 3", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_16시_1분, ""));
        scheduleService.save(관리자_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 1", 날짜_2022년_7월_16일_18시_0분, 날짜_2022년_7월_16일_20시_0분, ""));
        scheduleService.save(관리자_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 2", 날짜_2022년_7월_16일_20시_0분, 날짜_2022년_7월_20일_0시_0분, ""));
        scheduleService.save(관리자_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 3", 날짜_2022년_7월_20일_11시_59분, 날짜_2022년_7월_27일_0시_0분, ""));
        scheduleService.save(관리자_id, FE_일정.getId(),
                new ScheduleCreateRequest("프론트엔드 일정 1", 날짜_2022년_7월_27일_11시_59분, 날짜_2022년_7월_31일_0시_0분, ""));
        scheduleService.save(관리자_id, FE_일정.getId(),
                new ScheduleCreateRequest("프론트엔드 일정 2", 날짜_2022년_7월_31일_0시_0분, 날짜_2022년_8월_15일_14시_0분, ""));

        /* 카테고리 구독 */
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        Long 파랑_id = parseMemberId(파랑_인증_코드_토큰_요청());
        Long 매트_id = parseMemberId(매트_인증_코드_토큰_요청());
        Long 리버_id = parseMemberId(리버_인증_코드_토큰_요청());

        subscriptionService.save(후디_id, 공통_일정.getId());
        subscriptionService.save(파랑_id, 공통_일정.getId());
        subscriptionService.save(매트_id, 공통_일정.getId());
        subscriptionService.save(리버_id, 공통_일정.getId());

        subscriptionService.save(매트_id, BE_일정.getId());
        subscriptionService.save(매트_id, FE_일정.getId());
        subscriptionService.save(리버_id, FE_일정.getId());

        categoryService.save(매트_id, 대한민국_공휴일_생성_요청);

        // when
        List<PeriodResponse> actual = availablePeriodsFinder.getAvailablePeriods(공통_일정.getId(),
                new DateRangeRequest("2022-07-01T00:00", "2022-08-31T00:00"));

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(8);
            assertThat(actual.stream().map(PeriodResponse::getStartDateTime)).containsExactly(날짜_2022년_7월_1일_0시_0분,
                    날짜_2022년_7월_10일_0시_0분, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_1분, 날짜_2022년_7월_20일_0시_0분,
                    날짜_2022년_7월_27일_0시_0분, 날짜_2022년_8월_15일_14시_0분, LocalDateTime.of(2022, 8, 20, 0, 0));
            assertThat(actual.stream().map(PeriodResponse::getEndDateTime)).containsExactly(날짜_2022년_7월_7일_16시_0분,
                    날짜_2022년_7월_10일_11시_59분, 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_18시_0분, 날짜_2022년_7월_20일_11시_59분,
                    날짜_2022년_7월_27일_11시_59분, LocalDateTime.of(2022, 8, 20, 0, 0), LocalDateTime.of(2022, 8, 31, 0, 0));
        });
    }

    @DisplayName("체크하지 않은 구독은 일정 산정 대상에서 제외된다.")
    @Test
    void 체크하지_않은_구독은_일정_산정_대상에서_제외된다() {
        // given
        /* 관리자 및 카테고리 생성 */
        Long 관리자_id = parseMemberId(관리자_인증_코드_토큰_요청());
        CategoryResponse 공통_일정 = categoryService.save(관리자_id, 공통_일정_생성_요청);
        CategoryResponse BE_일정 = categoryService.save(관리자_id, BE_일정_생성_요청);
        CategoryResponse FE_일정 = categoryService.save(관리자_id, FE_일정_생성_요청);

        /* 카테고리에 일정 추가 */
        scheduleService.save(관리자_id, 공통_일정.getId(),
                new ScheduleCreateRequest("공통 일정 1", 날짜_2022년_7월_7일_16시_0분, 날짜_2022년_7월_10일_0시_0분, ""));
        scheduleService.save(관리자_id, 공통_일정.getId(),
                new ScheduleCreateRequest("공통 일정 2", 날짜_2022년_7월_10일_11시_59분, 날짜_2022년_7월_15일_16시_0분, ""));
        scheduleService.save(관리자_id, 공통_일정.getId(),
                new ScheduleCreateRequest("공통 일정 3", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_16시_1분, ""));
        scheduleService.save(관리자_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 1", 날짜_2022년_7월_16일_18시_0분, 날짜_2022년_7월_16일_20시_0분, ""));
        scheduleService.save(관리자_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 2", 날짜_2022년_7월_16일_20시_0분, 날짜_2022년_7월_20일_0시_0분, ""));
        scheduleService.save(관리자_id, BE_일정.getId(),
                new ScheduleCreateRequest("백엔드 일정 3", 날짜_2022년_7월_20일_11시_59분, 날짜_2022년_7월_27일_0시_0분, ""));
        scheduleService.save(관리자_id, FE_일정.getId(),
                new ScheduleCreateRequest("프론트엔드 일정 1", 날짜_2022년_7월_27일_11시_59분, 날짜_2022년_7월_31일_0시_0분, ""));
        scheduleService.save(관리자_id, FE_일정.getId(),
                new ScheduleCreateRequest("프론트엔드 일정 2", 날짜_2022년_7월_31일_0시_0분, 날짜_2022년_8월_15일_14시_0분, ""));

        /* 카테고리 구독 */
        Long 후디_id = parseMemberId(후디_인증_코드_토큰_요청());
        Long 파랑_id = parseMemberId(파랑_인증_코드_토큰_요청());
        Long 매트_id = parseMemberId(매트_인증_코드_토큰_요청());
        Long 리버_id = parseMemberId(리버_인증_코드_토큰_요청());

        subscriptionService.save(후디_id, 공통_일정.getId());
        subscriptionService.save(파랑_id, 공통_일정.getId());
        subscriptionService.save(매트_id, 공통_일정.getId());
        subscriptionService.save(리버_id, 공통_일정.getId());
        subscriptionService.save(매트_id, BE_일정.getId());

        SubscriptionResponse 매트_FE_일정_구독 = subscriptionService.save(매트_id, FE_일정.getId());
        SubscriptionResponse 리버_FE_일정_구독 = subscriptionService.save(리버_id, FE_일정.getId());

        subscriptionService.update(매트_FE_일정_구독.getId(), 매트_id,
                new SubscriptionUpdateRequest(Color.COLOR_1, false));
        subscriptionService.update(리버_FE_일정_구독.getId(), 리버_id,
                new SubscriptionUpdateRequest(Color.COLOR_1, false));

        // when
        List<PeriodResponse> actual = availablePeriodsFinder.getAvailablePeriods(공통_일정.getId(),
                new DateRangeRequest("2022-07-01T00:00", "2022-08-31T00:00"));

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(7);
            assertThat(actual.stream().map(PeriodResponse::getStartDateTime)).containsExactly(날짜_2022년_7월_1일_0시_0분,
                    날짜_2022년_7월_10일_0시_0분, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_1분, 날짜_2022년_7월_20일_0시_0분,
                    날짜_2022년_7월_27일_0시_0분, 날짜_2022년_8월_15일_14시_0분);
            assertThat(actual.stream().map(PeriodResponse::getEndDateTime)).containsExactly(날짜_2022년_7월_7일_16시_0분,
                    날짜_2022년_7월_10일_11시_59분, 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_18시_0분, 날짜_2022년_7월_20일_11시_59분,
                    날짜_2022년_7월_27일_11시_59분,
                    LocalDateTime.of(2022, 8, 31, 0, 0));
        });
    }
}

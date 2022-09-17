package com.allog.dallog.domain.composition.application;

import static com.allog.dallog.common.fixtures.AuthFixtures.MEMBER_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.관리자_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.리버_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.매트_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.파랑_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.후디_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.우아한테크코스_외부_일정_생성_요청;
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
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_17시_0분;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.schedule.application.ScheduleService;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponse;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponses;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CalendarServiceTest extends ServiceTest {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExternalCategoryDetailRepository externalCategoryDetailRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("시작일시와 종료일시로 유저의 캘린더를 일정 유형에 따라 분류하고 정렬하여 반환한다.")
    @Test
    void 시작일시와_종료일시로_유저의_캘린더를_일정_유형에_따라_분류하고_정렬하여_반환한다() {
        // given
        Long memberId = parseMemberId(MEMBER_인증_코드_토큰_요청());

        CategoryResponse BE_일정_응답 = categoryService.save(memberId, BE_일정_생성_요청);
        Category BE_일정 = categoryRepository.getById(BE_일정_응답.getId());

        subscriptionService.save(memberId, BE_일정.getId());

        /* 장기간 일정 */
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("장기간 첫번째", 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_8월_15일_14시_0분, ""));
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("장기간 두번째", 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_7월_31일_0시_0분, ""));
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("장기간 세번째", 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_7월_16일_16시_1분, ""));
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("장기간 네번째", 날짜_2022년_7월_7일_16시_0분, 날짜_2022년_7월_15일_16시_0분, ""));
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("장기간 다섯번째", 날짜_2022년_7월_31일_0시_0분, 날짜_2022년_8월_15일_17시_0분, ""));

        /* 종일 일정 */
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("종일 첫번째", 날짜_2022년_7월_10일_0시_0분, 날짜_2022년_7월_10일_11시_59분, ""));
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("종일 두번째", 날짜_2022년_7월_20일_0시_0분, 날짜_2022년_7월_20일_11시_59분, ""));
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("종일 세번째", 날짜_2022년_7월_27일_0시_0분, 날짜_2022년_7월_27일_11시_59분, ""));

        /* 몇시간 일정 */
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("몇시간 첫번째", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_20시_0분, ""));
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("몇시간 두번째", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_18시_0분, ""));
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("몇시간 세번째", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_16시_1분, ""));
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("몇시간 네번째", 날짜_2022년_7월_16일_18시_0분, 날짜_2022년_7월_16일_18시_0분, ""));

        CategoryResponse 우아한테크코스_외부_일정_응답 = categoryService.save(memberId, 우아한테크코스_외부_일정_생성_요청);
        Category 우아한테크코스 = categoryRepository.getById(우아한테크코스_외부_일정_응답.getId());
        externalCategoryDetailRepository.save(new ExternalCategoryDetail(우아한테크코스, "dfggsdfasdasadsgs"));

        subscriptionService.save(memberId, 우아한테크코스.getId());

        // when
        MemberScheduleResponses memberScheduleResponses = calendarService.findSchedulesByMemberId(memberId,
                new DateRangeRequest("2022-07-01T00:00", "2022-08-15T23:59"));

        // then
        assertAll(() -> {
            assertThat(memberScheduleResponses.getLongTerms()).extracting(MemberScheduleResponse::getTitle)
                    .contains("장기간 첫번째", "장기간 두번째", "장기간 세번째", "장기간 네번째", "장기간 다섯번째");
            assertThat(memberScheduleResponses.getAllDays()).extracting(MemberScheduleResponse::getTitle)
                    .contains("종일 첫번째", "종일 두번째", "종일 세번째");
            assertThat(memberScheduleResponses.getFewHours()).extracting(MemberScheduleResponse::getTitle)
                    .contains("몇시간 첫번째", "몇시간 두번째", "몇시간 세번째", "몇시간 네번째");
        });
    }

    // TODO: 외부 일정을 잘 가져오는지를 테스트 해야함
    @DisplayName("카테고리를 구독하는 유저들의 모든 내부 일정을 가져온다.")
    @Test
    void 카테고리를_구독하는_유저들의_모든_내부_일정을_가져온다() {
        // given
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

        // when
        List<IntegrationSchedule> actual = calendarService.getSchedulesBySubscriberIds(
                List.of(후디_id, 파랑_id, 매트_id, 리버_id), new DateRangeRequest("2022-07-07T16:00", "2022-08-15T14:00"));

        // then
        assertThat(actual.stream().map(IntegrationSchedule::getTitle))
                .containsExactly("공통 일정 1", "공통 일정 2", "공통 일정 3", "백엔드 일정 1", "백엔드 일정 2", "백엔드 일정 3", "프론트엔드 일정 1",
                        "프론트엔드 일정 2");
    }
}

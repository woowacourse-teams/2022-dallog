package com.allog.dallog.domain.composition.application;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.우아한테크코스_외부_일정_생성_요청;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
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
import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.schedule.application.ScheduleService;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponse;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponses;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CalendarServiceTest extends ServiceTest {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @Autowired
    private ExternalCategoryDetailRepository externalCategoryDetailRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ScheduleService scheduleService;

    @DisplayName("시작일시와 종료일시로 유저의 캘린더를 일정 유형에 따라 분류하고 정렬하여 반환한다.")
    @Test
    void 시작일시와_종료일시로_유저의_캘린더를_일정_유형에_따라_분류하고_정렬하여_반환한다() {
        // given
        MemberResponse 후디 = memberService.save(후디());
        oAuthTokenRepository.save(new OAuthToken(memberService.getMember(후디.getId()), "wegwefaasdasdasda"));

        CategoryResponse BE_일정_응답 = categoryService.save(후디.getId(), BE_일정_생성_요청);
        Category BE_일정 = categoryService.getCategory(BE_일정_응답.getId());

        subscriptionService.save(후디.getId(), BE_일정.getId());

        /* 장기간 일정 */
        scheduleService.save(후디.getId(), BE_일정.getId(),
                new ScheduleCreateRequest("장기간 첫번째", 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_8월_15일_14시_0분, ""));
        scheduleService.save(후디.getId(), BE_일정.getId(),
                new ScheduleCreateRequest("장기간 두번째", 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_7월_31일_0시_0분, ""));
        scheduleService.save(후디.getId(), BE_일정.getId(),
                new ScheduleCreateRequest("장기간 세번째", 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_7월_16일_16시_1분, ""));
        scheduleService.save(후디.getId(), BE_일정.getId(),
                new ScheduleCreateRequest("장기간 네번째", 날짜_2022년_7월_7일_16시_0분, 날짜_2022년_7월_15일_16시_0분, ""));
        scheduleService.save(후디.getId(), BE_일정.getId(),
                new ScheduleCreateRequest("장기간 다섯번째", 날짜_2022년_7월_31일_0시_0분, 날짜_2022년_8월_15일_17시_0분, ""));

        /* 종일 일정 */
        scheduleService.save(후디.getId(), BE_일정.getId(),
                new ScheduleCreateRequest("종일 첫번째", 날짜_2022년_7월_10일_0시_0분, 날짜_2022년_7월_10일_11시_59분, ""));
        scheduleService.save(후디.getId(), BE_일정.getId(),
                new ScheduleCreateRequest("종일 두번째", 날짜_2022년_7월_20일_0시_0분, 날짜_2022년_7월_20일_11시_59분, ""));
        scheduleService.save(후디.getId(), BE_일정.getId(),
                new ScheduleCreateRequest("종일 세번째", 날짜_2022년_7월_27일_0시_0분, 날짜_2022년_7월_27일_11시_59분, ""));

        /* 몇시간 일정 */
        scheduleService.save(후디.getId(), BE_일정.getId(),
                new ScheduleCreateRequest("몇시간 첫번째", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_20시_0분, ""));
        scheduleService.save(후디.getId(), BE_일정.getId(),
                new ScheduleCreateRequest("몇시간 두번째", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_18시_0분, ""));
        scheduleService.save(후디.getId(), BE_일정.getId(),
                new ScheduleCreateRequest("몇시간 세번째", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_16시_1분, ""));
        scheduleService.save(후디.getId(), BE_일정.getId(),
                new ScheduleCreateRequest("몇시간 네번째", 날짜_2022년_7월_16일_18시_0분, 날짜_2022년_7월_16일_18시_0분, ""));

        CategoryResponse 우아한테크코스_외부_일정_응답 = categoryService.save(후디.getId(), 우아한테크코스_외부_일정_생성_요청);
        Category 우아한테크코스 = categoryService.getCategory(우아한테크코스_외부_일정_응답.getId());
        externalCategoryDetailRepository.save(new ExternalCategoryDetail(우아한테크코스, "dfggsdfasdasadsgs"));

        subscriptionService.save(후디.getId(), 우아한테크코스.getId());

        // when
        MemberScheduleResponses memberScheduleResponses = calendarService.findSchedulesByMemberId(후디.getId(),
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
}

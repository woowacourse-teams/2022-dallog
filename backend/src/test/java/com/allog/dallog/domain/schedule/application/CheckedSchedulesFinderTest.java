package com.allog.dallog.domain.schedule.application;

import static com.allog.dallog.common.fixtures.AuthFixtures.MEMBER_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.우아한테크코스_외부_일정_생성_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_10일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_11일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_15일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_1분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_18시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_20시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_1일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_20일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_21일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_27일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_28일_0시_0분;
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
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponse;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CheckedSchedulesFinderTest extends ServiceTest {

    @Autowired
    private CheckedSchedulesFinder checkedSchedulesFinder;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExternalCategoryDetailRepository externalCategoryDetailRepository;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("시작일시와 종료일시로 유저의 달력을 일정 유형에 따라 분류하고 정렬하여 반환한다.")
    @Test
    void 시작일시와_종료일시로_유저의_달력을_일정_유형에_따라_분류하고_정렬하여_반환한다() {
        // given
        Long memberId = parseMemberId(MEMBER_인증_코드_토큰_요청());

        CategoryResponse BE_일정_응답 = categoryService.save(memberId, BE_일정_생성_요청);
        Category BE_일정 = categoryRepository.getById(BE_일정_응답.getId());

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
                new ScheduleCreateRequest("종일 첫번째", 날짜_2022년_7월_10일_0시_0분, 날짜_2022년_7월_11일_0시_0분, ""));
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("종일 두번째", 날짜_2022년_7월_20일_0시_0분, 날짜_2022년_7월_21일_0시_0분, ""));
        scheduleService.save(memberId, BE_일정.getId(),
                new ScheduleCreateRequest("종일 세번째", 날짜_2022년_7월_27일_0시_0분, 날짜_2022년_7월_28일_0시_0분, ""));

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

        // when
        MemberScheduleResponses memberScheduleResponses = checkedSchedulesFinder.findMyCheckedSchedules(
                memberId, new DateRangeRequest("2022-07-01T00:00", "2022-08-15T23:59"));

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

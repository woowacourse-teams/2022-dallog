package com.allog.dallog.domain.schedule.application;

import static com.allog.dallog.common.Constants.MEMBER_이름;
import static com.allog.dallog.common.Constants.MEMBER_이메일;
import static com.allog.dallog.common.Constants.MEMBER_프로필_URL;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.몇시간_네번째_일정;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.몇시간_두번째_일정;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.몇시간_세번째_일정;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.몇시간_첫번째_일정;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.장기간_네번째_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.장기간_다섯번째_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.장기간_두번째_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.장기간_세번째_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.장기간_첫번째_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.종일_두번째_일정;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.종일_세번째_일정;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.종일_첫번째_일정;
import static com.allog.dallog.domain.member.domain.SocialType.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.IntegrationTest;
import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.response.IntegrationScheduleResponse;
import com.allog.dallog.domain.schedule.dto.response.IntegrationScheduleResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CheckedSchedulesFinderTest extends IntegrationTest {

    @Autowired
    private CheckedSchedulesFinder checkedSchedulesFinder;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @DisplayName("시작일시와 종료일시로 회원의 달력을 일정 유형에 따라 분류하고 정렬하여 반환한다.")
    @Test
    void 시작일시와_종료일시로_회원의_달력을_일정_유형에_따라_분류하고_정렬하여_반환한다() {
        // given
        Member member = memberRepository.save(new Member(MEMBER_이메일, MEMBER_이름, MEMBER_프로필_URL, GOOGLE));
        oAuthTokenRepository.save(new OAuthToken(member, "refreshToken"));

        CategoryResponse BE_일정_응답 = categoryService.save(member.getId(), BE_일정_생성_요청);
        Category BE_일정 = categoryRepository.getById(BE_일정_응답.getId());

        /* 장기간 일정 */
        scheduleService.save(member.getId(), BE_일정.getId(), 장기간_첫번째_요청);
        scheduleService.save(member.getId(), BE_일정.getId(), 장기간_두번째_요청);
        scheduleService.save(member.getId(), BE_일정.getId(), 장기간_세번째_요청);
        scheduleService.save(member.getId(), BE_일정.getId(), 장기간_네번째_요청);
        scheduleService.save(member.getId(), BE_일정.getId(), 장기간_다섯번째_요청);

        /* 종일 일정 */
        scheduleService.save(member.getId(), BE_일정.getId(), 종일_첫번째_일정);
        scheduleService.save(member.getId(), BE_일정.getId(), 종일_두번째_일정);
        scheduleService.save(member.getId(), BE_일정.getId(), 종일_세번째_일정);

        /* 몇시간 일정 */
        scheduleService.save(member.getId(), BE_일정.getId(), 몇시간_첫번째_일정);
        scheduleService.save(member.getId(), BE_일정.getId(), 몇시간_두번째_일정);
        scheduleService.save(member.getId(), BE_일정.getId(), 몇시간_세번째_일정);
        scheduleService.save(member.getId(), BE_일정.getId(), 몇시간_네번째_일정);

        // when
        IntegrationScheduleResponses integrationScheduleResponses = checkedSchedulesFinder.findMyCheckedSchedules(
                member.getId(), new DateRangeRequest("2022-07-01T00:00", "2022-08-15T23:59"));

        // then
        assertAll(() -> {
            assertThat(integrationScheduleResponses.getLongTerms()).extracting(IntegrationScheduleResponse::getTitle)
                    .contains("장기간 첫번째", "장기간 두번째", "장기간 세번째", "장기간 네번째", "장기간 다섯번째");
            assertThat(integrationScheduleResponses.getAllDays()).extracting(IntegrationScheduleResponse::getTitle)
                    .contains("종일 첫번째", "종일 두번째", "종일 세번째");
            assertThat(integrationScheduleResponses.getFewHours()).extracting(IntegrationScheduleResponse::getTitle)
                    .contains("몇시간 첫번째", "몇시간 두번째", "몇시간 세번째", "몇시간 네번째");
        });
    }
}

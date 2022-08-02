package com.allog.dallog.domain.schedule.application;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.MemberFixtures.리버;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_15일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_1일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_31일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_생성_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_시작일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_종료일시;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.schedule.exception.InvalidScheduleException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MemberService memberService;

    @DisplayName("새로운 일정을 생성한다.")
    @Test
    void 새로운_일정을_생성한다() {
        // given & when
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse BE_일정 = categoryService.save(후디.getId(), BE_일정_생성_요청);
        Long id = scheduleService.save(후디.getId(), BE_일정.getId(), 알록달록_회의_생성_요청);

        // then
        assertThat(id).isNotNull();
    }

    @DisplayName("새로운 일정을 생성 할 떄 일정 제목의 길이가 20을 초과하는 경우 예외를 던진다.")
    @Test
    void 새로운_일정을_생성_할_때_일정_제목의_길이가_20을_초과하는_경우_예외를_던진다() {
        // given
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse BE_일정 = categoryService.save(후디.getId(), BE_일정_생성_요청);

        String 잘못된_일정_제목 = "일이삼사오육칠팔구십일이삼사오육칠팔구십일";
        ScheduleCreateRequest 잘못된_일정_생성_요청 = new ScheduleCreateRequest(잘못된_일정_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시,
                알록달록_회의_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(후디.getId(), BE_일정.getId(), 잘못된_일정_생성_요청)).
                isInstanceOf(InvalidScheduleException.class);
    }

    @DisplayName("새로운 일정을 생성 할 떄 일정 메모의 길이가 255를 초과하는 경우 예외를 던진다.")
    @Test
    void 새로운_일정을_생성_할_때_일정_메모의_길이가_255를_초과하는_경우_예외를_던진다() {
        // given
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse BE_일정 = categoryService.save(후디.getId(), BE_일정_생성_요청);

        String 잘못된_일정_메모 = "1".repeat(256);
        ScheduleCreateRequest 잘못된_일정_생성_요청 = new ScheduleCreateRequest(알록달록_회의_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시,
                잘못된_일정_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(후디.getId(), BE_일정.getId(), 잘못된_일정_생성_요청)).
                isInstanceOf(InvalidScheduleException.class);
    }

    @DisplayName("새로운 일정을 생성 할 떄 종료일시가 시작일시 이전이라면 예외를 던진다.")
    @Test
    void 새로운_일정을_생성_할_때_종료일시가_시작일시_이전이라면_예외를_던진다() {
        // given
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse BE_일정 = categoryService.save(후디.getId(), BE_일정_생성_요청);

        LocalDateTime 시작일시 = 날짜_2022년_7월_15일_16시_0분;
        LocalDateTime 종료일시 = 날짜_2022년_7월_1일_0시_0분;
        ScheduleCreateRequest 일정_생성_요청 = new ScheduleCreateRequest(알록달록_회의_제목, 시작일시, 종료일시, 알록달록_회의_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(후디.getId(), BE_일정.getId(), 일정_생성_요청)).
                isInstanceOf(InvalidScheduleException.class);
    }

    @DisplayName("일정 생성 요청자가 카테고리의 생성자가 아닌경우 예외를 던진다")
    @Test
    void 일정_생성_요청자가_카테고리의_생성자가_아닌경우_예외를_던진다() {
        // given
        MemberResponse 리버 = memberService.save(리버());
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse BE_일정 = categoryService.save(후디.getId(), BE_일정_생성_요청);

        LocalDateTime 시작일시 = 날짜_2022년_7월_15일_16시_0분;
        LocalDateTime 종료일시 = 날짜_2022년_7월_31일_0시_0분;
        ScheduleCreateRequest 일정_생성_요청 = new ScheduleCreateRequest(알록달록_회의_제목, 시작일시, 종료일시, 알록달록_회의_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(리버.getId(), BE_일정.getId(), 일정_생성_요청)).
                isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("일정 생성시 전달한 카테고리가 존재하지 않는다면 예외를 던진다.")
    @Test
    void 일정_생성시_전달한_카테고리가_존재하지_않는다면_예외를_던진다() {
        // given
        MemberResponse 후디 = memberService.save(후디());

        LocalDateTime 시작일시 = 날짜_2022년_7월_15일_16시_0분;
        LocalDateTime 종료일시 = 날짜_2022년_7월_31일_0시_0분;
        ScheduleCreateRequest 일정_생성_요청 = new ScheduleCreateRequest(알록달록_회의_제목, 시작일시, 종료일시, 알록달록_회의_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(후디.getId(), 999L, 일정_생성_요청)).
                isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("일정의 ID로 단건 일정을 조회한다.")
    @Test
    void 일정의_ID로_단건_일정을_조회한다() {
        // given
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse BE_일정 = categoryService.save(후디.getId(), BE_일정_생성_요청);
        Long id = scheduleService.save(후디.getId(), BE_일정.getId(), 알록달록_회의_생성_요청);

        // when
        ScheduleResponse response = scheduleService.findById(id);

        // then
        assertAll(() -> {
            assertThat(response.getId()).isEqualTo(id);
            assertThat(response.getTitle()).isEqualTo(알록달록_회의_제목);
            assertThat(response.getStartDateTime()).isEqualTo(알록달록_회의_시작일시);
            assertThat(response.getEndDateTime()).isEqualTo(알록달록_회의_종료일시);
            assertThat(response.getMemo()).isEqualTo(알록달록_회의_메모);
        });
    }
}

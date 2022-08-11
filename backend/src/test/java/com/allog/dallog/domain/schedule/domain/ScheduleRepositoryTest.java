package com.allog.dallog.domain.schedule.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_15일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_1분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_1일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_31일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_7일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_14시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_17시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.매고라_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.매고라_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회식_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회식_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_제목;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.domain.RepositoryTest;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ScheduleRepositoryTest extends RepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("카테고리와 시작일시, 종료일시를 전달하면 그 사이에 해당하는 일정을 조회한다.")
    @Test
    void 카테고리와_시작일시_종료일시를_전달하면_그_사이에_해당하는_일정을_조회한다() {
        // given
        Member 관리자 = 관리자();
        memberRepository.save(관리자);

        Category BE_일정 = BE_일정(관리자);
        categoryRepository.save(BE_일정);

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);

        // when
        List<Schedule> schedules = scheduleRepository.findByCategoryIdAndBetween(BE_일정, 날짜_2022년_7월_1일_0시_0분,
                날짜_2022년_7월_31일_0시_0분);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("카테고리가 여러개 일때, 카테고리와 시작일시, 종료일시를 전달하면 그 사이에 해당하는 일정을 조회한다.")
    @Test
    void 카테고리가_여러개_일때_카테고리와_시작일시_종료일시를_전달하면_그_사이에_해당하는_일정을_조회한다() {
        // given
        Member 관리자 = 관리자();
        memberRepository.save(관리자);

        Category BE_일정 = BE_일정(관리자);
        categoryRepository.save(BE_일정);

        Category FE_일정 = FE_일정(관리자);
        categoryRepository.save(FE_일정);

        Category 매트_아고라 = 매트_아고라(관리자);
        categoryRepository.save(매트_아고라);

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        Schedule 레벨_인터뷰 = new Schedule(FE_일정, 레벨_인터뷰_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 레벨_인터뷰_메모);

        Schedule 매고라 = new Schedule(매트_아고라, 매고라_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 매고라_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);
        scheduleRepository.save(레벨_인터뷰);
        scheduleRepository.save(매고라);

        // when
        List<Schedule> schedules = scheduleRepository.findByCategoryIdAndBetween(BE_일정, 날짜_2022년_7월_1일_0시_0분,
                날짜_2022년_7월_31일_0시_0분);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("카테고리와 시작일시, 종료일시를 전달할 때 일정의 시작날짜가 시작일시와 같으면 조회된다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_시작날짜가_시작일시와_같으면_조회된다() {
        // given
        Member 관리자 = 관리자();
        memberRepository.save(관리자);

        Category BE_일정 = BE_일정(관리자);
        categoryRepository.save(BE_일정);

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);

        // when
        List<Schedule> schedules = scheduleRepository.findByCategoryIdAndBetween(BE_일정, 날짜_2022년_7월_15일_16시_0분,
                날짜_2022년_7월_31일_0시_0분);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("카테고리와 시작일시, 종료일시를 전달할 때 일정의 시작날짜가 종료일시와 같으면 조회된다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_시작날짜가_종료일시와_같으면_조회된다() {
        // given
        Member 관리자 = 관리자();
        memberRepository.save(관리자);

        Category BE_일정 = BE_일정(관리자);
        categoryRepository.save(BE_일정);

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);

        // when
        List<Schedule> schedules = scheduleRepository.findByCategoryIdAndBetween(BE_일정, 날짜_2022년_7월_1일_0시_0분,
                날짜_2022년_7월_15일_16시_0분);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("카테고리와 시작일시, 종료일시를 전달할 때 일정의 시작날짜가 종료일시 이후이면 조회되지 않는다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_시작날짜가_종료일시_이후이면_조회되지_않는다() {
        // given
        Member 관리자 = 관리자();
        memberRepository.save(관리자);

        Category BE_일정 = BE_일정(관리자);
        categoryRepository.save(BE_일정);

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);

        // when
        List<Schedule> schedules = scheduleRepository.findByCategoryIdAndBetween(BE_일정, 날짜_2022년_7월_1일_0시_0분,
                날짜_2022년_7월_7일_16시_0분);

        // then
        assertThat(schedules).hasSize(0);
    }

    @DisplayName("카테고리와 시작일시, 종료일시를 전달할 때 일정의 종료날짜가 시작일시와 같으면 조회된다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_종료날짜가_시작일시와_같으면_조회된다() {
        // given
        Member 관리자 = 관리자();
        memberRepository.save(관리자);

        Category BE_일정 = BE_일정(관리자);
        categoryRepository.save(BE_일정);

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);

        // when
        List<Schedule> schedules = scheduleRepository.findByCategoryIdAndBetween(BE_일정, 날짜_2022년_7월_16일_16시_0분,
                날짜_2022년_7월_31일_0시_0분);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("카테고리와 시작일시, 종료일시를 전달할 때 일정의 종료날짜가 시작일시 이전이면 조회되지 않는다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_종료날짜가_시작일시_이전이면_조회되지_않는다() {
        // given
        Member 관리자 = 관리자();
        memberRepository.save(관리자);

        Category BE_일정 = BE_일정(관리자);
        categoryRepository.save(BE_일정);

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);

        // when
        List<Schedule> schedules = scheduleRepository.findByCategoryIdAndBetween(BE_일정, 날짜_2022년_7월_16일_16시_1분,
                날짜_2022년_7월_31일_0시_0분);

        // then
        assertThat(schedules).hasSize(0);
    }
}

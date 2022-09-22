package com.allog.dallog.domain.schedule.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_10일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_10일_11시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_15일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_1분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_18시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_20시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_17일_23시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_1일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_20일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_20일_11시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_27일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_27일_11시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_31일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_7일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_14시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_17시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_23시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.매고라_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.매고라_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회식_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회식_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_제목;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.annotation.RepositoryTest;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import java.time.LocalDateTime;
import java.util.Collections;
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

    @DisplayName("특정 카테고리들에 속한 일정을 전부 삭제한다.")
    @Test
    void 특정_카테고리들에_속한_일정을_전부_삭제한다() {
        // given
        Member 관리자 = 관리자();
        memberRepository.save(관리자);

        Category BE_일정 = BE_일정(관리자);
        Category FE_일정 = FE_일정(관리자);
        Category 공통_일정 = 공통_일정(관리자);
        categoryRepository.save(BE_일정);
        categoryRepository.save(FE_일정);
        categoryRepository.save(공통_일정);

        Schedule 알록달록_회의_BE = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분,
                알록달록_회의_메모);
        Schedule 알록달록_회식_BE = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분,
                알록달록_회식_메모);
        Schedule 알록달록_회의_FE = new Schedule(FE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분,
                알록달록_회의_메모);
        Schedule 알록달록_회식_FE = new Schedule(FE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분,
                알록달록_회식_메모);
        Schedule 알록달록_회의_공통 = new Schedule(공통_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분,
                알록달록_회의_메모);
        Schedule 알록달록_회식_공통 = new Schedule(공통_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분,
                알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의_BE);
        scheduleRepository.save(알록달록_회식_BE);
        scheduleRepository.save(알록달록_회의_FE);
        scheduleRepository.save(알록달록_회식_FE);
        scheduleRepository.save(알록달록_회의_공통);
        scheduleRepository.save(알록달록_회식_공통);

        // when
        scheduleRepository.deleteByCategoryIdIn(List.of(BE_일정.getId(), FE_일정.getId(), 공통_일정.getId()));

        // then
        assertThat(scheduleRepository.findAll()).hasSize(0);
    }

    @DisplayName("카테코리와 시작일시, 종료일시를 전달하면 그 사이에 해당하는 일정을 조회한다.")
    @Test
    void 카테고리와_시작일시_종료일시를_전달하면_그_사이에_해당하는_일정을_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        Category BE_일정 = categoryRepository.save(BE_일정(관리자));

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);

        // when
        List<IntegrationSchedule> actual = scheduleRepository.getByCategoriesAndBetween(List.of(BE_일정),
                날짜_2022년_7월_1일_0시_0분, 날짜_2022년_7월_31일_0시_0분);

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("조회하기 위한 category 리스트의 크기가 0인 경우 빈 리스트를 반환한다.")
    @Test
    void 조회하기_위한_category_리스트의_크기가_0인_경우_빈_리스트를_반환한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        Category BE_일정 = categoryRepository.save(BE_일정(관리자));

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);

        List<Category> categories = Collections.emptyList();
        LocalDateTime startDate = 날짜_2022년_7월_1일_0시_0분;
        LocalDateTime endDate = 날짜_2022년_7월_31일_0시_0분;

        // when
        List<IntegrationSchedule> actual = scheduleRepository.getByCategoriesAndBetween(categories, startDate, endDate);

        // then
        assertThat(actual).hasSize(0);
    }

    @DisplayName("카테고리가 여러 개 일 때, 카테고리와 시작일시, 종료일시를 전달하면 그 사이에 해당하는 일정을 조회한다.")
    @Test
    void 카테고리가_여러_개_일_때_카테고리와_시작일시_종료일시를_전달하면_그_사이에_해당하는_일정을_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        Category BE_일정 = categoryRepository.save(BE_일정(관리자));
        Category FE_일정 = categoryRepository.save(FE_일정(관리자));
        Category 매트_아고라 = categoryRepository.save(매트_아고라(관리자));

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        Schedule 레벨_인터뷰 = new Schedule(FE_일정, 레벨_인터뷰_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 레벨_인터뷰_메모);

        Schedule 매고라 = new Schedule(매트_아고라, 매고라_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 매고라_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);
        scheduleRepository.save(레벨_인터뷰);
        scheduleRepository.save(매고라);

        List<Category> categories = List.of(BE_일정);
        LocalDateTime startDate = 날짜_2022년_7월_1일_0시_0분;
        LocalDateTime endDate = 날짜_2022년_7월_31일_0시_0분;

        // when
        List<IntegrationSchedule> actual = scheduleRepository.getByCategoriesAndBetween(categories, startDate,
                endDate);

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("카테고리가 여러 개 일 때, 카테고리 리스트와 시작일시, 종료일시를 전달하면 그 사이에 해당하는 일정을 조회한다.")
    @Test
    void 카테고리가_여러_개_일_때_카테고리_리스트와_시작일시_종료일시를_전달하면_그_사이에_해당하는_일정을_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        Category BE_일정 = categoryRepository.save(BE_일정(관리자));
        Category FE_일정 = categoryRepository.save(FE_일정(관리자));
        Category 매트_아고라 = categoryRepository.save(매트_아고라(관리자));

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        Schedule 레벨_인터뷰 = new Schedule(FE_일정, 레벨_인터뷰_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 레벨_인터뷰_메모);

        Schedule 매고라 = new Schedule(매트_아고라, 매고라_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 매고라_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);
        scheduleRepository.save(레벨_인터뷰);
        scheduleRepository.save(매고라);

        List<Category> categories = List.of(BE_일정, FE_일정, 매트_아고라);
        LocalDateTime startDate = 날짜_2022년_7월_1일_0시_0분;
        LocalDateTime endDate = 날짜_2022년_7월_31일_0시_0분;

        // when
        List<IntegrationSchedule> actual = scheduleRepository.getByCategoriesAndBetween(categories, startDate, endDate);

        // then
        assertThat(actual).hasSize(3);
    }

    @DisplayName("카테고리와 시작일시, 종료일시를 전달할 때 일정의 시작날짜가 종료일시와 같으면 조회한다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_시작일시와_같으면_조회된다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        Category BE_일정 = categoryRepository.save(BE_일정(관리자));

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);

        List<Category> categories = List.of(BE_일정);
        LocalDateTime startDate = 날짜_2022년_7월_1일_0시_0분;
        LocalDateTime endDate = 날짜_2022년_7월_15일_16시_0분;

        // when
        List<IntegrationSchedule> actual = scheduleRepository.getByCategoriesAndBetween(categories, startDate, endDate);

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("카테고리와 시작일시, 종료일시를 전달할 때 일정의 시작날짜가 종료일시 이후이면 조회되지 않는다.")
    @Test
    void 카테고리와_시작일시_종료일시를_전달할_때_일정의_시작날짜가_종료일시_이후이면_조회되지_않는다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        Category BE_일정 = categoryRepository.save(BE_일정(관리자));

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);

        List<Category> categories = List.of(BE_일정);
        LocalDateTime startDate = 날짜_2022년_7월_1일_0시_0분;
        LocalDateTime endDate = 날짜_2022년_7월_7일_16시_0분;

        // when
        List<IntegrationSchedule> actual = scheduleRepository.getByCategoriesAndBetween(categories, startDate, endDate);

        // then
        assertThat(actual).hasSize(0);
    }

    @DisplayName("카테고리와 시작일시, 종료일시를 전달할 때 일정의 종료날짜가 시작일시와 같으면 조회된다.")
    @Test
    void 카테고리와_시작일시와_종료일시를_전달할_때_일정의_종료날짜가_시작일시와_같으면_조회된다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        Category BE_일정 = categoryRepository.save(BE_일정(관리자));

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);

        List<Category> categories = List.of(BE_일정);
        LocalDateTime startDate = 날짜_2022년_7월_16일_16시_0분;
        LocalDateTime endDate = 날짜_2022년_7월_31일_0시_0분;

        // when
        List<IntegrationSchedule> actual = scheduleRepository.getByCategoriesAndBetween(categories, startDate, endDate);

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("카테고리와 시작일시, 종료일시를 전달할 때 일정의 종료날짜가 시작일시 이전이면 조회되지 않는다.")
    @Test
    void 카테고리와_시작일시와_종료일시를_전달할_때_일정의_종료날짜가_시작일시_이전이면_조회되지_않는다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        Category BE_일정 = categoryRepository.save(BE_일정(관리자));

        Schedule 알록달록_회의 = new Schedule(BE_일정, 알록달록_회의_제목, 날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분, 알록달록_회의_메모);
        Schedule 알록달록_회식 = new Schedule(BE_일정, 알록달록_회식_제목, 날짜_2022년_8월_15일_14시_0분, 날짜_2022년_8월_15일_17시_0분, 알록달록_회식_메모);

        scheduleRepository.save(알록달록_회의);
        scheduleRepository.save(알록달록_회식);

        List<Category> categories = List.of(BE_일정);
        LocalDateTime startDate = 날짜_2022년_7월_1일_0시_0분;
        LocalDateTime endDate = 날짜_2022년_7월_7일_16시_0분;

        // when
        List<IntegrationSchedule> actual = scheduleRepository.getByCategoriesAndBetween(categories, startDate, endDate);

        // then
        assertThat(actual).hasSize(0);
    }

    @DisplayName("시작일시와 종료일시로 특정 카테고리의 일정을 조회한다.")
    @Test
    void 시작일시와_종료일시로_특정_카테고리의_일정을_조회한다() {
        // given
        Member 후디 = memberRepository.save(후디());

        Category BE_일정 = categoryRepository.save(BE_일정(후디));
        Category FE_일정 = categoryRepository.save(FE_일정(후디));
        Category 공통_일정 = categoryRepository.save(공통_일정(후디));

        /* BE 일정 */
        scheduleRepository.save(new Schedule(BE_일정, "BE 1", 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_8월_15일_14시_0분, ""));
        scheduleRepository.save(new Schedule(BE_일정, "BE 2", 날짜_2022년_7월_10일_0시_0분, 날짜_2022년_7월_10일_11시_59분, ""));
        scheduleRepository.save(new Schedule(BE_일정, "BE 3", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_20시_0분, ""));

        /* FE 일정 */
        scheduleRepository.save(new Schedule(FE_일정, "FE 1", 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_7월_31일_0시_0분, ""));
        scheduleRepository.save(new Schedule(FE_일정, "FE 2", 날짜_2022년_7월_20일_0시_0분, 날짜_2022년_7월_20일_11시_59분, ""));
        scheduleRepository.save(new Schedule(FE_일정, "FE 3", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_18시_0분, ""));

        /* 공통 일정 */
        scheduleRepository.save(new Schedule(공통_일정, "공통 1", 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_7월_16일_16시_1분, ""));
        scheduleRepository.save(new Schedule(공통_일정, "공통 2", 날짜_2022년_7월_27일_0시_0분, 날짜_2022년_7월_27일_11시_59분, ""));
        scheduleRepository.save(new Schedule(공통_일정, "공통 3", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_16시_1분, ""));

        List<Category> categories = List.of(BE_일정, FE_일정);
        LocalDateTime startDate = 날짜_2022년_7월_1일_0시_0분;
        LocalDateTime endDate = 날짜_2022년_8월_15일_23시_59분;

        // when
        List<IntegrationSchedule> actual = scheduleRepository.getByCategoriesAndBetween(categories, startDate, endDate);

        // then
        assertThat(actual)
                .extracting(IntegrationSchedule::getTitle)
                .containsOnly("BE 1", "BE 2", "BE 3", "FE 1", "FE 2", "FE 3");
    }

    @DisplayName("시작일시와 종료일시로 특정 카테고리의 일정을 조회할 때 범위 밖의 일정은 제외된다.")
    @Test
    void 시작일시와_종료일시로_특정_카테고리의_일정을_조회할_때_범위_밖의_일정은_제외된다() {
        // given
        Member 후디 = memberRepository.save(후디());

        Category BE_일정 = categoryRepository.save(BE_일정(후디));
        Category FE_일정 = categoryRepository.save(FE_일정(후디));

        /* BE 일정 */
        scheduleRepository.save(new Schedule(BE_일정, "BE 1 포함", 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_8월_15일_14시_0분, ""));
        scheduleRepository.save(new Schedule(BE_일정, "BE 2 포함", 날짜_2022년_7월_10일_0시_0분, 날짜_2022년_7월_10일_11시_59분, ""));
        scheduleRepository.save(new Schedule(BE_일정, "BE 3 포함", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_20시_0분, ""));
        scheduleRepository.save(new Schedule(BE_일정, "BE 3 미포함", 날짜_2022년_7월_31일_0시_0분, 날짜_2022년_8월_15일_17시_0분, ""));

        /* FE 일정 */
        scheduleRepository.save(new Schedule(FE_일정, "FE 1 포함", 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_7월_31일_0시_0분, ""));
        scheduleRepository.save(new Schedule(FE_일정, "FE 2 포함", 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_18시_0분, ""));
        scheduleRepository.save(new Schedule(FE_일정, "FE 3 미포함", 날짜_2022년_7월_20일_0시_0분, 날짜_2022년_7월_20일_11시_59분, ""));

        List<Category> categories = List.of(BE_일정, FE_일정);
        LocalDateTime startDateTime = 날짜_2022년_7월_1일_0시_0분;
        LocalDateTime endDateTime = 날짜_2022년_7월_17일_23시_59분;

        // when
        List<IntegrationSchedule> actual = scheduleRepository.getByCategoriesAndBetween(categories,
                startDateTime, endDateTime);

        // then
        assertThat(actual).extracting(IntegrationSchedule::getTitle)
                .containsOnly("BE 1 포함", "BE 2 포함", "BE 3 포함", "FE 1 포함", "FE 2 포함");
    }
}

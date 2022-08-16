package com.allog.dallog.domain.schedule.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_15일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_14시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_17시_0분;
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

    @DisplayName("특정 카테고리들에 속한 일정을 전부 삭제한다")
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
}

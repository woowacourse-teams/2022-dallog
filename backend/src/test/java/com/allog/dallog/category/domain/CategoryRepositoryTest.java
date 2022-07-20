package com.allog.dallog.category.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_NUMBER_0;
import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_NUMBER_1;
import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_SIZE_2;
import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_SIZE_8;
import static com.allog.dallog.common.fixtures.MemberFixtures.CREATOR;
import static com.allog.dallog.common.fixtures.MemberFixtures.CREATOR2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.global.config.JpaConfig;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@DataJpaTest
@Import(JpaConfig.class)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("페이지와 사이즈를 받아 해당하는 구간의 카테고리를 조회한다.")
    @Test
    void 페이지와_사이즈를_받아_해당하는_구간의_카테고리를_조회한다() {
        // given
        Member creator = memberRepository.save(CREATOR);
        categoryRepository.save(new Category("BE 공식일정", creator));
        categoryRepository.save(new Category("FE 공식일정", creator));
        categoryRepository.save(new Category("알록달록 회의", creator));
        categoryRepository.save(new Category("지원플랫폼 근로", creator));
        categoryRepository.save(new Category("파랑의 코틀린 스터디", creator));

        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER_1, PAGE_SIZE_2);

        // when
        Slice<Category> categories = categoryRepository.findSliceBy(pageRequest);

        // then
        assertAll(() -> {
            assertThat(categories.getContent()).hasSize(PAGE_SIZE_2).extracting(Category::getName)
                    .contains("알록달록 회의", "지원플랫폼 근로");
            assertThat(
                    categories.getContent().stream().map(Category::getCreatedAt).allMatch(Objects::nonNull)).isTrue();
        });
    }

    @DisplayName("조회 시 데이터가 존재하지 않는 경우 빈 슬라이스가 반환된다.")
    @Test
    void 조회_시_데이터가_존재하지_않는_경우_빈_슬라이스가_반환된다() {
        // given
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER_1, PAGE_SIZE_2);

        // when
        Slice<Category> categories = categoryRepository.findSliceBy(pageRequest);

        // then
        assertThat(categories).hasSize(0);
    }

    @DisplayName("특정 멤버가 생성한 카테고리를 페이징을 통해 조회한다.")
    @Test
    void 특정_멤버가_생성한_카테고리를_페이징을_통해_조회한다() {
        // given
        Member creator = memberRepository.save(CREATOR);
        Member creator2 = memberRepository.save(CREATOR2);
        categoryRepository.save(new Category("BE 공식일정", creator));
        categoryRepository.save(new Category("FE 공식일정", creator));
        categoryRepository.save(new Category("알록달록 회의", creator));
        categoryRepository.save(new Category("지원플랫폼 근로", creator2));
        categoryRepository.save(new Category("파랑의 코틀린 스터디", creator2));

        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_8);

        // when
        Slice<Category> categories = categoryRepository.findSliceByMemberId(pageRequest, creator.getId());

        // then
        assertAll(() -> {
            assertThat(categories.getContent()).hasSize(3).extracting(Category::getName)
                    .containsExactlyInAnyOrder("BE 공식일정", "FE 공식일정", "알록달록 회의");
            assertThat(
                    categories.getContent().stream().map(Category::getCreatedAt).allMatch(Objects::nonNull)).isTrue();
        });
    }

}

package com.allog.dallog.category.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("페이지와 사이즈를 받아 해당하는 구간의 카테고리를 조회한다.")
    @Test
    void 페이지와_사이즈를_받아_해당하는_구간의_카테고리를_조회한다() {
        // given
        categoryRepository.save(new Category("BE 공식일정"));
        categoryRepository.save(new Category("FE 공식일정"));
        categoryRepository.save(new Category("알록달록 회의"));
        categoryRepository.save(new Category("지원플랫폼 근로"));
        categoryRepository.save(new Category("파랑의 코틀린 스터디"));

        int page = 1;
        int size = 2;
        PageRequest pageRequest = PageRequest.of(page, size);

        // when
        Slice<Category> categories = categoryRepository.findSliceBy(pageRequest);

        // then
        assertAll(
                () -> assertThat(categories.getContent())
                        .hasSize(size)
                        .extracting(Category::getName)
                        .contains("알록달록 회의", "지원플랫폼 근로"),
                () -> assertThat(categories.getContent().stream()
                        .map(Category::getCreatedAt)
                        .allMatch(Objects::nonNull))
                        .isTrue()
        );
    }

    @DisplayName("조회 시 데이터가 존재하지 않는 경우 빈 슬라이스가 반환된다.")
    @Test
    void 조회_시_데이터가_존재하지_않는_경우_빈_슬라이스가_반환된다() {
        // given
        int page = 1;
        int size = 2;
        PageRequest pageRequest = PageRequest.of(page, size);

        // when
        Slice<Category> categories = categoryRepository.findSliceBy(pageRequest);

        // then
        assertThat(categories).hasSize(0);
    }
}

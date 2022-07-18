package com.allog.dallog.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.category.exception.InvalidCategoryException;
import com.allog.dallog.global.dto.FindByPageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @DisplayName("새로운 카테고리를 생성한다.")
    @Test
    void 새로운_카테고리를_생성한다() {
        // given
        String name = "BE 공식일정";
        CategoryCreateRequest request = new CategoryCreateRequest(name);

        // when
        Long id = categoryService.save(request);

        // then
        assertThat(id).isNotNull();
    }

    @DisplayName("새로운 카테고리를 생성 할 떄 이름이 공백이거나 길이가 20을 초과하는 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "일이삼사오육칠팔구십일이삼사오육칠팔구십일", "알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 카테고리"})
    void 새로운_카테고리를_생성_할_때_이름이_공백이거나_길이가_20을_초과하는_경우_예외를_던진다(final String name) {
        // given
        CategoryCreateRequest request = new CategoryCreateRequest(name);

        // when & then
        assertThatThrownBy(() -> categoryService.save(request))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @DisplayName("페이지를 받아 해당하는 구간의 카테고리를 가져온다.")
    @Test
    void 페이지를_받아_해당하는_구간의_카테고리를_가져온다() {
        // given
        categoryService.save(new CategoryCreateRequest("BE 공식일정"));
        categoryService.save(new CategoryCreateRequest("FE 공식일정"));
        categoryService.save(new CategoryCreateRequest("알록달록 회의"));
        categoryService.save(new CategoryCreateRequest("지원플랫폼 근로"));
        categoryService.save(new CategoryCreateRequest("파랑의 코틀린 스터디"));

        int page = 2;
        int size = 2;
        PageRequest request = PageRequest.of(page, size);

        // when
        FindByPageResponse<CategoryResponse> response = categoryService.findAll(request);

        // then
        assertAll(() -> {
            assertThat(response.getData())
                    .hasSize(size)
                    .extracting(CategoryResponse::getName)
                    .contains("알록달록 회의", "지원플랫폼 근로");
            assertThat(response.getPage()).isEqualTo(page);
        });
    }
}

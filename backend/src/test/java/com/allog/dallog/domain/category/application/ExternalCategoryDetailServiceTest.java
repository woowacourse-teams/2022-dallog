package com.allog.dallog.domain.category.application;

import static com.allog.dallog.common.fixtures.CategoryFixtures.외부_BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.외부_FE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.OAuthFixtures.리버;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExternalCategoryDetailServiceTest extends ServiceTest {

    @Autowired
    private ExternalCategoryDetailService externalCategoryDetailService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExternalCategoryDetailRepository externalCategoryDetailRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("월별 일정 조회 시, 회원 ID로 해당하는 외부 연동 카테고리 전체를 조회한다.")
    @Test
    void 월별_일정_조회_시_회원_ID로_해당하는_외부_연동_카테고리의_전체를_조회한다() {
        // given
        Long 리버_id = toMemberId(리버.getOAuthMember());

        CategoryResponse 외부_BE_일정_응답 = categoryService.save(리버_id, 외부_BE_일정_생성_요청);
        Category 외부_BE_일정 = categoryRepository.getById(외부_BE_일정_응답.getId());

        CategoryResponse 외부_FE_일정_응답 = categoryService.save(리버_id, 외부_FE_일정_생성_요청);
        Category 외부_FE_일정 = categoryRepository.getById(외부_FE_일정_응답.getId());

        externalCategoryDetailRepository.save(new ExternalCategoryDetail(외부_BE_일정, "1111111"));
        externalCategoryDetailRepository.save(new ExternalCategoryDetail(외부_FE_일정, "2222222"));

        // when
        List<ExternalCategoryDetail> details = externalCategoryDetailService.findByMemberId(리버_id);

        // then
        assertThat(details).hasSize(2);
    }
}

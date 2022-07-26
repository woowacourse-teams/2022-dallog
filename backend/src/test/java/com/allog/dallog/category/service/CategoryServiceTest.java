package com.allog.dallog.category.service;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.관리자;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.auth.exception.NoPermissionException;
import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.category.dto.response.CategoriesResponse;
import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.category.exception.InvalidCategoryException;
import com.allog.dallog.category.exception.NoSuchCategoryException;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("새로운 카테고리를 생성한다.")
    @Test
    void 새로운_카테고리를_생성한다() {
        // given
        Member creator = memberRepository.save(관리자());

        // when
        CategoryResponse response = categoryService.save(creator.getId(), 공통_일정_생성_요청);

        // then
        assertThat(response.getName()).isEqualTo(공통_일정_이름);
    }

    @DisplayName("새로운 카테고리를 생성 할 떄 이름이 공백이거나 길이가 20을 초과하는 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "일이삼사오육칠팔구십일이삼사오육칠팔구십일", "알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 카테고리"})
    void 새로운_카테고리를_생성_할_때_이름이_공백이거나_길이가_20을_초과하는_경우_예외를_던진다(final String name) {
        // given
        CategoryCreateRequest request = new CategoryCreateRequest(name);
        Member creator = memberRepository.save(관리자());

        // when & then
        assertThatThrownBy(() -> categoryService.save(creator.getId(), request))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @DisplayName("페이지를 받아 해당하는 구간의 카테고리를 가져온다.")
    @Test
    void 페이지를_받아_해당하는_구간의_카테고리를_가져온다() {
        // given
        Member creator = memberRepository.save(관리자());
        Long creatorId = creator.getId();
        categoryService.save(creatorId, 공통_일정_생성_요청);
        categoryService.save(creatorId, BE_일정_생성_요청);
        categoryService.save(creatorId, FE_일정_생성_요청);
        categoryService.save(creatorId, 매트_아고라_생성_요청);
        categoryService.save(creatorId, 후디_JPA_스터디_생성_요청);

        PageRequest request = PageRequest.of(1, 2);

        // when
        CategoriesResponse response = categoryService.findAll(request);

        // then
        assertThat(response.getCategories())
                .hasSize(2)
                .extracting(CategoryResponse::getName)
                .contains(FE_일정_이름, 매트_아고라_이름);
    }

    @DisplayName("회원 id와 페이지를 기반으로 카테고리를 가져온다.")
    @Test
    void 회원_id와_페이지를_기반으로_카테고리를_가져온다() {
        // given
        Member creator = memberRepository.save(관리자());
        Long creatorId = creator.getId();
        categoryService.save(creatorId, 공통_일정_생성_요청);
        categoryService.save(creatorId, BE_일정_생성_요청);
        categoryService.save(creatorId, FE_일정_생성_요청);
        categoryService.save(creatorId, 매트_아고라_생성_요청);
        categoryService.save(creatorId, 후디_JPA_스터디_생성_요청);

        PageRequest request = PageRequest.of(1, 2);

        // when
        CategoriesResponse response = categoryService.findMine(creatorId, request);

        // then
        assertThat(response.getCategories())
                .hasSize(2)
                .extracting(CategoryResponse::getName)
                .contains(FE_일정_이름, 매트_아고라_이름);
    }

    @DisplayName("id를 통해 카테고리를 단건 조회한다.")
    @Test
    void id를_통해_카테고리를_단건_조회한다() {
        // given
        Member creator = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(creator.getId(), 공통_일정_생성_요청);

        // when & then
        assertThat(categoryService.findById(공통_일정.getId()))
                .usingRecursiveComparison()
                .isEqualTo(공통_일정);
    }

    @DisplayName("회원과 카테고리 id를 통해 카테고리를 수정한다.")
    @Test
    void 회원과_카테고리_id를_통해_카테고리를_수정한다() {
        // given
        Member creator = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(creator.getId(), 공통_일정_생성_요청);

        String 우테코_공통_일정_이름 = "우테코 공통 일정";
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(우테코_공통_일정_이름);

        // when
        categoryService.update(creator.getId(), 공통_일정.getId(), categoryUpdateRequest);
        Category category = categoryService.getCategory(공통_일정.getId());

        //then
        assertThat(category.getName()).isEqualTo(우테코_공통_일정_이름);
    }

    @DisplayName("자신이 만들지 않은 카테고리를 수정할 경우 예외를 던진다.")
    @Test
    void 자신이_만들지_않은_카테고리를_수정할_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Member 매트 = memberRepository.save(매트());

        CategoryResponse savedCategory = categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest("우테코 공통 일정");

        // when & then
        assertThatThrownBy(() -> categoryService.update(매트.getId(), savedCategory.getId(), categoryUpdateRequest))
                .isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("회원과 카테고리 id를 통해 카테고리를 삭제한다.")
    @Test
    void 회원과_카테고리_id를_통해_카테고리를_삭제한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse savedCategory = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // when
        categoryService.delete(관리자.getId(), savedCategory.getId());

        //then
        assertThatThrownBy(() -> categoryService.getCategory(savedCategory.getId()))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("자신이 만들지 않은 카테고리를 삭제할 경우 예외를 던진다.")
    @Test
    void 자신이_만들지_않은_카테고리를_삭제할_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Member 매트 = memberRepository.save(매트());
        CategoryResponse savedCategory = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // when & then
        assertThatThrownBy(
                () -> categoryService.delete(매트.getId(), savedCategory.getId()))
                .isInstanceOf(NoPermissionException.class);
    }
}

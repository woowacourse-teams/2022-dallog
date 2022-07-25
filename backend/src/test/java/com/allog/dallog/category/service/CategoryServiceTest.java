package com.allog.dallog.category.service;

import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_NAME;
import static com.allog.dallog.common.fixtures.CategoryFixtures.MODIFIED_CATEGORY_NAME;
import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_NUMBER_1;
import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_SIZE_2;
import static com.allog.dallog.common.fixtures.MemberFixtures.CREATOR;
import static com.allog.dallog.common.fixtures.MemberFixtures.DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.MemberFixtures.EMAIL;
import static com.allog.dallog.common.fixtures.MemberFixtures.PROFILE_IMAGE_URI;
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
import com.allog.dallog.member.domain.SocialType;
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
        CategoryCreateRequest request = new CategoryCreateRequest(CATEGORY_NAME);
        Member creator = memberRepository.save(CREATOR);

        // when
        CategoryResponse response = categoryService.save(creator.getId(), request);

        // then
        assertThat(response.getName()).isEqualTo(CATEGORY_NAME);
    }

    @DisplayName("새로운 카테고리를 생성 할 떄 이름이 공백이거나 길이가 20을 초과하는 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "일이삼사오육칠팔구십일이삼사오육칠팔구십일", "알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 카테고리"})
    void 새로운_카테고리를_생성_할_때_이름이_공백이거나_길이가_20을_초과하는_경우_예외를_던진다(final String name) {
        // given
        CategoryCreateRequest request = new CategoryCreateRequest(name);
        Member creator = memberRepository.save(CREATOR);

        // when & then
        assertThatThrownBy(() -> categoryService.save(creator.getId(), request))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @DisplayName("페이지를 받아 해당하는 구간의 카테고리를 가져온다.")
    @Test
    void 페이지를_받아_해당하는_구간의_카테고리를_가져온다() {
        // given
        Member creator = memberRepository.save(CREATOR);
        Long creatorId = creator.getId();
        categoryService.save(creatorId, new CategoryCreateRequest("BE 공식일정"));
        categoryService.save(creatorId, new CategoryCreateRequest("FE 공식일정"));
        categoryService.save(creatorId, new CategoryCreateRequest("알록달록 회의"));
        categoryService.save(creatorId, new CategoryCreateRequest("지원플랫폼 근로"));
        categoryService.save(creatorId, new CategoryCreateRequest("파랑의 코틀린 스터디"));

        PageRequest request = PageRequest.of(PAGE_NUMBER_1, PAGE_SIZE_2);

        // when
        CategoriesResponse response = categoryService.findAll(request);

        // then
        assertThat(response.getCategories())
                .hasSize(PAGE_SIZE_2)
                .extracting(CategoryResponse::getName)
                .contains("알록달록 회의", "지원플랫폼 근로");
    }

    @DisplayName("회원 id와 페이지를 기반으로 카테고리를 가져온다.")
    @Test
    void 회원_id와_페이지를_기반으로_카테고리를_가져온다() {
        // given
        Member creator = memberRepository.save(CREATOR);
        Long creatorId = creator.getId();
        categoryService.save(creatorId, new CategoryCreateRequest("BE 공식일정"));
        categoryService.save(creatorId, new CategoryCreateRequest("FE 공식일정"));
        categoryService.save(creatorId, new CategoryCreateRequest("알록달록 회의"));
        categoryService.save(creatorId, new CategoryCreateRequest("지원플랫폼 근로"));
        categoryService.save(creatorId, new CategoryCreateRequest("파랑의 코틀린 스터디"));

        PageRequest request = PageRequest.of(PAGE_NUMBER_1, PAGE_SIZE_2);

        // when
        CategoriesResponse response = categoryService.findMine(creatorId, request);

        // then
        assertThat(response.getCategories())
                .hasSize(PAGE_SIZE_2)
                .extracting(CategoryResponse::getName)
                .contains("알록달록 회의", "지원플랫폼 근로");
    }

    @DisplayName("id를 통해 카테고리를 단건 조회한다.")
    @Test
    void id를_통해_카테고리를_단건_조회한다() {
        // given
        Member creator = memberRepository.save(CREATOR);
        categoryService.save(creator.getId(), new CategoryCreateRequest("BE 공식일정"));
        CategoryResponse savedCategory = categoryService.save(creator.getId(), new CategoryCreateRequest("FE 공식일정"));

        // when & then
        assertThat(categoryService.findById(savedCategory.getId()))
                .usingRecursiveComparison()
                .isEqualTo(savedCategory);
    }

    @DisplayName("회원과 카테고리 id를 통해 카테고리를 수정한다.")
    @Test
    void 회원과_카테고리_id를_통해_카테고리를_수정한다() {
        // given
        Member creator = memberRepository.save(CREATOR);
        CategoryResponse savedCategory = categoryService.save(creator.getId(),
                new CategoryCreateRequest(CATEGORY_NAME));

        // when
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(MODIFIED_CATEGORY_NAME);
        categoryService.update(creator.getId(), savedCategory.getId(), categoryUpdateRequest);

        //then
        Category category = categoryService.getCategory(savedCategory.getId());
        assertThat(category.getName())
                .isEqualTo(MODIFIED_CATEGORY_NAME);
    }

    @DisplayName("자신이 만들지 않은 카테고리를 수정할 경우 예외를 던진다.")
    @Test
    void 자신이_만들지_않은_카테고리를_수정할_경우_예외를_던진다() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE));
        Member creator = memberRepository.save(
                new Member("creator@email.com", "/image.png", "creator", SocialType.GOOGLE));
        CategoryResponse savedCategory = categoryService.save(creator.getId(),
                new CategoryCreateRequest(CATEGORY_NAME));

        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(MODIFIED_CATEGORY_NAME);

        // when & then
        assertThatThrownBy(
                () -> categoryService.update(member.getId(), savedCategory.getId(), categoryUpdateRequest))
                .isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("회원과 카테고리 id를 통해 카테고리를 삭제한다.")
    @Test
    void 회원과_카테고리_id를_통해_카테고리를_삭제한다() {
        // given
        Member creator = memberRepository.save(CREATOR);
        CategoryResponse savedCategory = categoryService.save(creator.getId(),
                new CategoryCreateRequest(CATEGORY_NAME));

        // when
        categoryService.delete(creator.getId(), savedCategory.getId());

        //then
        assertThatThrownBy(() -> categoryService.getCategory(savedCategory.getId()))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("자신이 만들지 않은 카테고리를 삭제할 경우 예외를 던진다.")
    @Test
    void 자신이_만들지_않은_카테고리를_삭제할_경우_예외를_던진다() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE));
        Member creator = memberRepository.save(
                new Member("creator@email.com", "/image.png", "creator", SocialType.GOOGLE));
        CategoryResponse savedCategory = categoryService.save(creator.getId(), new CategoryCreateRequest("FE 공식일정"));

        // when & then
        assertThatThrownBy(
                () -> categoryService.delete(member.getId(), savedCategory.getId()))
                .isInstanceOf(NoPermissionException.class);
    }
}

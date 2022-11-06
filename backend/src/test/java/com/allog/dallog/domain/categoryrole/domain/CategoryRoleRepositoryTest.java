package com.allog.dallog.domain.categoryrole.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.annotation.IntegrationTest;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryRoleRepositoryTest extends IntegrationTest {

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("member id와 category id를 기반으로 조회한다.")
    @Test
    void member_id와_category_id를_기반으로_조회한다() {
        // given
        Member 매트 = memberRepository.save(매트());
        Category BE_일정 = categoryRepository.save(BE_일정(매트));

        CategoryRole savedCategoryRoleType = categoryRoleRepository.save(
                new CategoryRole(BE_일정, 매트, CategoryRoleType.ADMIN));

        // when
        CategoryRole actual = categoryRoleRepository.getByMemberIdAndCategoryId(매트.getId(), BE_일정.getId());

        // then
        assertThat(actual).isEqualTo(savedCategoryRoleType);
    }

    @DisplayName("category id를 기반으로 조회한다.")
    @Test
    void category_id를_기반으로_조회한다() {
        // given
        Member 매트 = memberRepository.save(매트());
        Category BE_일정 = categoryRepository.save(BE_일정(매트));
        categoryRoleRepository.save(new CategoryRole(BE_일정, 매트, CategoryRoleType.ADMIN));

        // when
        List<CategoryRole> actual = categoryRoleRepository.findByMemberId(매트.getId());

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("특정 카테고리에 admin이 혼자인지 확인한다.")
    @Test
    void 특정_카테고리에_admin이_혼자인지_확인한다() {
        // given
        Member 매트 = memberRepository.save(매트());
        Category BE_일정 = categoryRepository.save(BE_일정(매트));
        categoryRoleRepository.save(new CategoryRole(BE_일정, 매트, CategoryRoleType.ADMIN));

        // when
        boolean actual = categoryRoleRepository.isMemberSoleAdminInCategory(매트.getId(), BE_일정.getId());

        // then
        assertThat(actual).isTrue();
    }
}

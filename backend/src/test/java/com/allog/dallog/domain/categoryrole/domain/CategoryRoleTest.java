package com.allog.dallog.domain.categoryrole.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CategoryRoleTest {

    @DisplayName("역할이 특정 권한을 가지고 있는지 확인한다.")
    @CsvSource(value = {"ADMIN,UPDATE_CATEGORY,true", "NONE,UPDATE_CATEGORY,false"})
    @ParameterizedTest
    void 역할이_특정_권한을_가지고_있는지_확인한다(final CategoryRoleType roleType, final CategoryAuthority authority,
                                 final boolean expected) {
        // given
        Category BE_일정 = BE_일정(관리자());
        Member 후디 = 후디();
        CategoryRole categoryRole = new CategoryRole(BE_일정, 후디, roleType);

        // when
        boolean actual = categoryRole.ableTo(authority);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}

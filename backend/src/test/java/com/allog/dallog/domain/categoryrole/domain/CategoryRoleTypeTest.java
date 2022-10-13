package com.allog.dallog.domain.categoryrole.domain;

import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.ADD_SCHEDULE;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.UPDATE_SCHEDULE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CategoryRoleTypeTest {

    @DisplayName("역할 유형이 권한을 가지고 있는지 확인한다.")
    @CsvSource(value = {"ADMIN,UPDATE_CATEGORY,true", "NONE,UPDATE_CATEGORY,false"})
    @ParameterizedTest
    void 역할_유형이_권한을_가지고_있는지_확인한다(final CategoryRoleType roleType, final CategoryAuthority authority,
                                 final boolean expected) {
        // given & when
        boolean actual = roleType.ableTo(authority);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("주어진 권한 목록을 모두 가지고 있는 역할 유형 목록을 가져온다.")
    @Test
    void 주어진_권한_목록을_가지고_있는_역할_유형_목록을_가져온다() {
        // given, when
        Set<CategoryRoleType> actual = CategoryRoleType.getHavingAuthorities(Set.of(ADD_SCHEDULE, UPDATE_SCHEDULE));

        // then
        assertThat(actual).containsExactly(CategoryRoleType.ADMIN);
    }
}

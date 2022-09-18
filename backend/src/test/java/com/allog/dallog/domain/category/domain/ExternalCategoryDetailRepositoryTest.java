package com.allog.dallog.domain.category.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.우아한테크코스_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.common.annotation.RepositoryTest;
import com.allog.dallog.domain.category.exception.ExistExternalCategoryException;
import com.allog.dallog.domain.category.exception.NoSuchExternalCategoryDetailException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ExternalCategoryDetailRepositoryTest extends RepositoryTest {

    @Autowired
    private ExternalCategoryDetailRepository externalCategoryDetailRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("존재하지 않는 외부 카테고리 세부정보를 가져오는 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_외부_카테고리_세부정보를_가져오는_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        Category 우아한테크코스_일정 = 우아한테크코스_일정(관리자);
        categoryRepository.save(우아한테크코스_일정);

        externalCategoryDetailRepository.save(new ExternalCategoryDetail(우아한테크코스_일정, "externalId"));

        // when & then
        assertThatThrownBy(() -> externalCategoryDetailRepository.getByCategoryId(0L))
                .isInstanceOf(NoSuchExternalCategoryDetailException.class);
    }

    @DisplayName("새로운 외부 카테고리 세부정보인 경우 예외를 던지지 않는다.")
    @Test
    void 새로운_외부_카테고리_세부정보인_경우_예외를_던지지_않는다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        Category 우아한테크코스_일정 = 우아한테크코스_일정(관리자);
        categoryRepository.save(우아한테크코스_일정);

        String externalId = "externalId";
        externalCategoryDetailRepository.save(new ExternalCategoryDetail(우아한테크코스_일정, externalId));

        // when & then
        assertDoesNotThrow(() -> externalCategoryDetailRepository
                .validateExistByExternalIdAndCategoryIn(externalId, List.of()));
    }

    @DisplayName("이미 존재하는 외부 카테고리 세부정보인 경우 예외를 던진다.")
    @Test
    void 이미_존재하는_외부_카테고리_세부정보인_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        Category 우아한테크코스_일정 = 우아한테크코스_일정(관리자);
        categoryRepository.save(우아한테크코스_일정);

        String externalId = "externalId";
        externalCategoryDetailRepository.save(new ExternalCategoryDetail(우아한테크코스_일정, externalId));

        // when & then
        assertThatThrownBy(() -> externalCategoryDetailRepository
                .validateExistByExternalIdAndCategoryIn(externalId, List.of(우아한테크코스_일정)))
                .isInstanceOf(ExistExternalCategoryException.class);
    }
}

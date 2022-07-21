package com.allog.dallog.category.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_NUMBER_0;
import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_NUMBER_1;
import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_SIZE_2;
import static com.allog.dallog.common.fixtures.CategoryFixtures.PAGE_SIZE_8;
import static com.allog.dallog.common.fixtures.MemberFixtures.DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.MemberFixtures.DISPLAY_NAME2;
import static com.allog.dallog.common.fixtures.MemberFixtures.EMAIL;
import static com.allog.dallog.common.fixtures.MemberFixtures.EMAIL2;
import static com.allog.dallog.common.fixtures.MemberFixtures.PROFILE_IMAGE_URI;
import static com.allog.dallog.common.fixtures.MemberFixtures.PROFILE_IMAGE_URI2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.global.config.JpaConfig;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.member.domain.SocialType;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
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

    private Member neo;
    private Member pobi;

    private Category 백엔드_공식일정;
    private Category 프론트엔드_공식일정;
    private Category 알록달록_회의;
    private Category 지원플랫폼_근로;
    private Category 파랑의_코틀린_스터디;

    @BeforeEach
    void setUp() {
        neo = new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE);
        pobi = new Member(EMAIL2, PROFILE_IMAGE_URI2, DISPLAY_NAME2, SocialType.GOOGLE);
        memberRepository.save(neo);
        memberRepository.save(pobi);

        백엔드_공식일정 = new Category("BE 공식일정", neo);
        프론트엔드_공식일정 = new Category("FE 공식일정", neo);
        알록달록_회의 = new Category("알록달록 회의", neo);
        지원플랫폼_근로 = new Category("지원플랫폼 근로", pobi);
        파랑의_코틀린_스터디 = new Category("파랑의 코틀린 스터디", pobi);
    }

    @DisplayName("페이지와 사이즈를 받아 해당하는 구간의 카테고리를 조회한다.")
    @Test
    void 페이지와_사이즈를_받아_해당하는_구간의_카테고리를_조회한다() {
        // given
        categoryRepository.save(백엔드_공식일정);
        categoryRepository.save(프론트엔드_공식일정);
        categoryRepository.save(알록달록_회의);
        categoryRepository.save(지원플랫폼_근로);
        categoryRepository.save(파랑의_코틀린_스터디);

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

    @DisplayName("특정 멤버가 생성한 카테고리를 페이징을 통해 조회한다.")
    @Test
    void 특정_멤버가_생성한_카테고리를_페이징을_통해_조회한다() {
        // given
        categoryRepository.save(백엔드_공식일정);
        categoryRepository.save(프론트엔드_공식일정);
        categoryRepository.save(알록달록_회의);
        categoryRepository.save(지원플랫폼_근로);
        categoryRepository.save(파랑의_코틀린_스터디);

        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_8);

        // when
        Slice<Category> categories = categoryRepository.findSliceByMemberId(pageRequest, neo.getId());

        // then
        assertAll(() -> {
            assertThat(categories.getContent()).hasSize(3).extracting(Category::getName)
                    .containsExactlyInAnyOrder("BE 공식일정", "FE 공식일정", "알록달록 회의");
            assertThat(
                    categories.getContent().stream().map(Category::getCreatedAt).allMatch(Objects::nonNull)).isTrue();
        });
    }

}

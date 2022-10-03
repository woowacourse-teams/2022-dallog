package com.allog.dallog.domain.member.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.리버;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_이메일;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.색상1_구독;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.RepositoryTest;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @DisplayName("중복된 이메일이 존재하는 경우 true를 반환한다.")
    @Test
    void 중복된_이메일이_존재하는_경우_true를_반환한다() {
        // given
        memberRepository.save(파랑());

        // when & then
        assertThat(memberRepository.existsByEmail(파랑_이메일)).isTrue();
    }

    @DisplayName("이메일을 통해 회원을 찾는다.")
    @Test
    void 이메일을_통해_회원을_찾는다() {
        // given
        Member 파랑 = memberRepository.save(파랑());

        // when
        Member actual = memberRepository.getByEmail(파랑_이메일);

        // then
        assertThat(actual.getId()).isEqualTo(파랑.getId());
    }

    @DisplayName("존재하지 않는 email을 조회할 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_email을_조회할_경우_예외를_던진다() {
        // given
        String email = "dev.hyeonic@gmail.com";

        // given & when & then
        assertThatThrownBy(() -> memberRepository.getByEmail(email))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @DisplayName("존재하지 않는 id이면 예외를 던진다.")
    @Test
    void 존재하지_않는_id이면_예외를_던진다() {
        // given
        Long id = 0L;

        // when & then
        assertThatThrownBy(() -> memberRepository.validateExistsById(id))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @DisplayName("특정 카테고리의 구독자 목록을 반환한다.")
    @Test
    void 특정_카테고리의_구독자_목록을_반환한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Member 매트 = memberRepository.save(매트());
        Member 파랑 = memberRepository.save(파랑());
        memberRepository.save(리버());
        memberRepository.save(후디());

        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        subscriptionRepository.save(색상1_구독(매트, 공통_일정));
        subscriptionRepository.save(색상1_구독(파랑, 공통_일정));

        // when
        List<Member> actual = memberRepository.findSubscribers(공통_일정.getId());

        // then
        assertAll(() -> {
            assertThat(actual.size()).isEqualTo(2);
            assertThat(actual.stream().map(Member::getDisplayName).collect(Collectors.toList()))
                    .containsExactly(매트_이름, 파랑_이름);
        });
    }
}

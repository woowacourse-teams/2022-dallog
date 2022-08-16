package com.allog.dallog.domain.member.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_이메일;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.색상1_구독;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.annotation.RepositoryTest;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.util.Optional;
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

    @DisplayName("이메일을 통해 회원을 찾는다.")
    @Test
    void 이메일을_통해_회원을_찾는다() {
        // given
        Member 파랑 = memberRepository.save(파랑());

        // when
        Member actual = memberRepository.findByEmail(파랑_이메일).get();

        // then
        assertThat(actual.getId()).isEqualTo(파랑.getId());
    }

    @DisplayName("중복된 이메일이 존재하는 경우 true를 반환한다.")
    @Test
    void 중복된_이메일이_존재하는_경우_true를_반환한다() {
        // given
        memberRepository.save(파랑());

        // when & then
        assertThat(memberRepository.existsByEmail(파랑_이메일)).isTrue();
    }

    @DisplayName("구독 ID로 멤버를 찾는다.")
    @Test
    void 구독_ID로_멤버를_찾는다() {
        // given
        Member 후디 = 후디();
        memberRepository.save(후디);

        Category 공통_일정 = categoryRepository.save(공통_일정(후디));
        Subscription 구독 = subscriptionRepository.save(색상1_구독(후디, 공통_일정));

        // when
        Optional<Member> actual = memberRepository.findBySubscriptionId(구독.getId());

        // then
        assertThat(actual.get()).isEqualTo(후디);
    }
}

package com.allog.dallog.domain.subscription.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.노란색_구독;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.빨간색_구독;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.파란색_구독;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.global.config.JpaConfig;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
class SubscriptionRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @DisplayName("회원 정보를 기반으로 구독 정보를 조회한다.")
    @Test
    void 회원_정보를_기반으로_구독_정보를_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));
        Category BE_일정 = categoryRepository.save(BE_일정(관리자));
        Category FE_일정 = categoryRepository.save(FE_일정(관리자));

        Member 후디 = memberRepository.save(후디());
        Subscription 빨간색_구독 = 빨간색_구독(후디, 공통_일정);
        Subscription 파란색_구독 = 파란색_구독(후디, BE_일정);
        Subscription 노란색_구독 = 노란색_구독(후디, FE_일정);

        subscriptionRepository.save(빨간색_구독);
        subscriptionRepository.save(파란색_구독);
        subscriptionRepository.save(노란색_구독);

        // when
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(후디.getId());

        // then
        assertThat(subscriptions).hasSize(3);
    }

    @DisplayName("회원의 구독 정보가 존재하지 않는 경우 빈 리스트가 조회된다.")
    @Test
    void 회원의_구독_정보가_존재하지_않는_경우_빈_리스트가_조회된다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        // when
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(관리자.getId());

        // then
        assertThat(subscriptions).isEmpty();
    }

    @DisplayName("회원의 특정 구독 정보 여부를 확인한다.")
    @Test
    void 회원의_특정_구독_정보_여부를_확인한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        Member 후디 = memberRepository.save(후디());
        Subscription 빨간색_구독 = 빨간색_구독(후디, 공통_일정);
        subscriptionRepository.save(빨간색_구독);

        // when
        boolean actual = subscriptionRepository.existsByIdAndMemberId(빨간색_구독.getId(), 후디.getId());

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("회원의 존재하지 않는 구독 정보 여부를 확인한다.")
    @Test
    void 회원의_존재하지_않는_구독_정보_여부를_확인한다() {
        // given
        Member member = memberRepository.save(관리자());

        // when
        boolean actual = subscriptionRepository.existsByIdAndMemberId(0L, member.getId());

        // then
        assertThat(actual).isFalse();
    }
}

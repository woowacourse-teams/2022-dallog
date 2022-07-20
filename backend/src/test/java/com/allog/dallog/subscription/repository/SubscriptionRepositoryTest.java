package com.allog.dallog.subscription.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.common.fixtures.CategoryFixtures;
import com.allog.dallog.common.fixtures.MemberFixtures;
import com.allog.dallog.common.fixtures.SubscriptionFixtures;
import com.allog.dallog.global.config.JpaConfig;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.subscription.domain.Subscription;
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
        Member member = memberRepository.save(MemberFixtures.MEMBER);
        Category category1 = categoryRepository.save(CategoryFixtures.CATEGORY_1);
        Category category2 = categoryRepository.save(CategoryFixtures.CATEGORY_2);
        Category category3 = categoryRepository.save(CategoryFixtures.CATEGORY_3);

        Subscription subscription1 = new Subscription(member, category1, SubscriptionFixtures.COLOR_RED);
        Subscription subscription2 = new Subscription(member, category2, SubscriptionFixtures.COLOR_BLUE);
        Subscription subscription3 = new Subscription(member, category3, SubscriptionFixtures.COLOR_YELLOW);

        subscriptionRepository.save(subscription1);
        subscriptionRepository.save(subscription2);
        subscriptionRepository.save(subscription3);

        // when
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(member.getId());

        // then
        assertThat(subscriptions).hasSize(3);
    }

    @DisplayName("회원의 구독 정보가 존재하지 않는 경우 빈 리스트가 조회된다.")
    @Test
    void 회원의_구독_정보가_존재하지_않는_경우_빈_리스트가_조회된다() {
        // given
        Member member = memberRepository.save(MemberFixtures.MEMBER);

        // when
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(member.getId());

        // then
        assertThat(subscriptions).isEmpty();
    }
}

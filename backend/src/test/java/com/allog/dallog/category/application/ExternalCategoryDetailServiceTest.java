package com.allog.dallog.category.application;

import static com.allog.dallog.category.domain.CategoryType.GOOGLE;
import static com.allog.dallog.common.Constants.나인_이름;
import static com.allog.dallog.common.Constants.나인_이메일;
import static com.allog.dallog.common.Constants.나인_프로필_URL;
import static com.allog.dallog.common.Constants.외부_카테고리_ID;
import static com.allog.dallog.common.Constants.외부_카테고리_이름;
import static com.allog.dallog.subscription.domain.Color.COLOR_1;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.category.domain.CategoryType;
import com.allog.dallog.category.domain.ExternalCategoryDetail;
import com.allog.dallog.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.member.domain.SocialType;
import com.allog.dallog.subscription.domain.Subscription;
import com.allog.dallog.subscription.domain.SubscriptionRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExternalCategoryDetailServiceTest extends ServiceTest {

    @Autowired
    private ExternalCategoryDetailService externalCategoryDetailService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExternalCategoryDetailRepository externalCategoryDetailRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    void 월별_일정_조회를_하면_회원의_외부_서비스_카테고리_전체를_조회한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .외부_카테고리를_등록한다(외부_카테고리_이름, GOOGLE);

        // when
        List<ExternalCategoryDetail> actual = externalCategoryDetailService.findByMemberId(나인.회원().getId());

        // then
        assertThat(actual).hasSize(1);
    }

    private final class GivenBuilder {

        private Member member;

        private GivenBuilder 회원_가입을_한다(final String email, final String name, final String profile) {
            Member member = new Member(email, name, profile, SocialType.GOOGLE);
            this.member = memberRepository.save(member);
            return this;
        }

        private GivenBuilder 외부_카테고리를_등록한다(final String categoryName, final CategoryType categoryType) {
            Category category = new Category(categoryName, this.member, categoryType);
            ExternalCategoryDetail externalCategoryDetail = new ExternalCategoryDetail(category, 외부_카테고리_ID);
            Subscription subscription = new Subscription(this.member, category, COLOR_1);
            categoryRepository.save(category);
            externalCategoryDetailRepository.save(externalCategoryDetail);
            subscriptionRepository.save(subscription);
            return this;
        }

        private Member 회원() {
            return member;
        }
    }

    private GivenBuilder 나인() {
        return new GivenBuilder();
    }
}

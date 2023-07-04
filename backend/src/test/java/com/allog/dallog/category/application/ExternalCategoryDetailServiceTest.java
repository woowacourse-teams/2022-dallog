package com.allog.dallog.category.application;

import static com.allog.dallog.category.domain.CategoryType.GOOGLE;
import static com.allog.dallog.common.Constants.외부_카테고리_이름;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.category.domain.ExternalCategoryDetail;
import com.allog.dallog.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.common.builder.GivenBuilder;
import com.allog.dallog.member.domain.MemberRepository;
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
    void 월별_일정을_조회하면_회원의_외부_카테고리_전체를_조회한다() {
        // given
        GivenBuilder 나인 = 나인().외부_카테고리를_등록한다(외부_카테고리_이름, GOOGLE);

        // when
        List<ExternalCategoryDetail> actual = externalCategoryDetailService.findByMemberId(나인.회원().getId());

        // then
        assertThat(actual).hasSize(1);
    }
}

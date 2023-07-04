package com.allog.dallog.common.builder;

import com.allog.dallog.auth.domain.OAuthTokenRepository;
import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.schedule.domain.ScheduleRepository;
import com.allog.dallog.subscription.domain.SubscriptionRepository;
import org.springframework.stereotype.Component;

@Component
public class BuilderSupporter {

    private final MemberRepository memberRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final CategoryRepository categoryRepository;
    private final ExternalCategoryDetailRepository externalCategoryDetailRepository;
    private final CategoryRoleRepository categoryRoleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ScheduleRepository scheduleRepository;

    public BuilderSupporter(final MemberRepository memberRepository,
                            final OAuthTokenRepository oAuthTokenRepository,
                            final CategoryRepository categoryRepository,
                            final ExternalCategoryDetailRepository externalCategoryDetailRepository,
                            final CategoryRoleRepository categoryRoleRepository,
                            final SubscriptionRepository subscriptionRepository,
                            final ScheduleRepository scheduleRepository) {
        this.memberRepository = memberRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.categoryRepository = categoryRepository;
        this.externalCategoryDetailRepository = externalCategoryDetailRepository;
        this.categoryRoleRepository = categoryRoleRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public MemberRepository memberRepository() {
        return memberRepository;
    }

    public OAuthTokenRepository oAuthTokenRepository() {
        return oAuthTokenRepository;
    }

    public CategoryRepository categoryRepository() {
        return categoryRepository;
    }

    public ExternalCategoryDetailRepository externalCategoryDetailRepository() {
        return externalCategoryDetailRepository;
    }

    public CategoryRoleRepository categoryRoleRepository() {
        return categoryRoleRepository;
    }

    public SubscriptionRepository subscriptionRepository() {
        return subscriptionRepository;
    }

    public ScheduleRepository scheduleRepository() {
        return scheduleRepository;
    }
}

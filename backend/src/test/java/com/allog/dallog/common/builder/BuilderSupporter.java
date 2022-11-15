package com.allog.dallog.common.builder;

import com.allog.dallog.auth.domain.OAuthTokenRepository;
import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.schedule.domain.ScheduleRepository;
import com.allog.dallog.subscription.domain.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuilderSupporter {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExternalCategoryDetailRepository externalCategoryDetailRepository;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

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

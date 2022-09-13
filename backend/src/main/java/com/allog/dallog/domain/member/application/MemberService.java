package com.allog.dallog.domain.member.application;

import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.member.dto.MemberUpdateRequest;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ScheduleRepository scheduleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final OAuthTokenRepository oAuthTokenRepository;

    public MemberService(final MemberRepository memberRepository, final CategoryRepository categoryRepository,
                         final ScheduleRepository scheduleRepository,
                         final SubscriptionRepository subscriptionRepository,
                         final OAuthTokenRepository oAuthTokenRepository) {
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.scheduleRepository = scheduleRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
    }

    public MemberResponse findById(final Long id) {
        return new MemberResponse(memberRepository.getById(id));
    }

    public MemberResponse findBySubscriptionId(final Long subscriptionId) {
        Subscription subscription = subscriptionRepository.getById(subscriptionId);

        Member member = subscription.getMember();
        return new MemberResponse(member);
    }

    @Transactional
    public void update(final Long id, final MemberUpdateRequest request) {
        Member member = memberRepository.getById(id);
        member.change(request.getDisplayName());
    }

    @Transactional
    public void deleteById(final Long id) {
        List<Long> categoryIds = categoryRepository.findByMemberId(id)
                .stream()
                .map(Category::getId)
                .collect(Collectors.toList());

        scheduleRepository.deleteByCategoryIdIn(categoryIds);
        subscriptionRepository.deleteByCategoryIdIn(categoryIds);
        categoryRepository.deleteByMemberId(id);

        oAuthTokenRepository.deleteByMemberId(id);
        memberRepository.deleteById(id);
    }
}

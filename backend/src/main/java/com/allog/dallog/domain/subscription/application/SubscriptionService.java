package com.allog.dallog.domain.subscription.application;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionUpdateRequest;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionsResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ColorPicker colorPicker;

    public SubscriptionService(final SubscriptionRepository subscriptionRepository,
                               final MemberRepository memberRepository, final CategoryRepository categoryRepository,
                               final ColorPicker colorPicker) {
        this.subscriptionRepository = subscriptionRepository;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.colorPicker = colorPicker;
    }

    @Transactional
    public SubscriptionResponse save(final Long memberId, final Long categoryId) {
        subscriptionRepository.validateNotExistsByMemberIdAndCategoryId(memberId, categoryId);

        Member member = memberRepository.getById(memberId);
        Category category = categoryRepository.getById(categoryId);
        category.validateSubscriptionPossible(member);

        Color color = Color.pick(colorPicker.pickNumber());
        Subscription savedSubscription = subscriptionRepository.save(new Subscription(member, category, color));
        return new SubscriptionResponse(savedSubscription);
    }

    public SubscriptionResponse findById(final Long id) {
        Subscription subscription = subscriptionRepository.getById(id);
        return new SubscriptionResponse(subscription);
    }

    public SubscriptionsResponse findByMemberId(final Long memberId) {
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(memberId);

        List<SubscriptionResponse> subscriptionResponses = subscriptions.stream()
                .map(SubscriptionResponse::new)
                .collect(Collectors.toList());

        return new SubscriptionsResponse(subscriptionResponses);
    }

    public List<SubscriptionResponse> findByCategoryId(final Long categoryId) {
        return subscriptionRepository.findByCategoryId(categoryId)
                .stream()
                .map(SubscriptionResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(final Long id, final Long memberId, final SubscriptionUpdateRequest request) {
        subscriptionRepository.validateExistsByIdAndMemberId(id, memberId);
        Subscription subscription = subscriptionRepository.getById(id);
        subscription.change(request.getColor(), request.isChecked());
    }

    @Transactional
    public void delete(final Long id, final Long memberId) {
        Subscription subscription = subscriptionRepository.getById(id);
        subscription.validateDeletePossible(memberId);
        subscriptionRepository.deleteById(id);
    }
}

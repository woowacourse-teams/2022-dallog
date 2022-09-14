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
        subscriptionRepository.validateExistsSubscription(memberId, categoryId);

        Member member = memberRepository.getById(memberId);
        Category category = categoryRepository.getById(categoryId);
        member.validateCanSubscribe(category);

        Color color = Color.pick(colorPicker.pickNumber());
        Subscription savedSubscription = subscriptionRepository.save(new Subscription(member, category, color));
        return new SubscriptionResponse(savedSubscription);
    }

    public SubscriptionsResponse findByMemberId(final Long memberId) {
        List<Subscription> subscriptions = subscriptionRepository.getByMemberId(memberId);

        List<SubscriptionResponse> subscriptionResponses = subscriptions.stream()
                .map(SubscriptionResponse::new)
                .collect(Collectors.toList());

        return new SubscriptionsResponse(subscriptionResponses);
    }

    public SubscriptionResponse findById(final Long id) {
        Subscription subscription = subscriptionRepository.getById(id);
        return new SubscriptionResponse(subscription);
    }

    public List<SubscriptionResponse> findByCategoryId(final Long categoryId) {
        return subscriptionRepository.getByCategoryId(categoryId)
                .stream()
                .map(SubscriptionResponse::new)
                .collect(Collectors.toList());
    }

    // TODO: 상위 Service인 CalanderService에서만 사용하는 메서드입니다. 삭제 예정
    public List<Subscription> getAllByMemberId(final Long memberId) {
        return subscriptionRepository.getByMemberId(memberId);
    }

    @Transactional
    public void update(final Long id, final Long memberId, final SubscriptionUpdateRequest request) {
        Subscription subscription = subscriptionRepository.getById(id);
        Member member = memberRepository.getById(memberId);
        subscription.validateCanEditBy(member);
        subscription.change(request.getColor(), request.isChecked());
    }

    @Transactional
    public void delete(final Long id, final Long memberId) {
        Subscription subscription = subscriptionRepository.getById(id);
        Member member = memberRepository.getById(memberId);
        subscription.validateCanDeleteBy(member);
        subscriptionRepository.deleteById(id);
    }
}

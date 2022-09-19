package com.allog.dallog.domain.category.application;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.domain.Subscriptions;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ExternalCategoryDetailService {

    private final ExternalCategoryDetailRepository externalCategoryDetailRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ExternalCategoryDetailService(final ExternalCategoryDetailRepository externalCategoryDetailRepository,
                                         final SubscriptionRepository subscriptionRepository) {
        this.externalCategoryDetailRepository = externalCategoryDetailRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<ExternalCategoryDetail> findByMemberId(final Long memberId) {
        Subscriptions subscriptions = new Subscriptions(subscriptionRepository.findByMemberId(memberId));
        List<Category> categories = subscriptions.findExternalCategory();
        return externalCategoryDetailRepository.findByCategoryIn(categories);
    }
}

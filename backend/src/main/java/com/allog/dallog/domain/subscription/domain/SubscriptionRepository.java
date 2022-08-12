package com.allog.dallog.domain.subscription.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    boolean existsByMemberIdAndCategoryId(final Long memberId, final Long categoryId);

    List<Subscription> findByMemberId(final Long memberId);

    boolean existsByIdAndMemberId(final Long id, final Long memberId);

    void deleteByIdAndMemberId(final Long id, final Long memberId);

    void deleteByMemberId(final Long memberId);
}

package com.allog.dallog.subscription.repository;

import com.allog.dallog.subscription.domain.Subscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByMemberId(final Long memberId);
}

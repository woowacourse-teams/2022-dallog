package com.allog.dallog.domain.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(final String email);

    @Query("SELECT s.member "
            + "FROM Subscription s "
            + "WHERE s.id = :subscriptionId")
    Optional<Member> findBySubscriptionId(final Long subscriptionId);

    boolean existsByEmail(final String email);
}

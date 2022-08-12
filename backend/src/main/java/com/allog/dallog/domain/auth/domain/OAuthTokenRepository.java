package com.allog.dallog.domain.auth.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {

    boolean existsByMemberId(final Long memberId);

    Optional<OAuthToken> findByMemberId(final Long memberId);

    void deleteByMemberId(final Long memberId);
}

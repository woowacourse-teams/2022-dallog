package com.allog.dallog.domain.auth.domain;

import com.allog.dallog.domain.auth.exception.NoSuchOAuthTokenException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {

    boolean existsByMemberId(final Long memberId);

    @Query("SELECT o "
            + "FROM OAuthToken o "
            + "WHERE o.member.id = :memberId")
    Optional<OAuthToken> findByMemberId(final Long memberId);

    default OAuthToken getByMemberId(final Long memberId) {
        return findByMemberId(memberId)
                .orElseThrow(NoSuchOAuthTokenException::new);
    }
}

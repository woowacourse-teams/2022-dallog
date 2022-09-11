package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OAuthTokenService {

    private final OAuthTokenRepository oAuthTokenRepository;

    public OAuthTokenService(final OAuthTokenRepository oAuthTokenRepository) {
        this.oAuthTokenRepository = oAuthTokenRepository;
    }

    public boolean existsByMemberId(final Long memberId) {
        return oAuthTokenRepository.existsByMemberId(memberId);
    }

    @Transactional
    public OAuthToken save(final Member member, final String refreshToken) {
        OAuthToken oAuthToken = new OAuthToken(member, refreshToken);
        return oAuthTokenRepository.save(oAuthToken);
    }

    public OAuthToken getByMemberId(final Long memberId) {
        return oAuthTokenRepository.findByMemberId(memberId)
                .orElseThrow(NoSuchMemberException::new);
    }
}

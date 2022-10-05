package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.domain.DallogToken;
import com.allog.dallog.domain.auth.domain.TokenRepository;
import com.allog.dallog.domain.auth.dto.response.AccessTokenResponse;
import com.allog.dallog.domain.auth.exception.NoSuchTokenException;
import org.springframework.stereotype.Component;

@Component
public class DallogTokenManager {

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    public DallogTokenManager(final TokenProvider tokenProvider, final TokenRepository tokenRepository) {
        this.tokenProvider = tokenProvider;
        this.tokenRepository = tokenRepository;
    }

    public DallogToken createDallogToken(final Long memberId) {
        String accessToken = tokenProvider.createAccessToken(String.valueOf(memberId));
        String refreshToken = findRefreshToken(memberId);
        return new DallogToken(accessToken, refreshToken);
    }

    public AccessTokenResponse createDallogToken(final String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        Long memberId = Long.valueOf(tokenProvider.getPayload(refreshToken));

        String refreshTokenInMemory = tokenRepository.getToken(memberId);
        if (!refreshTokenInMemory.equals(refreshToken)) {
            throw new NoSuchTokenException("회원의 리프레시 토큰이 아닙니다.");
        }
        String accessToken = tokenProvider.createAccessToken(String.valueOf(memberId));
        return new AccessTokenResponse(accessToken);
    }

    private String findRefreshToken(final Long memberId) {
        if (tokenRepository.exist(memberId)) {
            return tokenRepository.getToken(memberId);
        }
        String refreshToken = tokenProvider.createRefreshToken(String.valueOf(memberId));
        return tokenRepository.save(memberId, refreshToken);
    }
}

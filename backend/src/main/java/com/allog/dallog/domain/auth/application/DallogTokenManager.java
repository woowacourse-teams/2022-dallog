package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.domain.DallogToken;
import com.allog.dallog.domain.auth.domain.TokenRepository;
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
        String refreshToken = createRefreshToken(memberId);
        return new DallogToken(accessToken, refreshToken);
    }

    private String createRefreshToken(final Long memberId) {
        if (tokenRepository.exist(memberId)) {
            return tokenRepository.getToken(memberId);
        }
        String refreshToken = tokenProvider.createRefreshToken(String.valueOf(memberId));
        return tokenRepository.save(memberId, refreshToken);
    }

    public DallogToken renewDallogToken(final String outRefreshToken) {
        tokenProvider.validateToken(outRefreshToken);
        Long memberId = Long.valueOf(tokenProvider.getPayload(outRefreshToken));

        String accessToken = tokenProvider.createAccessToken(String.valueOf(memberId));
        String refreshToken = tokenRepository.getToken(memberId);

        DallogToken renewalDallogToken = new DallogToken(refreshToken, accessToken);
        renewalDallogToken.validateSameRefreshToken(outRefreshToken);

        return renewalDallogToken;
    }
}

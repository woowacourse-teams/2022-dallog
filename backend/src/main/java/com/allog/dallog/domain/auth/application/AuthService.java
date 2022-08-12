package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.TokenResponse;
import com.allog.dallog.domain.auth.exception.NoSuchOAuthTokenException;
import com.allog.dallog.domain.composition.application.RegisterService;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthService {

    private final OAuthUri oAuthUri;
    private final OAuthClient oAuthClient;
    private final MemberService memberService;
    private final RegisterService registerService;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final TokenProvider tokenProvider;

    public AuthService(final OAuthUri oAuthUri, final OAuthClient oAuthClient, final MemberService memberService,
                       final RegisterService registerService, final OAuthTokenRepository oAuthTokenRepository,
                       final TokenProvider tokenProvider) {
        this.oAuthUri = oAuthUri;
        this.oAuthClient = oAuthClient;
        this.memberService = memberService;
        this.registerService = registerService;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.tokenProvider = tokenProvider;
    }

    public String generateGoogleLink() {
        return oAuthUri.generate();
    }

    @Transactional
    public TokenResponse generateToken(final String code) {
        OAuthMember oAuthMember = oAuthClient.getOAuthMember(code);
        Member foundMember = getMember(oAuthMember);

        OAuthToken oAuthToken = getOAuthToken(oAuthMember, foundMember);
        oAuthToken.change(oAuthMember.getRefreshToken());
        String accessToken = tokenProvider.createToken(String.valueOf(foundMember.getId()));

        return new TokenResponse(accessToken);
    }

    private Member getMember(final OAuthMember oAuthMember) {
        if (!memberService.existsByEmail(oAuthMember.getEmail())) {
            registerService.register(oAuthMember);
        }

        return memberService.getByEmail(oAuthMember.getEmail());
    }

    private OAuthToken getOAuthToken(final OAuthMember oAuthMember, final Member foundMember) {
        if (!oAuthTokenRepository.existsByMemberId(foundMember.getId())) {
            oAuthTokenRepository.save(new OAuthToken(foundMember, oAuthMember.getRefreshToken()));
        }

        return oAuthTokenRepository.findByMemberId(foundMember.getId())
                .orElseThrow(NoSuchOAuthTokenException::new);
    }

    public Long extractMemberId(final String accessToken) {
        tokenProvider.validateToken(accessToken);
        Long memberId = Long.valueOf(tokenProvider.getPayload(accessToken));
        memberService.validateExistsMember(memberId);
        return memberId;
    }
}

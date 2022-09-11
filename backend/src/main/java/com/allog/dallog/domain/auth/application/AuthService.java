package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.request.TokenRequest;
import com.allog.dallog.domain.auth.dto.response.TokenResponse;
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
    private final AuthBeforeEvent authGenerateTokenBeforeEvent;
    private final TokenProvider tokenProvider;

    public AuthService(final OAuthUri oAuthUri, final OAuthClient oAuthClient, final MemberService memberService,
                       final AuthBeforeEvent authGenerateTokenBeforeEvent, final TokenProvider tokenProvider) {
        this.oAuthUri = oAuthUri;
        this.oAuthClient = oAuthClient;
        this.memberService = memberService;
        this.authGenerateTokenBeforeEvent = authGenerateTokenBeforeEvent;
        this.tokenProvider = tokenProvider;
    }

    public String generateGoogleLink(final String redirectUri) {
        return oAuthUri.generate(redirectUri);
    }

    @Transactional
    public TokenResponse generateToken(final TokenRequest tokenRequest) {
        OAuthMember oAuthMember = oAuthClient.getOAuthMember(tokenRequest.getCode(), tokenRequest.getRedirectUri());
        authGenerateTokenBeforeEvent.process(oAuthMember);

        Member foundMember = memberService.getByEmail(oAuthMember.getEmail());
        String accessToken = tokenProvider.createToken(String.valueOf(foundMember.getId()));

        return new TokenResponse(accessToken);
    }

    public Long extractMemberId(final String accessToken) {
        tokenProvider.validateToken(accessToken);
        Long memberId = Long.valueOf(tokenProvider.getPayload(accessToken));
        memberService.validateExistsMember(memberId);
        return memberId;
    }
}

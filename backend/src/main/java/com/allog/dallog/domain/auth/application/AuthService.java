package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.request.TokenRequest;
import com.allog.dallog.domain.auth.dto.response.TokenResponse;
import com.allog.dallog.domain.auth.exception.NoSuchOAuthTokenException;
import com.allog.dallog.domain.member.application.MemberAfterEvent;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.SocialType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthService {

    private final OAuthUri oAuthUri;
    private final OAuthClient oAuthClient;
    private final MemberService memberService;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final TokenProvider tokenProvider;
    private final MemberAfterEvent memberSaveAfterEvent;

    public AuthService(final OAuthUri oAuthUri, final OAuthClient oAuthClient, final MemberService memberService,
                       final OAuthTokenRepository oAuthTokenRepository, final TokenProvider tokenProvider,
                       final MemberAfterEvent memberSaveAfterEvent) {
        this.oAuthUri = oAuthUri;
        this.oAuthClient = oAuthClient;
        this.memberService = memberService;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.tokenProvider = tokenProvider;
        this.memberSaveAfterEvent = memberSaveAfterEvent;
    }

    public String generateGoogleLink(final String redirectUri) {
        return oAuthUri.generate(redirectUri);
    }

    @Transactional
    public TokenResponse generateToken(final TokenRequest tokenRequest) {
        String code = tokenRequest.getCode();
        String redirectUri = tokenRequest.getRedirectUri();

        OAuthMember oAuthMember = oAuthClient.getOAuthMember(code, redirectUri);
        Member foundMember = getMember(oAuthMember);

        OAuthToken oAuthToken = getOAuthToken(oAuthMember, foundMember);
        oAuthToken.change(oAuthMember.getRefreshToken());
        String accessToken = tokenProvider.createToken(String.valueOf(foundMember.getId()));

        return new TokenResponse(accessToken);
    }

    private Member getMember(final OAuthMember oAuthMember) {
        if (!memberService.existsByEmail(oAuthMember.getEmail())) {
            memberService.save(parseMember(oAuthMember), memberSaveAfterEvent);
        }

        return memberService.getByEmail(oAuthMember.getEmail());
    }

    private Member parseMember(final OAuthMember oAuthMember) {
        return new Member(oAuthMember.getEmail(), oAuthMember.getDisplayName(), oAuthMember.getProfileImageUrl(),
                SocialType.GOOGLE);
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

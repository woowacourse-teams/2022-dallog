package com.allog.dallog.auth.service;

import com.allog.dallog.auth.dto.OAuthMember;
import com.allog.dallog.auth.dto.TokenResponse;
import com.allog.dallog.auth.support.JwtTokenProvider;
import com.allog.dallog.auth.support.OAuthClient;
import com.allog.dallog.auth.support.OAuthEndpoint;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.SocialType;
import com.allog.dallog.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthService {

    private final OAuthEndpoint oAuthEndpoint;
    private final OAuthClient oAuthClient;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final OAuthEndpoint oAuthEndpoint, final OAuthClient oAuthClient,
                       final MemberService memberService, final JwtTokenProvider jwtTokenProvider) {
        this.oAuthEndpoint = oAuthEndpoint;
        this.oAuthClient = oAuthClient;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String generateGoogleLink() {
        return oAuthEndpoint.generate();
    }

    @Transactional
    public TokenResponse generateTokenWithCode(final String code) {
        OAuthMember oAuthMember = oAuthClient.getOAuthMember(code);
        String email = oAuthMember.getEmail();

        saveMemberIfNotExists(oAuthMember, email);

        Member foundMember = memberService.findByEmail(email);
        String accessToken = jwtTokenProvider.createToken(String.valueOf(foundMember.getId()));

        return new TokenResponse(accessToken);
    }

    private void saveMemberIfNotExists(final OAuthMember oAuthMember, final String email) {
        if (!memberService.existsByEmail(email)) {
            memberService.save(generateMemberBy(oAuthMember));
        }
    }

    private Member generateMemberBy(final OAuthMember oAuthMember) {
        return new Member(oAuthMember.getEmail(), oAuthMember.getProfileImageUrl(), oAuthMember.getDisplayName(),
                SocialType.GOOGLE);
    }
}

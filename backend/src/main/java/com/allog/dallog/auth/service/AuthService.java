package com.allog.dallog.auth.service;

import com.allog.dallog.auth.dto.OAuthMember;
import com.allog.dallog.auth.dto.TokenResponse;
import com.allog.dallog.auth.exception.InvalidTokenException;
import com.allog.dallog.auth.exception.NotFoundDataException;
import com.allog.dallog.auth.support.JwtTokenProvider;
import com.allog.dallog.auth.support.OAuthClient;
import com.allog.dallog.auth.support.OAuthUri;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.SocialType;
import com.allog.dallog.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthService {

    private final OAuthUri oAuthUri;
    private final OAuthClient oAuthClient;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final OAuthUri oAuthUri, final OAuthClient oAuthClient,
                       final MemberService memberService, final JwtTokenProvider jwtTokenProvider) {
        this.oAuthUri = oAuthUri;
        this.oAuthClient = oAuthClient;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String generateGoogleLink() {
        return oAuthUri.generate();
    }

    @Transactional
    public TokenResponse generateTokenWithCode(final String code) {
        OAuthMember oAuthMember = oAuthClient.getOAuthMember(code);
        String email = oAuthMember.getEmail();

        saveMemberIfNotExists(oAuthMember, email);

        Member foundMember = memberService.getByEmail(email);
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

    public void validateToken(final String accessToken) {
        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            throw new InvalidTokenException("권한이 없습니다.");
        }
    }

    public String getPayload(final String accessToken) {
        return jwtTokenProvider.getPayload(accessToken);
    }

    public void validateExistsId(final Long id) {
        if (!memberService.existsById(id)) {
            throw new NotFoundDataException("존재하지 않는 회원 입니다.");
        }
    }
}

package com.allog.dallog.domain.composition.application;

import com.allog.dallog.domain.auth.application.AuthBeforeEvent;
import com.allog.dallog.domain.auth.application.OAuthTokenService;
import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.SocialType;
import javax.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class AuthGenerateTokenBeforeEvent implements AuthBeforeEvent {

    private static final int ARGS_SIZE = 1;
    private static final int ARGS_OAUTH_MEMBER_INDEX = 0;

    private final MemberService memberService;
    private final OAuthTokenService oAuthTokenService;

    public AuthGenerateTokenBeforeEvent(final MemberService memberService, final OAuthTokenService oAuthTokenService) {
        this.memberService = memberService;
        this.oAuthTokenService = oAuthTokenService;
    }

    @Transactional
    @Override
    public void process(final Object... args) {
        validateArgs(args);

        OAuthMember oAuthMember = parseOAuthMember(args);
        Member foundMember = getMember(oAuthMember);

        OAuthToken oAuthToken = getOAuthToken(oAuthMember, foundMember);
        oAuthToken.change(oAuthMember.getRefreshToken());
    }

    private void validateArgs(final Object... args) {
        if (args.length != ARGS_SIZE || !(args[ARGS_OAUTH_MEMBER_INDEX] instanceof OAuthMember)) {
            throw new IllegalArgumentException("AuthGenerateTokenAfterEvent에서 필요한 매개변수 목록과 일치하지 않습니다.");
        }
    }

    private OAuthMember parseOAuthMember(final Object... args) {
        return (OAuthMember) args[ARGS_OAUTH_MEMBER_INDEX];
    }

    private Member getMember(final OAuthMember oAuthMember) {
        if (!memberService.existsByEmail(oAuthMember.getEmail())) {
            memberService.save(parseMember(oAuthMember));
        }

        return memberService.getByEmail(oAuthMember.getEmail());
    }

    private Member parseMember(final OAuthMember oAuthMember) {
        return new Member(oAuthMember.getEmail(), oAuthMember.getDisplayName(), oAuthMember.getProfileImageUrl(),
                SocialType.GOOGLE);
    }

    private OAuthToken getOAuthToken(final OAuthMember oAuthMember, final Member member) {
        if (!oAuthTokenService.existsByMemberId(member.getId())) {
            return oAuthTokenService.save(member, oAuthMember.getRefreshToken());
        }

        return oAuthTokenService.getByMemberId(member.getId());
    }
}

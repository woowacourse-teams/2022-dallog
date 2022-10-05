package com.allog.dallog.domain.auth.application;

import static com.allog.dallog.domain.category.domain.CategoryType.PERSONAL;

import com.allog.dallog.domain.auth.domain.DallogToken;
import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.auth.domain.TokenRepository;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.request.TokenRenewalRequest;
import com.allog.dallog.domain.auth.dto.request.TokenRequest;
import com.allog.dallog.domain.auth.dto.response.AccessAndRefreshTokenResponse;
import com.allog.dallog.domain.auth.dto.response.AccessTokenResponse;
import com.allog.dallog.domain.auth.exception.NoSuchTokenException;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.subscription.application.ColorPicker;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthService {

    private static final String PERSONAL_CATEGORY_NAME = "내 일정";

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final OAuthUri oAuthUri;
    private final OAuthClient oAuthClient;
    private final TokenProvider tokenProvider;
    private final ColorPicker colorPicker;
    private final TokenRepository tokenRepository;
    private final DallogTokenManager dallogTokenManager;

    public AuthService(final MemberRepository memberRepository, final CategoryRepository categoryRepository,
                       final OAuthTokenRepository oAuthTokenRepository,
                       final SubscriptionRepository subscriptionRepository, final OAuthUri oAuthUri,
                       final OAuthClient oAuthClient, final TokenProvider tokenProvider,
                       final ColorPicker colorPicker, final TokenRepository tokenRepository,
                       final DallogTokenManager dallogTokenManager) {
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.oAuthUri = oAuthUri;
        this.oAuthClient = oAuthClient;
        this.tokenProvider = tokenProvider;
        this.colorPicker = colorPicker;
        this.tokenRepository = tokenRepository;
        this.dallogTokenManager = dallogTokenManager;
    }

    public String generateGoogleLink(final String redirectUri) {
        return oAuthUri.generate(redirectUri);
    }

    @Transactional
    public AccessAndRefreshTokenResponse generateAccessAndRefreshToken(final TokenRequest tokenRequest) {
        String code = tokenRequest.getCode();
        String redirectUri = tokenRequest.getRedirectUri();

        OAuthMember oAuthMember = oAuthClient.getOAuthMember(code, redirectUri);
        Member foundMember = findMember(oAuthMember);

        OAuthToken oAuthToken = getOAuthToken(oAuthMember, foundMember);
        oAuthToken.change(oAuthMember.getRefreshToken()); // 의견 제시 할 부분!

        DallogToken dallogToken = dallogTokenManager.createDallogToken(foundMember.getId());
        return new AccessAndRefreshTokenResponse(dallogToken.getAccessToken(), dallogToken.getRefreshToken());
    }

    private Member findMember(final OAuthMember oAuthMember) {
        String email = oAuthMember.getEmail();
        if (memberRepository.existsByEmail(email)) {
            return memberRepository.getByEmail(email);
        }
        return saveMember(oAuthMember);
    }

    private Member saveMember(final OAuthMember oAuthMember) {
        Member savedMember = memberRepository.save(oAuthMember.toMember());
        Category savedCategory = categoryRepository.save(new Category(PERSONAL_CATEGORY_NAME, savedMember, PERSONAL));

        Color randomColor = Color.pick(colorPicker.pickNumber());
        subscriptionRepository.save(new Subscription(savedMember, savedCategory, randomColor));
        return savedMember;
    }

    private OAuthToken getOAuthToken(final OAuthMember oAuthMember, final Member member) {
        Long memberId = member.getId();
        if (oAuthTokenRepository.existsByMemberId(memberId)) {
            return oAuthTokenRepository.getByMemberId(memberId);
        }
        return oAuthTokenRepository.save(new OAuthToken(member, oAuthMember.getRefreshToken()));
    }

    public AccessTokenResponse generateAccessToken(final TokenRenewalRequest tokenRenewalRequest) {
        String refreshToken = tokenRenewalRequest.getRefreshToken();
        tokenProvider.validateToken(refreshToken);
        Long memberId = Long.valueOf(tokenProvider.getPayload(refreshToken));

        String refreshTokenInMemory = tokenRepository.getToken(memberId);
        if (!refreshTokenInMemory.equals(refreshToken)) {
            throw new NoSuchTokenException("회원의 리프레시 토큰이 아닙니다.");
        }
        String accessToken = tokenProvider.createAccessToken(String.valueOf(memberId));
        return new AccessTokenResponse(accessToken);
    }

    public Long extractMemberId(final String accessToken) {
        tokenProvider.validateToken(accessToken);
        Long memberId = Long.valueOf(tokenProvider.getPayload(accessToken));
        memberRepository.validateExistsById(memberId);
        return memberId;
    }
}

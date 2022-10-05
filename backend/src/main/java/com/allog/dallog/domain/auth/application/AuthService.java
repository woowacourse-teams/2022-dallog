package com.allog.dallog.domain.auth.application;

import static com.allog.dallog.domain.category.domain.CategoryType.PERSONAL;

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
import com.allog.dallog.domain.member.domain.SocialType;
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

    public AuthService(final MemberRepository memberRepository, final CategoryRepository categoryRepository,
                       final OAuthTokenRepository oAuthTokenRepository,
                       final SubscriptionRepository subscriptionRepository, final OAuthUri oAuthUri,
                       final OAuthClient oAuthClient, final TokenProvider tokenProvider,
                       final ColorPicker colorPicker, final TokenRepository tokenRepository) {
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.oAuthUri = oAuthUri;
        this.oAuthClient = oAuthClient;
        this.tokenProvider = tokenProvider;
        this.colorPicker = colorPicker;
        this.tokenRepository = tokenRepository;
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
        oAuthToken.change(oAuthMember.getRefreshToken());

        return createTokenResponse(foundMember);
    }

    private AccessAndRefreshTokenResponse createTokenResponse(final Member member) {
        Long memberId = member.getId();
        String accessToken = tokenProvider.createAccessToken(String.valueOf(memberId));

        if (tokenRepository.exist(memberId)) {
            String refreshToken = tokenRepository.getToken(memberId);
            return new AccessAndRefreshTokenResponse(accessToken, refreshToken);
        }

        String refreshToken = tokenProvider.createRefreshToken(String.valueOf(memberId));
        tokenRepository.save(memberId, refreshToken);
        return new AccessAndRefreshTokenResponse(accessToken, refreshToken);
    }

    private Member findMember(final OAuthMember oAuthMember) {
        if (memberRepository.existsByEmail(oAuthMember.getEmail())) {
            return memberRepository.getByEmail(oAuthMember.getEmail());
        }

        return saveMember(oAuthMember);
    }

    private Member saveMember(final OAuthMember oAuthMember) {
        Member savedMember = memberRepository.save(toMember(oAuthMember));
        Category savedCategory = saveCategory(savedMember);
        saveSubscription(savedMember, savedCategory);

        return savedMember;
    }

    private Member toMember(final OAuthMember oAuthMember) {
        return new Member(oAuthMember.getEmail(), oAuthMember.getDisplayName(), oAuthMember.getProfileImageUrl(),
                SocialType.GOOGLE);
    }

    private Category saveCategory(final Member savedMember) {
        return categoryRepository.save(new Category(PERSONAL_CATEGORY_NAME, savedMember, PERSONAL));
    }

    private Subscription saveSubscription(final Member savedMember, final Category savedCategory) {
        Color randomColor = Color.pick(colorPicker.pickNumber());
        return subscriptionRepository.save(new Subscription(savedMember, savedCategory, randomColor));
    }

    private OAuthToken getOAuthToken(final OAuthMember oAuthMember, final Member foundMember) {
        if (oAuthTokenRepository.existsByMemberId(foundMember.getId())) {
            return oAuthTokenRepository.getByMemberId(foundMember.getId());
        }

        return oAuthTokenRepository.save(new OAuthToken(foundMember, oAuthMember.getRefreshToken()));
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

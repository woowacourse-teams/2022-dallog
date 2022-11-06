package com.allog.dallog.domain.subscription.application;

import static com.allog.dallog.common.Constants.MEMBER_이름;
import static com.allog.dallog.common.Constants.MEMBER_이메일;
import static com.allog.dallog.common.Constants.MEMBER_프로필_URL;
import static com.allog.dallog.common.Constants.개인_카테고리_이름;
import static com.allog.dallog.common.Constants.네오_이름;
import static com.allog.dallog.common.Constants.네오_이메일;
import static com.allog.dallog.common.Constants.네오_프로필_URL;
import static com.allog.dallog.common.Constants.박람회_카테고리_이름;
import static com.allog.dallog.common.Constants.취업_카테고리_이름;
import static com.allog.dallog.common.Constants.포비_이름;
import static com.allog.dallog.common.Constants.포비_이메일;
import static com.allog.dallog.common.Constants.포비_프로필_URL;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.category.domain.CategoryType.PERSONAL;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.NONE;
import static com.allog.dallog.domain.subscription.domain.Color.COLOR_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.CategoryType;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleType;
import com.allog.dallog.domain.categoryrole.exception.NoSuchCategoryRoleException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.domain.SocialType;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionUpdateRequest;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionsResponse;
import com.allog.dallog.domain.subscription.exception.ExistSubscriptionException;
import com.allog.dallog.domain.subscription.exception.InvalidSubscriptionException;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import com.allog.dallog.domain.subscription.exception.NotAbleToUnsubscribeException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class SubscriptionServiceTest extends ServiceTest {

    private final SubscriptionUpdateRequest 구독_정보_변경_요청 = new SubscriptionUpdateRequest(COLOR_1, true);

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    private User 네오;
    private User 포비;
    private User 제이슨;

    @BeforeEach
    void setUp() {
        네오 = new User();
        포비 = new User();
        제이슨 = new User();
    }

    @DisplayName("새로운 구독을 생성한다.")
    @Test
    void 새로운_구독을_생성한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL);

        // when
        SubscriptionResponse response = subscriptionService.save(포비.계정().getId(), 네오.카테고리().getId());

        // then
        assertThat(response.getCategory().getName()).isEqualTo(취업_카테고리_이름);
    }

    @DisplayName("자신이 생성하지 않은 개인 카테고리를 구독시 예외가 발생한다.")
    @Test
    void 자신이_생성하지_않은_개인_카테고리를_구독시_예외가_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(개인_카테고리_이름, PERSONAL);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL);

        // when & then
        assertThatThrownBy(() -> subscriptionService.save(포비.계정().getId(), 네오.카테고리().getId()))
                .isInstanceOf(NoPermissionException.class)
                .hasMessage("구독 권한이 없는 카테고리입니다.");
    }

    @DisplayName("이미 존재하는 구독 정보를 저장할 경우 예외를 던진다.")
    @Test
    void 이미_존재하는_구독_정보를_저장할_경우_예외를_던진다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(개인_카테고리_이름, PERSONAL);

        // when & then
        assertThatThrownBy(() -> subscriptionService.save(네오.계정().getId(), 네오.카테고리().getId()))
                .isInstanceOf(ExistSubscriptionException.class);
    }

    @DisplayName("구독 id를 기반으로 단건 조회한다.")
    @Test
    void 구독_id를_기반으로_단건_조회한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(개인_카테고리_이름, PERSONAL);

        // when
        SubscriptionResponse actual = subscriptionService.findById(네오.구독().getId());

        // then
        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(네오.구독().getId());
            assertThat(actual.getCategory().getId()).isEqualTo(네오.카테고리().getId());
        });
    }

    @DisplayName("존재하지 않는 구독 정보인 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_구독_정보인_경우_예외를_던진다() {
        // given & when & then
        assertThatThrownBy(() -> subscriptionService.findById(0L))
                .isInstanceOf(NoSuchSubscriptionException.class);
    }

    @DisplayName("회원 정보를 기반으로 구독 정보를 조회한다.")
    @Test
    void 회원_정보를_기반으로_구독_정보를_조회한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(박람회_카테고리_이름, NORMAL);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        네오.카테고리를_등록한다(취업_카테고리_이름, NORMAL);
        포비.카테고리를_구독한다(네오.카테고리());

        // when
        SubscriptionsResponse actual = subscriptionService.findByMemberId(포비.계정().getId());

        // then
        assertThat(actual.getSubscriptions()).hasSize(2);
    }

    @DisplayName("category id를 기반으로 구독 정보를 조회한다.")
    @Test
    void category_id를_기반으로_구독_정보를_조회한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(박람회_카테고리_이름, NORMAL);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        제이슨.회원_가입을_한다(MEMBER_이메일, MEMBER_이름, MEMBER_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        // when
        List<SubscriptionResponse> actual = subscriptionService.findByCategoryId(네오.카테고리().getId());

        // then
        assertThat(actual).hasSize(3);
    }

    @DisplayName("구독 정보를 수정한다.")
    @Test
    void 구독_정보를_수정한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(박람회_카테고리_이름, NORMAL);

        // when
        subscriptionService.update(네오.구독().getId(), 네오.계정().getId(), 구독_정보_변경_요청);

        // then
        Subscription actual = subscriptionRepository.getById(네오.구독().getId());

        assertAll(() -> {
            assertThat(actual.getColor()).isEqualTo(COLOR_1);
            assertThat(actual.isChecked()).isTrue();
        });
    }

    @DisplayName("구독 정보 수정 시 존재하지 않는 색상인 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"#111", "#1111", "#11111", "123456", "#**1234", "##12345", "334172#", "#00FF00"})
    void 구독_정보_수정_시_존재하지_않는_색상인_경우_예외를_던진다(final String invalidColorCode) {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(박람회_카테고리_이름, NORMAL);

        SubscriptionUpdateRequest 잘못된_구독_변경_요청 = new SubscriptionUpdateRequest(invalidColorCode, true);
        // when & then
        assertThatThrownBy(() -> subscriptionService.update(네오.구독().getId(), 네오.계정().getId(), 잘못된_구독_변경_요청))
                .isInstanceOf(InvalidSubscriptionException.class);
    }

    @DisplayName("구독 정보를 삭제한다.")
    @Test
    void 구독_정보를_삭제한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(박람회_카테고리_이름, NORMAL);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        // when
        subscriptionService.delete(포비.구독().getId(), 포비.계정().getId());

        // then
        assertThatThrownBy(() -> subscriptionRepository.getById(포비.구독().getId()))
                .isInstanceOf(NoSuchSubscriptionException.class);
    }

    @DisplayName("자신의 구독 정보가 아닌 구독을 삭제할 경우 예외를 던진다.")
    @Test
    void 자신의_구독_정보가_아닌_구독을_삭제할_경우_예외를_던진다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(박람회_카테고리_이름, NORMAL);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        // when & then
        assertThatThrownBy(() -> subscriptionService.delete(포비.구독().getId(), 네오.계정().getId()))
                .isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("카테고리를 구독하면 카테고리에 대한 NONE 역할이 생성된다")
    @Test
    void 카테고리를_구독하면_카테고리에_대한_NONE_역할이_생성된다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(박람회_카테고리_이름, NORMAL);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL);

        // when
        subscriptionService.save(포비.계정().getId(), 네오.카테고리().getId());

        // then
        CategoryRole actual = categoryRoleRepository.getByMemberIdAndCategoryId(포비.계정().getId(), 네오.카테고리().getId());
        assertThat(actual.getCategoryRoleType()).isEqualTo(CategoryRoleType.NONE);
    }

    @DisplayName("카테고리를 구독 해제하면 카테고리에 대한 역할이 제거된다")
    @Test
    void 카테고리를_구독_해제하면_카테고리에_대한_역할이_제거된다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(박람회_카테고리_이름, NORMAL);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        // when
        subscriptionService.delete(포비.구독().getId(), 포비.계정().getId());

        // then
        assertThatThrownBy(() -> categoryRoleRepository.getByMemberIdAndCategoryId(포비.계정().getId(), 네오.카테고리().getId()))
                .isInstanceOf(NoSuchCategoryRoleException.class);
    }

    @Transactional
    @DisplayName("카테고리 역할이 NONE이 아닌 경우 카테고리를 구독 해제할 수 없다")
    @Test
    void 카테고리_역할이_NONE이_아닌_경우_카테고리를_구독_해제할_수_없다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(박람회_카테고리_이름, NORMAL);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        네오.내_카테고리_관리_권한을_부여한다(포비.계정());

        // when & then
        assertThatThrownBy(() -> subscriptionService.delete(포비.구독().getId(), 포비.계정().getId()))
                .isInstanceOf(NotAbleToUnsubscribeException.class);
    }

    private final class User {

        private Member member;
        private Category category;
        private Subscription subscription;

        private User 회원_가입을_한다(final String email, final String name, final String profile) {
            this.member = new Member(email, name, profile, SocialType.GOOGLE);
            memberRepository.save(member);
            return this;
        }

        private User 카테고리를_등록한다(final String categoryName, final CategoryType categoryType) {
            this.category = new Category(categoryName, this.member, categoryType);
            CategoryRole categoryRole = new CategoryRole(category, this.member, ADMIN);
            this.subscription = new Subscription(this.member, category, COLOR_1);
            categoryRepository.save(category);
            categoryRoleRepository.save(categoryRole);
            subscriptionRepository.save(subscription);
            return this;
        }

        private User 카테고리를_구독한다(final Category category) {
            this.subscription = new Subscription(this.member, category, COLOR_1);
            CategoryRole categoryRole = new CategoryRole(category, this.member, NONE);
            subscriptionRepository.save(subscription);
            categoryRoleRepository.save(categoryRole);
            return this;
        }

        private User 내_카테고리_관리_권한을_부여한다(final Member otherMember) {
            CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(otherMember.getId(),
                    category.getId());
            categoryRole.changeRole(ADMIN);
            return this;
        }

        private Member 계정() {
            return member;
        }

        private Category 카테고리() {
            return category;
        }

        private Subscription 구독() {
            return subscription;
        }
    }
}

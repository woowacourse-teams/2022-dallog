package com.allog.dallog.domain.category.application;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.내_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.우아한테크코스_외부_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디_이름;
import static com.allog.dallog.common.fixtures.ExternalCategoryFixtures.대한민국_공휴일_생성_요청;
import static com.allog.dallog.common.fixtures.ExternalCategoryFixtures.대한민국_공휴일_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.OAuthFixtures.리버;
import static com.allog.dallog.common.fixtures.OAuthFixtures.파랑;
import static com.allog.dallog.common.fixtures.OAuthFixtures.후디;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_생성_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회식_생성_요청;
import static com.allog.dallog.domain.category.domain.CategoryType.GOOGLE;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.NONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.common.fixtures.CategoryFixtures;
import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryDetailResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.category.exception.ExistExternalCategoryException;
import com.allog.dallog.domain.category.exception.InvalidCategoryException;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.categoryrole.application.CategoryRoleService;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.dto.request.CategoryRoleUpdateRequest;
import com.allog.dallog.domain.categoryrole.exception.ManagingCategoryLimitExcessException;
import com.allog.dallog.domain.categoryrole.exception.NoCategoryAuthorityException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.schedule.application.ScheduleService;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionsResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class CategoryServiceTest extends ServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    @Autowired
    private CategoryRoleService categoryRoleService;

    @DisplayName("새로운 카테고리를 생성한다.")
    @Test
    void 새로운_카테고리를_생성한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        // when
        CategoryResponse response = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // then
        assertThat(response.getName()).isEqualTo(공통_일정_이름);
    }

    @DisplayName("새로운 개인 카테고리를 생성한다.")
    @Test
    void 새로운_개인_카테고리를_생성한다() {
        // given
        Member 후디 = memberRepository.save(후디());

        // when
        CategoryResponse 내_일정_응답 = categoryService.save(후디.getId(), 내_일정_생성_요청);
        Category 내_일정 = categoryRepository.getById(내_일정_응답.getId());

        // then
        assertAll(() -> {
            assertThat(내_일정.getName()).isEqualTo(CategoryFixtures.내_일정_이름);
            assertThat(내_일정.isPersonal()).isTrue();
        });
    }

    @DisplayName("카테고리 생성 시 자동으로 구독한다.")
    @Test
    void 카테고리_생성_시_자동으로_구독한다() {
        // given
        Long 파랑_id = toMemberId(파랑.getOAuthMember());

        // when
        categoryService.save(파랑_id, 공통_일정_생성_요청);

        SubscriptionsResponse subscriptions = subscriptionService.findByMemberId(파랑_id);
        List<SubscriptionResponse> actual = subscriptions.getSubscriptions();

        // then
        assertThat(actual).hasSize(2);
    }

    @Transactional
    @DisplayName("카테고리 생성 시 생성자에 대한 카테고리 역할을 ADMIN으로 생성한다.")
    @Test
    void 카테고리_생성_시_생성자에_대한_카테고리_역할을_ADMIN으로_생성한다() {
        // given
        Long 후디_id = toMemberId(후디.getOAuthMember());

        CategoryResponse JPA_스터디 = categoryService.save(후디_id, 후디_JPA_스터디_생성_요청);

        // when
        CategoryRole actual = categoryRoleRepository.getByMemberIdAndCategoryId(후디_id, JPA_스터디.getId());

        // then
        assertThat(actual.getCategoryRoleType()).isEqualTo(ADMIN);
    }

    @DisplayName("새로운 카테고리를 생성 할 떄 이름이 공백이거나 길이가 20을 초과하는 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "일이삼사오육칠팔구십일이삼사오육칠팔구십일", "알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 카테고리"})
    void 새로운_카테고리를_생성_할_때_이름이_공백이거나_길이가_20을_초과하는_경우_예외를_던진다(final String name) {
        // given
        CategoryCreateRequest request = new CategoryCreateRequest(name, NORMAL);
        Member 관리자 = memberRepository.save(관리자());

        // when & then
        assertThatThrownBy(() -> categoryService.save(관리자.getId(), request))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @DisplayName("새로운 외부 카테고리를 생성한다.")
    @Test
    void 새로운_외부_카테고리를_생성한다() {
        // given
        Member 후디 = memberRepository.save(후디());

        // when
        CategoryResponse 후디_대한민국_공휴일_카테고리_응답 = categoryService.save(후디.getId(), 대한민국_공휴일_생성_요청);
        Category 후디_대한민국_공휴일_카테고리 = categoryRepository.getById(후디_대한민국_공휴일_카테고리_응답.getId());

        // then
        assertAll(() -> {
            assertThat(후디_대한민국_공휴일_카테고리.getName()).isEqualTo(대한민국_공휴일_이름);
            assertThat(후디_대한민국_공휴일_카테고리.getCategoryType()).isEqualTo(GOOGLE);
        });
    }

    @DisplayName("중복된 외부 카테고리를 저장하는 경우 예외를 던진다.")
    @Test
    void 중복된_외부_카테고리를_저장하는_경우_예외를_던진다() {
        // given
        Member 후디 = memberRepository.save(후디());

        // when
        categoryService.save(후디.getId(), 대한민국_공휴일_생성_요청);

        // then
        assertThatThrownBy(() -> categoryService.save(후디.getId(), 대한민국_공휴일_생성_요청))
                .isInstanceOf(ExistExternalCategoryException.class);
    }


    @DisplayName("외부 카테고리 생성 시 자동으로 구독한다.")
    @Test
    void 외부_카테고리_생성_시_자동으로_구독한다() {
        // given
        Long 파랑_id = toMemberId(파랑.getOAuthMember());

        // when
        categoryService.save(파랑_id, 대한민국_공휴일_생성_요청);

        SubscriptionsResponse subscriptions = subscriptionService.findByMemberId(파랑_id);
        List<SubscriptionResponse> actual = subscriptions.getSubscriptions();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("검색어를 받아 제목에 검색어가 포함된 카테고리를 가져온다.")
    @Test
    void 검색어를_받아_제목에_검색어가_포함됨_카테고리를_가져온다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Long 관리자_ID = 관리자.getId();
        categoryService.save(관리자_ID, 공통_일정_생성_요청);
        categoryService.save(관리자_ID, BE_일정_생성_요청);
        categoryService.save(관리자_ID, FE_일정_생성_요청);
        categoryService.save(관리자_ID, 매트_아고라_생성_요청);
        categoryService.save(관리자_ID, 후디_JPA_스터디_생성_요청);

        // when
        CategoriesResponse response = categoryService.findNormalByName("일");

        // then
        assertThat(response.getCategories())
                .hasSize(3)
                .extracting(CategoryResponse::getName)
                .contains(공통_일정_이름, BE_일정_이름, FE_일정_이름);
    }

    @DisplayName("개인 카테고리는 전체 조회 대상에서 제외된다.")
    @Test
    void 개인_카테고리는_전체_조회_대상에서_제외된다() {
        // given
        authService.generateAccessAndRefreshToken(후디.getOAuthMember());
        authService.generateAccessAndRefreshToken(리버.getOAuthMember());

        // when
        CategoriesResponse response = categoryService.findNormalByName("");

        // then
        assertThat(response.getCategories()).hasSize(0);
    }

    @DisplayName("회원이 일정을 추가/수정/삭제할 수 있는 카테고리 목록을 조회한다.")
    @Test
    void 회원이_일정을_추가_수정_삭제할_수_있는_카테고리_목록을_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        categoryService.save(관리자.getId(), BE_일정_생성_요청);
        categoryService.save(관리자.getId(), FE_일정_생성_요청);

        Member 후디 = memberRepository.save(후디());
        CategoryResponse 매트_아고라 = categoryService.save(후디.getId(), 매트_아고라_생성_요청);
        CategoryResponse 후디_JPA_스터디 = categoryService.save(후디.getId(), 후디_JPA_스터디_생성_요청);

        subscriptionService.save(관리자.getId(), 매트_아고라.getId());
        subscriptionService.save(관리자.getId(), 후디_JPA_스터디.getId());

        categoryRoleService.updateRole(후디.getId(), 관리자.getId(), 매트_아고라.getId(), new CategoryRoleUpdateRequest(ADMIN));
        categoryRoleService.updateRole(후디.getId(), 관리자.getId(), 후디_JPA_스터디.getId(),
                new CategoryRoleUpdateRequest(ADMIN));

        // when
        CategoriesResponse actual = categoryService.findScheduleEditableCategories(관리자.getId());

        // then
        assertAll(() -> {
            assertThat(actual.getCategories().size()).isEqualTo(5);
            assertThat(actual.getCategories().stream().map(CategoryResponse::getName).collect(Collectors.toList()))
                    .containsExactly(공통_일정_이름, BE_일정_이름, FE_일정_이름, 매트_아고라_이름, 후디_JPA_스터디_이름);
        });
    }

    @DisplayName("회원이 ADMIN으로 있는 카테고리 목록을 조회한다.")
    @Test
    void 회원이_ADMIN으로_있는_카테고리_목록을_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        categoryService.save(관리자.getId(), BE_일정_생성_요청);
        categoryService.save(관리자.getId(), FE_일정_생성_요청);

        Member 후디 = memberRepository.save(후디());
        CategoryResponse 매트_아고라 = categoryService.save(후디.getId(), 매트_아고라_생성_요청);
        CategoryResponse 후디_JPA_스터디 = categoryService.save(후디.getId(), 후디_JPA_스터디_생성_요청);

        subscriptionService.save(관리자.getId(), 매트_아고라.getId());
        subscriptionService.save(관리자.getId(), 후디_JPA_스터디.getId());

        categoryRoleService.updateRole(후디.getId(), 관리자.getId(), 매트_아고라.getId(), new CategoryRoleUpdateRequest(ADMIN));
        categoryRoleService.updateRole(후디.getId(), 관리자.getId(), 후디_JPA_스터디.getId(),
                new CategoryRoleUpdateRequest(ADMIN));

        // when
        CategoriesResponse actual = categoryService.findAdminCategories(관리자.getId());

        // then
        assertAll(() -> {
            assertThat(actual.getCategories().size()).isEqualTo(5);
            assertThat(actual.getCategories().stream().map(CategoryResponse::getName).collect(Collectors.toList()))
                    .containsExactly(공통_일정_이름, BE_일정_이름, FE_일정_이름, 매트_아고라_이름, 후디_JPA_스터디_이름);
        });
    }

    @DisplayName("id를 통해 카테고리를 단건 조회한다.")
    @Test
    void id를_통해_카테고리를_단건_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // when & then
        CategoryDetailResponse 조회한_공통_일정 = categoryService.findDetailCategoryById(공통_일정.getId());

        assertAll(() -> {
            assertThat(조회한_공통_일정.getId()).isEqualTo(공통_일정.getId());
            assertThat(조회한_공통_일정.getName()).isEqualTo(공통_일정.getName());
        });
    }

    @DisplayName("id를 통해 카테고리를 단건 조회할 때 카테고리가 존재하지 않다면 예외가 발생한다.")
    @Test
    void id를_통해_카테고리를_단건_조회할_때_카테고리가_존재하지_않다면_예외가_발생한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // when & then
        assertThatThrownBy(() -> categoryService.findDetailCategoryById(공통_일정.getId() + 1))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("ADMIN 역할의 회원은 카테고리를 수정할 수 있다.")
    @Test
    void ADMIN_역할의_회원은_카테고리를_수정할_수_있다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        String 우테코_공통_일정_이름 = "우테코 공통 일정";
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(우테코_공통_일정_이름);

        // when
        categoryService.update(관리자.getId(), 공통_일정.getId(), categoryUpdateRequest);
        Category category = categoryRepository.getById(공통_일정.getId());

        //then
        assertThat(category.getName()).isEqualTo(우테코_공통_일정_이름);
    }

    @DisplayName("ADMIN 역할이 아닌 회원이 카테고리를 수정할 경우 예외를 던진다.")
    @Test
    void ADMIN_역할이_아닌_회원이_카테고리를_수정할_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        Member 매트 = memberRepository.save(매트());
        subscriptionService.save(매트.getId(), 공통_일정.getId());

        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest("우테코 공통 일정");

        // when & then
        assertThatThrownBy(
                () -> categoryService.update(매트.getId(), 공통_일정.getId(), categoryUpdateRequest))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("카테고리 생성자라도 역할이 ADMIN이 아닐 경우 카테고리 수정시 예외를 던진다.")
    @Test
    void 카테고리_생성자라도_역할이_ADMIN이_아닐_경우_카테고리_수정시_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        Member 매트 = memberRepository.save(매트());
        subscriptionService.save(매트.getId(), 공통_일정.getId());

        categoryRoleService.updateRole(관리자.getId(), 매트.getId(), 공통_일정.getId(), new CategoryRoleUpdateRequest(ADMIN));
        categoryRoleService.updateRole(매트.getId(), 관리자.getId(), 공통_일정.getId(), new CategoryRoleUpdateRequest(NONE));

        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest("우테코 공통 일정");

        // when & then
        assertThatThrownBy(
                () -> categoryService.update(관리자.getId(), 공통_일정.getId(), categoryUpdateRequest))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("존재하지 않는 카테고리를 수정할 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_카테고리를_수정할_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(BE_일정_이름);

        // when & then
        assertThatThrownBy(() -> categoryService.update(관리자.getId(), 공통_일정.getId() + 1, categoryUpdateRequest))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("ADMIN인 회원이 카테고리를 삭제한다.")
    @Test
    void ADMIN인_회원이_카테고리를_삭제한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        Member 후디 = memberRepository.save(후디());
        subscriptionService.save(후디.getId(), 공통_일정.getId());
        categoryRoleService.updateRole(관리자.getId(), 후디.getId(), 공통_일정.getId(), new CategoryRoleUpdateRequest(ADMIN));

        // when
        categoryService.delete(후디.getId(), 공통_일정.getId());

        //then
        assertThatThrownBy(() -> categoryRepository.getById(공통_일정.getId()))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("ADMIN 역할이 아닌 회원이 카테고리를 삭제할 경우 예외를 던진다.")
    @Test
    void ADMIN_역할이_아닌_회원이_카테고리를_삭제할_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        Member 매트 = memberRepository.save(매트());
        subscriptionService.save(매트.getId(), 공통_일정.getId());

        // when & then
        assertThatThrownBy(
                () -> categoryService.delete(매트.getId(), 공통_일정.getId()))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("카테고리 생성자라도 역할이 ADMIN이 아닐 경우 카테고리 삭제시 예외를 던진다.")
    @Test
    void 카테고리_생성자라도_역할이_ADMIN이_아닐_경우_카테고리_삭제시_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        Member 매트 = memberRepository.save(매트());
        subscriptionService.save(매트.getId(), 공통_일정.getId());

        categoryRoleService.updateRole(관리자.getId(), 매트.getId(), 공통_일정.getId(), new CategoryRoleUpdateRequest(ADMIN));
        categoryRoleService.updateRole(매트.getId(), 관리자.getId(), 공통_일정.getId(), new CategoryRoleUpdateRequest(NONE));

        // when & then
        assertThatThrownBy(
                () -> categoryService.delete(관리자.getId(), 공통_일정.getId()))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("카테고리를 생성 시 이미 관리자로 참여중인 카테고리 개수가 50개 이상이면 예외를 던진다.")
    @Test
    void 카테고리를_생성_시_이미_관리자로_참여중인_카테고리_개수가_50개_이상이면_예외를_던진다() {
        // given
        Member 후디 = memberRepository.save(후디());
        for (int i = 0; i < 50; i++) {
            CategoryCreateRequest request = new CategoryCreateRequest("카테고리 " + i, NORMAL);
            categoryService.save(후디.getId(), request);
        }

        // when & then
        assertThatThrownBy(() -> categoryService.save(후디.getId(), BE_일정_생성_요청))
                .isInstanceOf(ManagingCategoryLimitExcessException.class);
    }

    @DisplayName("존재하지 않는 카테고리를 삭제할 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_카테고리를_삭제할_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // when & then
        assertThatThrownBy(() -> categoryService.delete(관리자.getId(), 공통_일정.getId() + 1))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("카테고리 삭제 시 연관된 일정 엔티티도 모두 제거된다.")
    @Test
    void 카테고리_삭제_시_연관된_일정_엔티티도_모두_제거된다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        ScheduleResponse 알록달록_회식 = scheduleService.save(관리자.getId(), 공통_일정.getId(), 알록달록_회식_생성_요청);
        ScheduleResponse 레벨_인터뷰 = scheduleService.save(관리자.getId(), 공통_일정.getId(), 레벨_인터뷰_생성_요청);

        // when
        categoryService.delete(관리자.getId(), 공통_일정.getId());

        // then
        assertAll(() -> {
            assertThatThrownBy(() -> scheduleService.findById(알록달록_회식.getId()))
                    .isInstanceOf(NoSuchScheduleException.class);
            assertThatThrownBy(() -> scheduleService.findById(레벨_인터뷰.getId()))
                    .isInstanceOf(NoSuchScheduleException.class);
        });
    }

    @DisplayName("카테고리 삭제 시 연관된 구독 엔티티도 모두 제거된다.")
    @Test
    void 카테고리_삭제_시_연관된_구독_엔티티도_모두_제거된다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        Member 후디 = memberRepository.save(후디());
        SubscriptionResponse 구독 = subscriptionService.save(후디.getId(), 공통_일정.getId());

        // when
        categoryService.delete(관리자.getId(), 공통_일정.getId());

        // then
        assertThat(subscriptionRepository.existsById(구독.getId())).isFalse();
    }

    @Transactional
    @DisplayName("카테고리 삭제 시 연관된 카테고리 역할 엔티티도 모두 제거된다.")
    @Test
    void 카테고리_삭제_시_연관된_카테고리_역할_엔티티도_모두_제거된다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        Member 후디 = memberRepository.save(후디());
        subscriptionService.save(후디.getId(), 공통_일정.getId());

        CategoryRole 역할 = categoryRoleRepository.getByMemberIdAndCategoryId(후디.getId(), 공통_일정.getId());

        // when
        categoryService.delete(관리자.getId(), 공통_일정.getId());
        boolean actual = categoryRoleRepository.findById(역할.getId()).isPresent();

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("개인 카테고리는 삭제할 수 없다.")
    @Test
    void 개인_카테고리는_삭제할_수_없다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 내_일정 = categoryService.save(관리자.getId(), 내_일정_생성_요청);

        // when & then
        assertThatThrownBy(() -> categoryService.delete(관리자.getId(), 내_일정.getId()))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @DisplayName("외부 캘린더의 카테고리를 삭제한다.")
    @Test
    void 외부_캘린더의_카테고리를_삭제한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 우아한테크코스_외부_일정 = categoryService.save(관리자.getId(), 우아한테크코스_외부_일정_생성_요청);

        // when
        categoryService.delete(관리자.getId(), 우아한테크코스_외부_일정.getId());

        // then
        assertThatThrownBy(() -> categoryService.findDetailCategoryById(우아한테크코스_외부_일정.getId()))
                .isInstanceOf(NoSuchCategoryException.class);
    }
}

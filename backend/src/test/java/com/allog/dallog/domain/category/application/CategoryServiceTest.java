package com.allog.dallog.domain.category.application;

import static com.allog.dallog.common.Constants.개인_카테고리_이름;
import static com.allog.dallog.common.Constants.네오_이름;
import static com.allog.dallog.common.Constants.네오_이메일;
import static com.allog.dallog.common.Constants.네오_프로필_URL;
import static com.allog.dallog.common.Constants.스터디_카테고리_이름;
import static com.allog.dallog.common.Constants.외부_카테고리_ID;
import static com.allog.dallog.common.Constants.외부_카테고리_이름;
import static com.allog.dallog.common.Constants.제이슨_이름;
import static com.allog.dallog.common.Constants.제이슨_이메일;
import static com.allog.dallog.common.Constants.제이슨_프로필_URL;
import static com.allog.dallog.common.Constants.취업_일정_메모;
import static com.allog.dallog.common.Constants.취업_일정_시작일;
import static com.allog.dallog.common.Constants.취업_일정_제목;
import static com.allog.dallog.common.Constants.취업_일정_종료일;
import static com.allog.dallog.common.Constants.취업_카테고리_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.domain.category.domain.CategoryType.GOOGLE;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.category.domain.CategoryType.PERSONAL;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.NONE;
import static com.allog.dallog.domain.subscription.domain.Color.COLOR_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.auth.event.MemberSavedEvent;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.CategoryType;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.domain.category.dto.request.ExternalCategoryCreateRequest;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryDetailResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.category.exception.ExistExternalCategoryException;
import com.allog.dallog.domain.category.exception.InvalidCategoryException;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.exception.ManagingCategoryLimitExcessException;
import com.allog.dallog.domain.categoryrole.exception.NoCategoryAuthorityException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.domain.SocialType;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class CategoryServiceTest extends ServiceTest {

    private final CategoryCreateRequest 취업_카테고리_생성_요청 = new CategoryCreateRequest(취업_카테고리_이름, NORMAL);
    private final CategoryCreateRequest 개인_카테고리_생성_요청 = new CategoryCreateRequest(개인_카테고리_이름, PERSONAL);
    private final ExternalCategoryCreateRequest 외부_카테고리_생성_요청 = new ExternalCategoryCreateRequest(외부_카테고리_ID,
            외부_카테고리_이름);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private BusinessBuilder 네오;
    private BusinessBuilder 제이슨;

    @BeforeEach
    void setUp() {
        네오 = new BusinessBuilder();
        제이슨 = new BusinessBuilder();
    }

    @DisplayName("카테고리를 등록한다.")
    @Test
    void 카테고리를_등록한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        // when
        CategoryResponse actual = categoryService.save(네오.회원().getId(), 취업_카테고리_생성_요청);

        // then
        assertThat(actual.getName()).isEqualTo(취업_카테고리_이름);
    }

    @DisplayName("PERSONAl 카테고리를 등록한다.")
    @Test
    void PERSONAL_카테고리를_등록한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        // when
        CategoryResponse 개인_카테고리_응답 = categoryService.save(네오.회원().getId(), 개인_카테고리_생성_요청);

        // then
        Category 개인_카테고리 = categoryRepository.findById(개인_카테고리_응답.getId()).get();

        assertAll(() -> {
            assertThat(개인_카테고리.getName()).isEqualTo(개인_카테고리_이름);
            assertThat(개인_카테고리.isPersonal()).isTrue();
        });
    }

    @DisplayName("카테고리를 등록할 때 자동으로 구독한다.")
    @Test
    void 카테고리를_등록할_때_자동으로_구독한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        // when
        categoryService.save(네오.회원().getId(), 취업_카테고리_생성_요청);

        // then
        List<Subscription> 구독_정보들 = subscriptionRepository.findByMemberId(네오.회원().getId());
        assertThat(구독_정보들).hasSize(1);
    }

    @Transactional
    @DisplayName("카테고리 등록할 때 권한을 ADMIN으로 등록한다.")
    @Test
    void 카테고리를_등록할_때_권한을_ADMIN으로_등록한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        // when
        CategoryResponse 응답 = categoryService.save(네오.회원().getId(), 취업_카테고리_생성_요청);

        // then
        CategoryRole 카테고리_권한 = categoryRoleRepository.getByMemberIdAndCategoryId(네오.회원().getId(), 응답.getId());
        assertThat(카테고리_권한.getCategoryRoleType()).isEqualTo(ADMIN);
    }

    @DisplayName("카테고리를 등록할 떄 이름이 공백이거나 길이가 20을 초과하면 예외를 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "일이삼사오육칠팔구십일이삼사오육칠팔구십일", "알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 카테고리"})
    void 카테고리를_등록할_때_이름이_공백이거나_길이가_20을_초과하면_예외를_발생한다(final String invalidName) {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        CategoryCreateRequest 카테고리_생성_요청 = new CategoryCreateRequest(invalidName, NORMAL);

        // when & then
        assertThatThrownBy(() -> categoryService.save(네오.회원().getId(), 카테고리_생성_요청))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @DisplayName("외부 카테고리를 등록한다.")
    @Test
    void 외부_카테고리를_등록한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        // when
        CategoryResponse 응답 = categoryService.save(네오.회원().getId(), 외부_카테고리_생성_요청);

        // then
        Category 외부_카테고리 = categoryRepository.findById(응답.getId()).get();
        assertAll(() -> {
            assertThat(외부_카테고리.getName()).isEqualTo(외부_카테고리_이름);
            assertThat(외부_카테고리.getCategoryType()).isEqualTo(GOOGLE);
        });
    }

    @DisplayName("중복되는 외부 카테고리를 등록하면 예외를 발생한다.")
    @Test
    void 중복되는_외부_카테고리를_등록하면_예외를_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        categoryService.save(네오.회원().getId(), 외부_카테고리_생성_요청);

        // when & then
        assertThatThrownBy(() -> categoryService.save(네오.회원().getId(), 외부_카테고리_생성_요청))
                .isInstanceOf(ExistExternalCategoryException.class);
    }

    @DisplayName("외부 카테고리를 등록할 때 자동으로 구독한다.")
    @Test
    void 외부_카테고리를_등록할_때_자동으로_구독한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        // when
        categoryService.save(네오.회원().getId(), 외부_카테고리_생성_요청);

        // then
        List<Subscription> 구독_정보들 = subscriptionRepository.findByMemberId(네오.회원().getId());
        assertThat(구독_정보들).hasSize(1);
    }

    @DisplayName("저장된 회원의 개인 카테고리를 생성하고 자동으로 구독하고 카테고리 역할을 부여한다.")
    @Test
    void 저장된_회원의_개인_카테고리를_생성하고_자동으로_구독하고_카테고리_역할을_부여한다() {
        // given
        Member 파랑 = memberRepository.save(파랑());
        MemberSavedEvent event = new MemberSavedEvent(파랑.getId());

        // when
        categoryService.savePersonalCategory(event);

        // then
        List<Category> categories = categoryRepository.findByMemberId(파랑.getId());
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(파랑.getId());
        List<CategoryRole> categoryRoles = categoryRoleRepository.findByMemberId(파랑.getId());

        assertAll(() -> {
            assertThat(categories).hasSize(1)
                    .extracting("categoryType")
                    .containsExactly(CategoryType.PERSONAL);
            assertThat(subscriptions).hasSize(1)
                    .extracting("checked")
                    .containsExactly(true);
            assertThat(categoryRoles).hasSize(1)
                    .extracting("categoryRoleType")
                    .containsExactly(ADMIN);
        });
    }

    @DisplayName("검색어가 제목에 있는 카테고리를 가져온다.")
    @Test
    void 검색어가_제목에_있는_카테고리를_가져온다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(외부_카테고리_이름, GOOGLE)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리를_등록한다(스터디_카테고리_이름, NORMAL);

        // when
        CategoriesResponse actual = categoryService.findNormalByName("취업");

        // then
        assertThat(actual.getCategories()).hasSize(1);
    }

    @DisplayName("검색어가 제목에 있는 카테고리를 가져올때 개인 카테고리는 제외한다.")
    @Test
    void 검색어가_제목에_있는_카테고리를_가져올때_개인_카테고리는_제외한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(개인_카테고리_이름, PERSONAL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리를_등록한다(스터디_카테고리_이름, NORMAL);

        // when
        CategoriesResponse actual = categoryService.findNormalByName("");

        // then
        assertThat(actual.getCategories()).hasSize(2);
    }

    @DisplayName("관리권한이 ADMIN인 카테고리 목록을 조회한다.")
    @Test
    void 관리권한이_ADMIN인_카테고리_목록을_조회한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(스터디_카테고리_이름, NORMAL);

        제이슨.회원_가입을_한다(제이슨_이메일, 제이슨_이름, 제이슨_프로필_URL)
                .카테고리를_등록한다(스터디_카테고리_이름, NORMAL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리를_구독한다(네오.카테고리());

        네오.내_카테고리_관리_권한을_부여한다(제이슨.회원());

        // when
        CategoriesResponse actual = categoryService.findScheduleEditableCategories(제이슨.회원().getId());

        // then
        assertThat(actual.getCategories().size()).isEqualTo(3);
    }

    @DisplayName("id로 카테고리 단건 조회한다.")
    @Test
    void id로_카테고리_단건_조회한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        // when
        CategoryDetailResponse actual = categoryService.findDetailCategoryById(네오.카테고리().getId());

        // then
        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(네오.카테고리().getId());
            assertThat(actual.getName()).isEqualTo(네오.카테고리().getName());
        });
    }

    @DisplayName("id로 카테고리 단건 조회할 때 없으면 예외를 발생한다.")
    @Test
    void id로_카테고리_단건_조회할_때_없으면_예외를_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        // when & then
        assertThatThrownBy(() -> categoryService.findDetailCategoryById(네오.카테고리().getId() + 1))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("권한이 ADMIN인 카테고리를 수정한다.")
    @Test
    void 권한이_ADMIN인_카테고리를_수정한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        CategoryUpdateRequest 카테고리_수정_요청 = new CategoryUpdateRequest("새로운 취업 카테고리 이름");

        // when
        categoryService.update(네오.회원().getId(), 네오.카테고리().getId(), 카테고리_수정_요청);

        //then
        Category 취업_카테고리 = categoryRepository.getById(네오.카테고리().getId());

        assertThat(취업_카테고리.getName()).isEqualTo("새로운 취업 카테고리 이름");
    }

    @DisplayName("권한이 ADMIN이 아닌 카테고리를 수정하면 예외를 발생한다.")
    @Test
    void 권한이_ADMIN이_아닌_카테고리를_수정하면_예외를_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        제이슨.회원_가입을_한다(제이슨_이메일, 제이슨_이름, 제이슨_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        CategoryUpdateRequest 카테고리_수정_요청 = new CategoryUpdateRequest("새로운 취업 카테고리 이름");

        // when & then
        assertThatThrownBy(() -> categoryService.update(제이슨.회원().getId(), 네오.카테고리().getId(), 카테고리_수정_요청))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("카테고리를 수정할 때 카테고리가 없으면 예외를 발생한다.")
    @Test
    void 카테고리를_수정할_때_카테고리가_없으면_예외를_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        CategoryUpdateRequest 카테고리_수정_요청 = new CategoryUpdateRequest("새로운 취업 카테고리 이름");

        // when & then
        assertThatThrownBy(() -> categoryService.update(네오.회원().getId(), -1L, 카테고리_수정_요청))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("권한이 ADMIN인 카테고리를 삭제한다.")
    @Test
    void 권한이_ADMIN인_카테고리를_삭제한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        // when
        categoryService.delete(네오.회원().getId(), 네오.카테고리().getId());

        //then
        assertThatThrownBy(() -> categoryRepository.getById(네오.카테고리().getId()))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("권한이 ADMIN이 아닌 카테고리를 삭제하려 하면 예외를 발생한다.")
    @Test
    void 권한이_ADMIN이_아닌_카테고리를_삭제하려_하면_예외를_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        제이슨.회원_가입을_한다(제이슨_이메일, 제이슨_이름, 제이슨_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        // when & then
        assertThatThrownBy(() -> categoryService.delete(제이슨.회원().getId(), 네오.카테고리().getId()))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("카테고리를 등록할 때 ADMIN인 카테고리가 50개면 예외를 발생한다.")
    @Test
    void 카테고리를_등록할_때_ADMIN인_카테고리가_50개면_예외를_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);
        for (int i = 0; i < 50; i++) {
            네오.카테고리를_등록한다("카테고리 " + i, NORMAL);
        }

        // when & then
        assertThatThrownBy(() -> categoryService.save(네오.회원().getId(), BE_일정_생성_요청))
                .isInstanceOf(ManagingCategoryLimitExcessException.class);
    }

    @DisplayName("없는 카테고리를 삭제하려 하면 예외를 발생한다.")
    @Test
    void 없는_카테고리를_삭제하려_하면_예외를_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        // when & then
        assertThatThrownBy(() -> categoryService.delete(네오.회원().getId(), -1L))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("카테고리를 삭제할 때 등록한 일정도 모두 삭제한다.")
    @Test
    void 카테고리를_삭제할_때_등록한_일정도_모두_삭제한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // when
        categoryService.delete(네오.회원().getId(), 네오.카테고리().getId());

        // then
        assertAll(() -> {
            assertThatThrownBy(() -> scheduleRepository.getById(네오.카테고리_일정().getId()))
                    .isInstanceOf(NoSuchScheduleException.class);
        });
    }

    @DisplayName("카테고리를 삭제할 때 구독 정보도 모두 삭제한다.")
    @Test
    void 카테고리를_삭제할_때_구독_정보도_모두_삭제한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        제이슨.회원_가입을_한다(제이슨_이메일, 제이슨_이름, 제이슨_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        // when
        categoryService.delete(네오.회원().getId(), 네오.카테고리().getId());

        // then
        assertThatThrownBy(() -> subscriptionRepository.getById(제이슨.구독().getId()))
                .isInstanceOf(NoSuchSubscriptionException.class);
    }

    @DisplayName("카테고리를 삭제할 때 카테고리 권한도 모두 삭제한다.")
    @Test
    void 카테고리를_삭제할_때_카테고리_권한도_모두_삭제한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        CategoryRole 권한 = categoryRoleRepository.getByMemberIdAndCategoryId(네오.회원().getId(),
                네오.카테고리().getId());

        // when
        categoryService.delete(네오.회원().getId(), 네오.카테고리().getId());

        // then
        boolean actual = categoryRoleRepository.findById(권한.getId()).isPresent();
        assertThat(actual).isFalse();
    }

    @DisplayName("PERSONAL 카테고리는 삭제할 수 없다.")
    @Test
    void PERSONAL_카테고리는_삭제할_수_없다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(개인_카테고리_이름, PERSONAL);

        // when & then
        assertThatThrownBy(() -> categoryService.delete(네오.회원().getId(), 네오.카테고리().getId()))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @DisplayName("외부 서비스 연동 카테고리를 삭제한다.")
    @Test
    void 외부_서비스_연동_카테고리를_삭제한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(외부_카테고리_이름, GOOGLE);

        // when
        categoryService.delete(네오.회원().getId(), 네오.카테고리().getId());

        // then
        assertThatThrownBy(() -> categoryService.findDetailCategoryById(네오.카테고리().getId()))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    private final class BusinessBuilder {

        private Member member;
        private Category category;
        private CategoryRole categoryRole;
        private Subscription subscription;
        private Schedule schedule;

        private BusinessBuilder 회원_가입을_한다(final String email, final String name, final String profile) {
            Member member = new Member(email, name, profile, SocialType.GOOGLE);
            this.member = memberRepository.save(member);
            return this;
        }

        private BusinessBuilder 카테고리를_등록한다(final String categoryName, final CategoryType categoryType) {
            Category category = new Category(categoryName, this.member, categoryType);
            CategoryRole categoryRole = new CategoryRole(category, this.member, ADMIN);
            Subscription subscription = new Subscription(this.member, category, COLOR_1);
            this.category = categoryRepository.save(category);
            this.categoryRole = categoryRoleRepository.save(categoryRole);
            this.subscription = subscriptionRepository.save(subscription);
            return this;
        }

        private BusinessBuilder 카테고리를_구독한다(final Category category) {
            Subscription subscription = new Subscription(this.member, category, COLOR_1);
            CategoryRole categoryRole = new CategoryRole(category, this.member, NONE);
            this.subscription = subscriptionRepository.save(subscription);
            this.categoryRole = categoryRoleRepository.save(categoryRole);
            return this;
        }

        private BusinessBuilder 내_카테고리_관리_권한을_부여한다(final Member otherMember) {
            CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(otherMember.getId(),
                    category.getId());
            categoryRole.changeRole(ADMIN);
            categoryRoleRepository.save(categoryRole);
            return this;
        }

        private BusinessBuilder 일정을_등록한다(final String title, final LocalDateTime start, final LocalDateTime end,
                                         final String memo) {
            Schedule schedule = new Schedule(this.category, title, start, end, memo);
            this.schedule = scheduleRepository.save(schedule);
            return this;
        }

        private Member 회원() {
            return member;
        }

        private Category 카테고리() {
            return category;
        }

        private Subscription 구독() {
            return subscription;
        }

        private Schedule 카테고리_일정() {
            return schedule;
        }
    }
}

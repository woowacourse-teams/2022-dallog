package com.allog.dallog.domain.schedule.application;

import static com.allog.dallog.common.Constants.날짜_2022년_7월_10일_0시_0분;
import static com.allog.dallog.common.Constants.날짜_2022년_7월_11일_0시_0분;
import static com.allog.dallog.common.Constants.날짜_2022년_7월_16일_16시_0분;
import static com.allog.dallog.common.Constants.날짜_2022년_7월_16일_18시_0분;
import static com.allog.dallog.common.Constants.날짜_2022년_7월_16일_20시_0분;
import static com.allog.dallog.common.Constants.날짜_2022년_7월_1일_0시_0분;
import static com.allog.dallog.common.Constants.날짜_2022년_7월_20일_0시_0분;
import static com.allog.dallog.common.Constants.날짜_2022년_7월_21일_0시_0분;
import static com.allog.dallog.common.Constants.날짜_2022년_7월_31일_0시_0분;
import static com.allog.dallog.common.Constants.날짜_2022년_8월_15일_14시_0분;
import static com.allog.dallog.common.Constants.날짜_2022년_8월_15일_17시_0분;
import static com.allog.dallog.common.Constants.네오_이름;
import static com.allog.dallog.common.Constants.네오_이메일;
import static com.allog.dallog.common.Constants.네오_프로필_URL;
import static com.allog.dallog.common.Constants.박람회_카테고리_이름;
import static com.allog.dallog.common.Constants.취업_일정_메모;
import static com.allog.dallog.common.Constants.취업_일정_시작일;
import static com.allog.dallog.common.Constants.취업_일정_제목;
import static com.allog.dallog.common.Constants.취업_일정_종료일;
import static com.allog.dallog.common.Constants.취업_카테고리_이름;
import static com.allog.dallog.common.Constants.포비_이름;
import static com.allog.dallog.common.Constants.포비_이메일;
import static com.allog.dallog.common.Constants.포비_프로필_URL;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_제목;
import static com.allog.dallog.domain.category.domain.CategoryType.GOOGLE;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.NONE;
import static com.allog.dallog.domain.subscription.domain.Color.COLOR_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.CategoryType;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.exception.NoCategoryAuthorityException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.domain.SocialType;
import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.allog.dallog.domain.schedule.dto.response.IntegrationScheduleResponses;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.schedule.exception.InvalidScheduleException;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ScheduleServiceTest extends ServiceTest {

    private final ScheduleCreateRequest 취업_일정_생성_요청 = new ScheduleCreateRequest(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일,
            취업_일정_메모);
    private final DateRangeRequest 구간_일정_조회_요청 = new DateRangeRequest("2022-07-01T00:00", "2022-08-15T23:59");

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    private User 네오;
    private User 포비;

    @BeforeEach
    void setUp() {
        네오 = new User();
        포비 = new User();
    }

    @DisplayName("ADMIN 역할의 회원은 카테고리에 새로운 일정을 생성할 수 있다.")
    @Test
    void ADMIN_역할의_회원은_카테고리에_새로운_일정을_생성할_수_있다() {
        // given & when
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리에_일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // then
        assertThat(네오.일정().getTitle()).isEqualTo(취업_일정_제목);
    }

    @DisplayName("ADMIN 역할이 아닌 회원이 카테고리에 새로운 일정을 생성할 시 예외가 발생한다.")
    @Test
    void ADMIN_역할이_아닌_회원이_카테고리에_새로운_일정을_생성할_시_예외가_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리에_일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        // when & then
        assertThatThrownBy(() -> scheduleService.save(포비.계정().getId(), 네오.카테고리().getId(), 취업_일정_생성_요청))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("카테고리 생성자라도 ADMIN 역할이 아니라면 새로운 일정을 생성할 시 예외가 발생한다.")
    @Test
    void 카테고리_생성자라도_ADMIN_역할이_아니라면_새로운_일정을_생성할_시_예외가_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        네오.카테고리_관리_권한을_부여한다(포비.계정(), 네오.카테고리());
        포비.카테고리_관리_권한을_해제한다(네오.계정(), 네오.카테고리());

        // when & then
        assertThatThrownBy(() -> scheduleService.save(네오.계정().getId(), 네오.카테고리().getId(), 취업_일정_생성_요청))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("새로운 일정을 생성 할 떄 일정 제목의 길이가 50을 초과하는 경우 예외를 던진다.")
    @Test
    void 새로운_일정을_생성_할_때_일정_제목의_길이가_50을_초과하는_경우_예외를_던진다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        String 잘못된_일정_제목 = "일이삼사오육칠팔구십일이삼사오육칠팔구십일일이삼사오육칠팔구십일이삼사오육칠팔구십일일이삼사오육칠팔구십일";
        ScheduleCreateRequest 잘못된_일정_생성_요청 = new ScheduleCreateRequest(잘못된_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(네오.계정().getId(), 네오.카테고리().getId(), 잘못된_일정_생성_요청)).
                isInstanceOf(InvalidScheduleException.class);
    }

    @DisplayName("새로운 일정을 생성 할 떄 일정 메모의 길이가 255를 초과하는 경우 예외를 던진다.")
    @Test
    void 새로운_일정을_생성_할_때_일정_메모의_길이가_255를_초과하는_경우_예외를_던진다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        String 잘못된_일정_메모 = "1".repeat(256);
        ScheduleCreateRequest 잘못된_일정_생성_요청 = new ScheduleCreateRequest(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 잘못된_일정_메모);
        // when & then
        assertThatThrownBy(() -> scheduleService.save(네오.계정().getId(), 네오.카테고리().getId(), 잘못된_일정_생성_요청)).
                isInstanceOf(InvalidScheduleException.class);
    }

    @DisplayName("새로운 일정을 생성 할 떄 종료일시가 시작일시 이전이라면 예외를 던진다.")
    @Test
    void 새로운_일정을_생성_할_때_종료일시가_시작일시_이전이라면_예외를_던진다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        ScheduleCreateRequest 잘못된_일정_생성_요청 = new ScheduleCreateRequest(알록달록_회의_제목, 취업_일정_종료일, 취업_일정_시작일, 알록달록_회의_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(네오.계정().getId(), 네오.카테고리().getId(), 잘못된_일정_생성_요청)).
                isInstanceOf(InvalidScheduleException.class);
    }

    @DisplayName("일정 생성시 전달한 카테고리가 존재하지 않는다면 예외를 던진다.")
    @Test
    void 일정_생성시_전달한_카테고리가_존재하지_않는다면_예외를_던진다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(네오.계정().getId(), 0L, 취업_일정_생성_요청)).
                isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("일정 생성시 전달한 카테고리가 외부 연동 카테고리라면 예외를 던진다.")
    @Test
    void 일정_생성시_전달한_카테고리가_외부_연동_카테고리라면_예외를_던진다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, GOOGLE);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(네오.계정().getId(), 네오.카테고리().getId(), 취업_일정_생성_요청)).
                isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("일정의 ID로 단건 일정을 조회한다.")
    @Test
    void 일정의_ID로_단건_일정을_조회한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리에_일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // when
        ScheduleResponse actual = scheduleService.findById(네오.일정().getId());

        // then
        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(네오.일정().getId());
            assertThat(actual.getTitle()).isEqualTo(취업_일정_제목);
            assertThat(actual.getStartDateTime()).isEqualTo(취업_일정_시작일);
            assertThat(actual.getEndDateTime()).isEqualTo(취업_일정_종료일);
            assertThat(actual.getMemo()).isEqualTo(취업_일정_메모);
        });
    }

    @DisplayName("존재하지 않는 일정 ID로 단건 일정을 조회하면 예외를 던진다.")
    @Test
    void 존재하지_않는_일정_ID로_단건_일정을_조회하면_예외를_던진다() {
        // given
        Long 잘못된_아이디 = 0L;

        // when & then
        assertThatThrownBy(() -> scheduleService.findById(잘못된_아이디))
                .isInstanceOf(NoSuchScheduleException.class);
    }

    @DisplayName("월별 일정 조회 시, 통합일정 정보를 반환한다.")
    @Test
    void 월별_일정_조회_시_통합일정_정보를_반환한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리에_일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // when
        List<IntegrationSchedule> schedules = scheduleService.findInternalByMemberIdAndDateRange(네오.계정().getId(),
                구간_일정_조회_요청).getSchedules();

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("카테고리 별 통합 일정 정보를 조회한다.")
    @Test
    void 카테고리_별_통합_일정_정보를_조회한다() {
        // given
        /* 장기간 일정 엣지 케이스 */
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리에_일정을_등록한다(취업_일정_제목, 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_7월_31일_0시_0분, 취업_일정_메모)
                .카테고리에_일정을_등록한다(취업_일정_제목, 날짜_2022년_7월_1일_0시_0분, 날짜_2022년_8월_15일_14시_0분, 취업_일정_메모)
                .카테고리에_일정을_등록한다(취업_일정_제목, 날짜_2022년_7월_31일_0시_0분, 날짜_2022년_8월_15일_17시_0분, 취업_일정_메모);

        /* 종일 일정 */
        네오.카테고리에_일정을_등록한다(취업_일정_제목, 날짜_2022년_7월_10일_0시_0분, 날짜_2022년_7월_11일_0시_0분, 취업_일정_메모);
        네오.카테고리에_일정을_등록한다(취업_일정_제목, 날짜_2022년_7월_20일_0시_0분, 날짜_2022년_7월_21일_0시_0분, 취업_일정_메모);

        /* 몇시간 일정 */
        네오.카테고리에_일정을_등록한다(취업_일정_제목, 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_20시_0분, 취업_일정_메모);
        네오.카테고리에_일정을_등록한다(취업_일정_제목, 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_18시_0분, 취업_일정_메모);

        // when
        IntegrationScheduleResponses actual = scheduleService.findByCategoryIdAndDateRange(네오.카테고리().getId(),
                구간_일정_조회_요청);

        // then
        assertAll(() -> {
            assertThat(actual.getLongTerms()).hasSize(3);
            assertThat(actual.getAllDays()).hasSize(2);
            assertThat(actual.getFewHours()).hasSize(2);
        });
    }

    @DisplayName("일정을 수정한다.")
    @Test
    void 일정을_수정한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리에_일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // when

        scheduleService.update(네오.일정().getId(), 네오.계정().getId(), 취업_일정_수정_요청(네오.카테고리()));

        // then
        Schedule actual = scheduleRepository.getById(네오.일정().getId());
        assertAll(
                () -> {
                    assertThat(actual.getId()).isEqualTo(네오.일정().getId());
                    assertThat(actual.getTitle()).isEqualTo("박람회 일정 제목");
                    assertThat(actual.getStartDateTime()).isEqualTo(취업_일정_시작일);
                    assertThat(actual.getEndDateTime()).isEqualTo(취업_일정_종료일);
                    assertThat(actual.getMemo()).isEqualTo(취업_일정_메모);
                }
        );
    }

    @DisplayName("ADMIN 역할이 아닌 회원이 카테고리에 새로운 일정을 수정할 시 예외가 발생한다.")
    @Test
    void ADMIN_역할이_아닌_회원이_카테고리에_새로운_일정을_수정할_시_예외가_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, GOOGLE)
                .카테고리에_일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        // when & then
        assertThatThrownBy(() -> scheduleService.update(네오.일정().getId(), 포비.계정().getId(), 취업_일정_수정_요청(네오.카테고리())))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("카테고리 생성자라도 ADMIN 역할이 아니라면 새로운 일정을 수정할 시 예외가 발생한다.")
    @Test
    void 카테고리_생성자라도_ADMIN_역할이_아니라면_새로운_일정을_수정할_시_예외가_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리에_일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        네오.카테고리_관리_권한을_부여한다(포비.계정(), 네오.카테고리());
        포비.카테고리_관리_권한을_해제한다(네오.계정(), 네오.카테고리());

        Schedule 마지막_등록_일정 = 네오.일정();

        // when & then
        assertThatThrownBy(() -> scheduleService.update(마지막_등록_일정.getId(), 네오.계정().getId(), 취업_일정_수정_요청(네오.카테고리())))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("일정 수정 시 존재하지 않은 일정일 경우 예외가 발생한다.")
    @Test
    void 일정_수정_시_존재하지_않은_일정일_경우_예외가_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        // when & then
        assertThatThrownBy(() -> scheduleService.update(1L, 네오.계정().getId(), 취업_일정_수정_요청(네오.카테고리())))
                .isInstanceOf(NoSuchScheduleException.class);
    }

    @DisplayName("일정의 카테고리도 수정한다.")
    @Test
    void 일정의_카테고리도_수정한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리에_일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        Schedule 마지막_등록_일정 = 네오.일정();

        Category 박람회_카테고리 = 네오.카테고리를_등록한다(박람회_카테고리_이름, NORMAL).카테고리();

        ScheduleUpdateRequest 일정_수정_요청 = new ScheduleUpdateRequest(박람회_카테고리.getId(), 취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일,
                취업_일정_메모);

        // when
        scheduleService.update(마지막_등록_일정.getId(), 네오.계정().getId(), 일정_수정_요청);

        // then
        Schedule actual = scheduleRepository.getById(마지막_등록_일정.getId());

        assertThat(actual.getCategory().getId()).isEqualTo(박람회_카테고리.getId());
    }

    @DisplayName("ADMIN 역할의 회원은 카테고리의 일정을 삭제할 수 있다.")
    @Test
    void AMDIN_역할의_회원은_카테고리의_일정을_삭제할_수_있다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리에_일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // when
        scheduleService.delete(네오.일정().getId(), 네오.계정().getId());

        // then
        assertThatThrownBy(() -> scheduleRepository.getById(네오.일정().getId()))
                .isInstanceOf(NoSuchScheduleException.class);
    }

    @DisplayName("ADMIN 역할이 아닌 회원이 카테고리에 새로운 일정을 삭제할 시 예외가 발생한다.")
    @Test
    void ADMIN_역할이_아닌_회원이_카테고리에_새로운_일정을_삭제할_시_예외가_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리에_일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        // when & then
        assertThatThrownBy(() -> scheduleService.delete(네오.일정().getId(), 포비.계정().getId()))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("카테고리 생성자라도 ADMIN 역할이 아니라면 새로운 일정을 삭제할 시 예외가 발생한다.")
    @Test
    void 카테고리_생성자라도_ADMIN_역할이_아니라면_새로운_일정을_삭제할_시_예외가_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리에_일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        네오.카테고리_관리_권한을_부여한다(포비.계정(), 네오.카테고리());
        포비.카테고리_관리_권한을_해제한다(네오.계정(), 네오.카테고리());

        Schedule 기존_일정 = 네오.일정();

        // when & then
        assertThatThrownBy(() -> scheduleService.delete(기존_일정.getId(), 네오.계정().getId()))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("일정 삭제 시 존재하지 않은 일정일 경우 예외가 발생한다.")
    @Test
    void 일정_삭제_시_존재하지_않은_일정일_경우_예외가_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        // when & then
        assertThatThrownBy(() -> scheduleService.delete(1L, 네오.계정().getId()))
                .isInstanceOf(NoSuchScheduleException.class);
    }

    private final class User {

        private Member member;
        private Category category;
        private Subscription subscription;
        private Schedule schedule;

        public User 회원_가입을_한다(final String email, final String name, final String profile) {
            this.member = new Member(email, name, profile, SocialType.GOOGLE);
            memberRepository.save(member);
            oAuthTokenRepository.save(new OAuthToken(this.member, "oAuthToken"));
            return this;
        }

        public User 카테고리를_등록한다(final String categoryName, final CategoryType categoryType) {
            this.category = new Category(categoryName, this.member, categoryType);
            CategoryRole categoryRole = new CategoryRole(category, this.member, ADMIN);
            Subscription subscription = new Subscription(this.member, category, COLOR_1);
            categoryRepository.save(category);
            categoryRoleRepository.save(categoryRole);
            subscriptionRepository.save(subscription);
            return this;
        }

        public User 카테고리를_구독한다(final Category category) {
            this.subscription = new Subscription(this.member, category, COLOR_1);
            CategoryRole categoryRole = new CategoryRole(category, this.member, NONE);
            subscriptionRepository.save(subscription);
            categoryRoleRepository.save(categoryRole);
            return this;
        }

        public User 카테고리_관리_권한을_부여한다(final Member otherMember, final Category category) {
            CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(otherMember.getId(),
                    category.getId());
            categoryRole.changeRole(ADMIN);
            return this;
        }

        public User 카테고리_관리_권한을_해제한다(final Member otherMember, final Category category) {
            CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(otherMember.getId(),
                    category.getId());
            categoryRole.changeRole(NONE);
            return this;
        }

        public User 카테고리에_일정을_등록한다(final String title, final LocalDateTime start, final LocalDateTime end,
                                   final String memo) {
            this.schedule = new Schedule(this.category, title, start, end, memo);
            scheduleRepository.save(schedule);
            return this;
        }

        public Member 계정() {
            return member;
        }

        public Category 카테고리() {
            return category;
        }

        public Schedule 일정() {
            return schedule;
        }
    }

    private ScheduleUpdateRequest 취업_일정_수정_요청(final Category category) {
        return new ScheduleUpdateRequest(category.getId(), "박람회 일정 제목", 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);
    }
}

package com.allog.dallog.schedule.application;

import static com.allog.dallog.category.domain.CategoryType.GOOGLE;
import static com.allog.dallog.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.categoryrole.domain.CategoryRoleType.ADMIN;
import static com.allog.dallog.categoryrole.domain.CategoryRoleType.NONE;
import static com.allog.dallog.common.Constants.나인_이름;
import static com.allog.dallog.common.Constants.나인_이메일;
import static com.allog.dallog.common.Constants.나인_프로필_URL;
import static com.allog.dallog.common.Constants.면접_일정_메모;
import static com.allog.dallog.common.Constants.면접_일정_시작일;
import static com.allog.dallog.common.Constants.면접_일정_제목;
import static com.allog.dallog.common.Constants.면접_일정_종료일;
import static com.allog.dallog.common.Constants.스터디_카테고리_이름;
import static com.allog.dallog.common.Constants.취업_일정_메모;
import static com.allog.dallog.common.Constants.취업_일정_시작일;
import static com.allog.dallog.common.Constants.취업_일정_제목;
import static com.allog.dallog.common.Constants.취업_일정_종료일;
import static com.allog.dallog.common.Constants.취업_카테고리_이름;
import static com.allog.dallog.common.Constants.티거_이름;
import static com.allog.dallog.common.Constants.티거_이메일;
import static com.allog.dallog.common.Constants.티거_프로필_URL;
import static com.allog.dallog.subscription.domain.Color.COLOR_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.auth.domain.OAuthToken;
import com.allog.dallog.auth.domain.OAuthTokenRepository;
import com.allog.dallog.auth.exception.NoPermissionException;
import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.category.domain.CategoryType;
import com.allog.dallog.category.exception.NoSuchCategoryException;
import com.allog.dallog.categoryrole.domain.CategoryRole;
import com.allog.dallog.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.categoryrole.exception.NoCategoryAuthorityException;
import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.member.domain.SocialType;
import com.allog.dallog.schedule.domain.IntegrationSchedule;
import com.allog.dallog.schedule.domain.Schedule;
import com.allog.dallog.schedule.domain.ScheduleRepository;
import com.allog.dallog.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.schedule.dto.request.ScheduleUpdateRequest;
import com.allog.dallog.schedule.dto.response.IntegrationScheduleResponse;
import com.allog.dallog.schedule.dto.response.IntegrationScheduleResponses;
import com.allog.dallog.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.schedule.exception.InvalidScheduleException;
import com.allog.dallog.schedule.exception.NoSuchScheduleException;
import com.allog.dallog.subscription.domain.Subscription;
import com.allog.dallog.subscription.domain.SubscriptionRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    void 관리_권한이_있는_회원은_카테고리에_새로운_일정을_생성할_수_있다() {
        // given & when
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // then
        assertThat(나인.카테고리_일정().getTitle()).isEqualTo(취업_일정_제목);
    }

    @Test
    void 관리_권한이_없는_회원이_카고리에_새로운_일정을_생성하려_하면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        GivenBuilder 티거 = 티거().회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        // when & then
        assertThatThrownBy(() -> scheduleService.save(티거.회원().getId(), 나인.카테고리().getId(), 취업_일정_생성_요청))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @Transactional
    @Test
    void 카테고리_생성자라도_관리_권한이_없으면_새로운_일정을_생성할_때_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        GivenBuilder 티거 = 티거().회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        나인.카테고리_관리_권한을_부여한다(티거.회원(), 나인.카테고리());
        티거.카테고리_관리_권한을_해제한다(나인.회원(), 나인.카테고리());

        // when & then
        assertThatThrownBy(() -> scheduleService.save(나인.회원().getId(), 나인.카테고리().getId(), 취업_일정_생성_요청))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @Test
    void 새로운_일정을_생성_할_때_일정_제목의_길이가_50을_초과하면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        String 잘못된_일정_제목 = "일이삼사오육칠팔구십일이삼사오육칠팔구십일일이삼사오육칠팔구십일이삼사오육칠팔구십일일이삼사오육칠팔구십일";
        ScheduleCreateRequest 잘못된_일정_생성_요청 = new ScheduleCreateRequest(잘못된_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(나인.회원().getId(), 나인.카테고리().getId(), 잘못된_일정_생성_요청)).
                isInstanceOf(InvalidScheduleException.class);
    }

    @Test
    void 새로운_일정을_생성_할_때_일정_메모의_길이가_255를_초과하면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        String 잘못된_일정_메모 = "1".repeat(256);
        ScheduleCreateRequest 잘못된_일정_생성_요청 = new ScheduleCreateRequest(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 잘못된_일정_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(나인.회원().getId(), 나인.카테고리().getId(), 잘못된_일정_생성_요청)).
                isInstanceOf(InvalidScheduleException.class);
    }

    @Test
    void 새로운_일정을_생성_할_때_종료일시가_시작일시_이전이라면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        ScheduleCreateRequest 잘못된_일정_생성_요청 = new ScheduleCreateRequest(취업_일정_제목, 취업_일정_종료일, 취업_일정_시작일, 취업_일정_메모);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(나인.회원().getId(), 나인.카테고리().getId(), 잘못된_일정_생성_요청)).
                isInstanceOf(InvalidScheduleException.class);
    }

    @Test
    void 존재하지_않는_카테고리에_일정을_추가하려하면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(나인.회원().getId(), 0L, 취업_일정_생성_요청)).
                isInstanceOf(NoSuchCategoryException.class);
    }

    @Test
    void 외부_연동_카테고리에_일정을_추가하려하면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, GOOGLE);

        // when & then
        assertThatThrownBy(() -> scheduleService.save(나인.회원().getId(), 나인.카테고리().getId(), 취업_일정_생성_요청)).
                isInstanceOf(NoPermissionException.class);
    }

    @Test
    void 단건_일정을_조회한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // when
        ScheduleResponse actual = scheduleService.findById(나인.카테고리_일정().getId());

        // then
        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(나인.카테고리().getId());
            assertThat(actual.getTitle()).isEqualTo(취업_일정_제목);
            assertThat(actual.getStartDateTime()).isEqualTo(취업_일정_시작일);
            assertThat(actual.getEndDateTime()).isEqualTo(취업_일정_종료일);
            assertThat(actual.getMemo()).isEqualTo(취업_일정_메모);
        });
    }

    @Test
    void 존재하지_않는_일정을_단건_조회하면_예외가_발생한다() {
        // given
        Long 잘못된_아이디 = 0L;

        // when & then
        assertThatThrownBy(() -> scheduleService.findById(잘못된_아이디))
                .isInstanceOf(NoSuchScheduleException.class);
    }

    @Transactional
    @Test
    void 월별_일정_조회를_하면_통합일정_정보를_반환한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모)
                .일정을_생성한다(면접_일정_제목, 면접_일정_시작일, 면접_일정_종료일, 면접_일정_메모);

        // when
        List<IntegrationSchedule> actual = scheduleService.findInternalByMemberIdAndDateRange(나인.회원().getId(),
                구간_일정_조회_요청).getSchedules();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void 카테고리_별_통합_일정_정보를_조회한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다("첫번째 장기 일정", LocalDateTime.of(2022, 7, 1, 0, 0), LocalDateTime.of(2022, 8, 15, 14, 0), "")
                .일정을_생성한다("두번째 장기 일정", LocalDateTime.of(2022, 7, 1, 0, 0), LocalDateTime.of(2022, 7, 31, 0, 0), "")
                .일정을_생성한다("세번째 장기 일정", LocalDateTime.of(2022, 7, 1, 0, 0), LocalDateTime.of(2022, 7, 16, 16, 1), "")
                .일정을_생성한다("네번째 장기 일정", LocalDateTime.of(2022, 7, 7, 16, 0), LocalDateTime.of(2022, 7, 15, 16, 0), "")
                .일정을_생성한다("다섯번째 장기 일정", LocalDateTime.of(2022, 7, 31, 0, 0), LocalDateTime.of(2022, 8, 15, 17, 0), "")
                .일정을_생성한다("첫번째 종일 일정", LocalDateTime.of(2022, 7, 10, 0, 0), LocalDateTime.of(2022, 7, 11, 0, 0), "")
                .일정을_생성한다("두번째 종일 일정", LocalDateTime.of(2022, 7, 27, 0, 0), LocalDateTime.of(2022, 7, 28, 0, 0), "")
                .일정을_생성한다("첫번째 몇시간 일정", LocalDateTime.of(2022, 7, 16, 16, 0), LocalDateTime.of(2022, 7, 16, 20, 0), "")
                .일정을_생성한다("두번째 몇시간 일정", LocalDateTime.of(2022, 7, 16, 16, 0), LocalDateTime.of(2022, 7, 16, 18, 0), "");

        // when
        IntegrationScheduleResponses actual = scheduleService.findByCategoryIdAndDateRange(나인.카테고리().getId(),
                구간_일정_조회_요청);

        // then
        assertAll(() -> {
            assertThat(actual.getLongTerms()).extracting(IntegrationScheduleResponse::getTitle)
                    .contains("첫번째 장기 일정", "두번째 장기 일정", "세번째 장기 일정", "네번째 장기 일정", "다섯번째 장기 일정");
            assertThat(actual.getAllDays()).extracting(IntegrationScheduleResponse::getTitle)
                    .contains("첫번째 종일 일정", "두번째 종일 일정");
            assertThat(actual.getFewHours()).extracting(IntegrationScheduleResponse::getTitle)
                    .contains("첫번째 몇시간 일정", "두번째 몇시간 일정");
        });
    }

    @Test
    void 일정을_수정한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // when
        ScheduleUpdateRequest 일정_수정_요청 = new ScheduleUpdateRequest(나인.카테고리().getId(), "제목", 취업_일정_시작일, 취업_일정_종료일, "메모");
        scheduleService.update(나인.카테고리_일정().getId(), 나인.회원().getId(), 일정_수정_요청);

        // then
        Schedule actual = scheduleRepository.getById(나인.카테고리_일정().getId());
        assertAll(
                () -> {
                    assertThat(actual.getId()).isEqualTo(나인.카테고리_일정().getId());
                    assertThat(actual.getTitle()).isEqualTo("제목");
                    assertThat(actual.getStartDateTime()).isEqualTo(취업_일정_시작일);
                    assertThat(actual.getEndDateTime()).isEqualTo(취업_일정_종료일);
                    assertThat(actual.getMemo()).isEqualTo("메모");
                }
        );
    }

    @Test
    void 관리_권한이_없는_회원이_카테고리의_일정을_수정하면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        GivenBuilder 티거 = 티거().회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        // when & then
        ScheduleUpdateRequest 일정_수정_요청 = new ScheduleUpdateRequest(나인.카테고리().getId(), "제목", 취업_일정_시작일, 취업_일정_종료일, "메모");

        assertThatThrownBy(() -> scheduleService.update(나인.카테고리_일정().getId(), 티거.회원().getId(), 일정_수정_요청))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @Transactional
    @Test
    void 카테고리_생성자라도_관리_권한이_없으면_카테고리의_일정을_수정할_떄_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        GivenBuilder 티거 = 티거().회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        나인.카테고리_관리_권한을_부여한다(티거.회원(), 나인.카테고리());
        티거.카테고리_관리_권한을_해제한다(나인.회원(), 나인.카테고리());

        // when & then
        ScheduleUpdateRequest 일정_수정_요청 = new ScheduleUpdateRequest(나인.카테고리().getId(), "제목", 취업_일정_시작일, 취업_일정_종료일, "메모");

        assertThatThrownBy(() -> scheduleService.update(나인.카테고리_일정().getId(), 나인.회원().getId(), 일정_수정_요청))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @Test
    void 존재하지_않은_일정을_수정하려하면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        // when & then
        ScheduleUpdateRequest 일정_수정_요청 = new ScheduleUpdateRequest(나인.카테고리().getId(), "제목", 취업_일정_시작일, 취업_일정_종료일, "메모");

        assertThatThrownBy(() -> scheduleService.update(0L, 나인.회원().getId(), 일정_수정_요청))
                .isInstanceOf(NoSuchScheduleException.class);
    }

    @Test
    void 일정의_카테고리를_변경한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        Schedule 기존_일정 = 나인.카테고리_일정();
        나인.카테고리를_생성한다(스터디_카테고리_이름, NORMAL);

        // when
        ScheduleUpdateRequest 일정_수정_요청 = new ScheduleUpdateRequest(나인.카테고리().getId(), "제목", 취업_일정_시작일, 취업_일정_종료일, "메모");
        scheduleService.update(기존_일정.getId(), 나인.회원().getId(), 일정_수정_요청);

        // then
        Schedule actual = scheduleRepository.getById(기존_일정.getId());
        assertThat(actual.getCategory().getId()).isEqualTo(나인.카테고리().getId());
    }

    @Test
    void 관리_권한이_있는_회원은_카테고리의_일정을_삭제할_수_있다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // when
        scheduleService.delete(나인.카테고리_일정().getId(), 나인.회원().getId());

        // then
        assertThatThrownBy(() -> scheduleRepository.getById(나인.카테고리_일정().getId()))
                .isInstanceOf(NoSuchScheduleException.class);
    }

    @Test
    void 관리_권한이_없는_회원이_카테고리의_일정을_삭제하려하면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        GivenBuilder 티거 = 티거().회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        // when & then
        assertThatThrownBy(() -> scheduleService.delete(나인.카테고리_일정().getId(), 티거.회원().getId()))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @Transactional
    @Test
    void 카테고리_생성자라도_관리_권한이_없으면_일정을_삭제하려할때_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL)
                .일정을_생성한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        GivenBuilder 티거 = 티거().회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        나인.카테고리_관리_권한을_부여한다(티거.회원(), 나인.카테고리());
        티거.카테고리_관리_권한을_해제한다(나인.회원(), 나인.카테고리());

        // when & then
        assertThatThrownBy(() -> scheduleService.delete(나인.카테고리_일정().getId(), 나인.회원().getId()))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @Test
    void 존재하지_않은_일정을_삭제하려하면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        // when & then
        assertThatThrownBy(() -> scheduleService.delete(0L, 나인.회원().getId()))
                .isInstanceOf(NoSuchScheduleException.class);
    }

    private GivenBuilder 나인() {
        return new GivenBuilder();
    }

    private GivenBuilder 티거() {
        return new GivenBuilder();
    }

    private final class GivenBuilder {

        private Member member;
        private Category category;
        private CategoryRole categoryRole;
        private Subscription subscription;
        private Schedule schedule;

        private GivenBuilder 회원_가입을_한다(final String email, final String name,
                                       final String profile) {
            Member member = new Member(email, name, profile, SocialType.GOOGLE);
            this.member = memberRepository.save(member);
            OAuthToken oAuthToken = new OAuthToken(this.member, "aaa");
            oAuthTokenRepository.save(oAuthToken);
            return this;
        }

        private GivenBuilder 카테고리를_생성한다(final String categoryName,
                                        final CategoryType categoryType) {
            Category category = new Category(categoryName, this.member, categoryType);
            CategoryRole categoryRole = new CategoryRole(category, this.member, ADMIN);
            Subscription subscription = new Subscription(this.member, category, COLOR_1);
            this.category = categoryRepository.save(category);
            this.categoryRole = categoryRoleRepository.save(categoryRole);
            this.subscription = subscriptionRepository.save(subscription);
            return this;
        }

        private GivenBuilder 카테고리를_구독한다(final Category category) {
            Subscription subscription = new Subscription(this.member, category, COLOR_1);
            CategoryRole categoryRole = new CategoryRole(category, this.member, NONE);
            this.subscription = subscriptionRepository.save(subscription);
            this.categoryRole = categoryRoleRepository.save(categoryRole);
            return this;
        }

        private GivenBuilder 카테고리_관리_권한을_부여한다(final Member otherMember, final Category category) {
            CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(otherMember.getId(),
                    category.getId());
            categoryRole.changeRole(ADMIN);
            categoryRoleRepository.save(categoryRole);
            return this;
        }

        private GivenBuilder 카테고리_관리_권한을_해제한다(final Member otherMember, final Category category) {
            CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(otherMember.getId(),
                    category.getId());
            categoryRole.changeRole(NONE);
            categoryRoleRepository.save(categoryRole);
            return this;
        }

        private GivenBuilder 일정을_생성한다(final String title, final LocalDateTime start,
                                      final LocalDateTime end,
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

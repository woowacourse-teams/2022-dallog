package com.allog.dallog.presentation;

import com.allog.dallog.common.config.ExternalApiConfig;
import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.auth.application.OAuthUri;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.categoryrole.application.CategoryRoleService;
import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarService;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.schedule.application.CheckedSchedulesFinder;
import com.allog.dallog.domain.schedule.application.ScheduleService;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.presentation.auth.AuthController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@Import(ExternalApiConfig.class)
@WebMvcTest({
        AuthController.class,
        CategoryController.class,
        ExternalCalendarController.class,
        MemberController.class,
        ScheduleController.class,
        SubscriptionController.class
})
@ActiveProfiles("test")
abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected CategoryService categoryService;

    @MockBean
    protected CategoryRoleService categoryRoleService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected OAuthUri oAuthUri;

    @MockBean
    protected ExternalCalendarService externalCalendarService;

    @MockBean
    protected ScheduleService scheduleService;

    @MockBean
    protected CheckedSchedulesFinder checkedSchedulesFinder;

    @MockBean
    protected SubscriptionService subscriptionService;
}

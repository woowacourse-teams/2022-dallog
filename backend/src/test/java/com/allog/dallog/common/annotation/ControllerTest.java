package com.allog.dallog.common.annotation;

import com.allog.dallog.auth.application.AuthService;
import com.allog.dallog.auth.application.OAuthUri;
import com.allog.dallog.auth.presentation.AuthController;
import com.allog.dallog.category.application.CategoryService;
import com.allog.dallog.category.presentaion.CategoryController;
import com.allog.dallog.categoryrole.application.CategoryRoleService;
import com.allog.dallog.common.config.ExternalApiConfig;
import com.allog.dallog.externalcalendar.application.ExternalCalendarService;
import com.allog.dallog.externalcalendar.presentation.ExternalCalendarController;
import com.allog.dallog.member.application.MemberService;
import com.allog.dallog.member.presentation.MemberController;
import com.allog.dallog.schedule.application.CheckedSchedulesFinder;
import com.allog.dallog.schedule.application.ScheduleService;
import com.allog.dallog.schedule.presentation.ScheduleController;
import com.allog.dallog.subscription.application.SubscriptionService;
import com.allog.dallog.subscription.presentation.SubscriptionController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest({
        AuthController.class,
        CategoryController.class,
        ExternalCalendarController.class,
        MemberController.class,
        ScheduleController.class,
        SubscriptionController.class
})
@Import(ExternalApiConfig.class)
@ActiveProfiles("test")
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected OAuthUri oAuthUri;

    @MockBean
    protected CategoryService categoryService;

    @MockBean
    protected CategoryRoleService categoryRoleService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected ExternalCalendarService externalCalendarService;

    @MockBean
    protected ScheduleService scheduleService;

    @MockBean
    protected CheckedSchedulesFinder checkedSchedulesFinder;

    @MockBean
    protected SubscriptionService subscriptionService;
}

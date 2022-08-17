package com.allog.dallog.common.fixtures;

import com.allog.dallog.domain.category.domain.CategoryType;
import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.integrationschedule.domain.Period;
import java.time.LocalDateTime;

public class IntegrationScheduleFixtures {

    public static final IntegrationSchedule 점심_식사 = new IntegrationSchedule("1", 2L, "점심 식사",
            new Period(LocalDateTime.of(2022, 8, 16, 11, 00), LocalDateTime.of(2022, 8, 16, 13, 00)), "",
            CategoryType.NORMAL.name());

    public static final IntegrationSchedule 달록_여행 = new IntegrationSchedule("2", 2L, "달록 여행",
            new Period(LocalDateTime.of(2022, 8, 24, 00, 00), LocalDateTime.of(2022, 8, 25, 23, 59)), "",
            CategoryType.NORMAL.name());

    public static final IntegrationSchedule 레벨3_방학 = new IntegrationSchedule("gsgadfgqwrtqwerfgasdasdasd", 1L,
            "레벨3 방학", new Period(LocalDateTime.of(2022, 8, 20, 00, 00), LocalDateTime.of(2022, 8, 20, 00, 00)), "",
            CategoryType.GOOGLE.name());

    public static final IntegrationSchedule 포수타 = new IntegrationSchedule("asgasgasfgadfgdf", 1L,
            "포수타", new Period(LocalDateTime.of(2022, 8, 12, 14, 00), LocalDateTime.of(2022, 8, 12, 14, 30)), "",
            CategoryType.GOOGLE.name());
}

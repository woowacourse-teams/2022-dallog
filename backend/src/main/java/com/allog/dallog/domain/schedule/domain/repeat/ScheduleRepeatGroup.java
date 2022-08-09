package com.allog.dallog.domain.schedule.domain.repeat;

import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.repeat.strategy.RepeatStrategy;
import com.allog.dallog.domain.schedule.domain.repeat.strategy.RepeatType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "schedule_repeat_groups")
@Entity
public class ScheduleRepeatGroup {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "scheduleRepeatGroup", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    protected ScheduleRepeatGroup() {
    }

    public ScheduleRepeatGroup(final Schedule initialSchedule, final LocalDate endDate, final RepeatType repeatType) {
        RepeatStrategy repeatStrategy = repeatType.getRepeatStrategy();
        List<Schedule> schedules = repeatStrategy.getSchedules(initialSchedule, endDate);
        this.schedules = addScheduleRepeatGroup(schedules);
    }

    private List<Schedule> addScheduleRepeatGroup(final List<Schedule> schedules) {
        return schedules.stream()
                .map(schedule -> new Schedule(schedule, this))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public List<Schedule> getSchedules() {
        return List.copyOf(schedules);
    }
}

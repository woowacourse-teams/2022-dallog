package com.allog.dallog.presentation;

import com.allog.dallog.domain.composition.application.SchedulerService;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.response.PeriodResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/scheduler")
@RestController
public class SchedulerController {

    private final SchedulerService schedulerService;

    public SchedulerController(final SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @GetMapping("/categories/{categoryId}/available-periods")
    public ResponseEntity<List<PeriodResponse>> scheduleByCategory(@PathVariable final Long categoryId,
                                                                   @ModelAttribute DateRangeRequest dateRange) {
        List<PeriodResponse> periods = schedulerService.getAvailablePeriods(categoryId, dateRange);
        return ResponseEntity.ok(periods);
    }
}

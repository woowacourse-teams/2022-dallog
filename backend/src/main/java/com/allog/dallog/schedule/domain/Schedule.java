package com.allog.dallog.schedule.domain;

import com.allog.dallog.common.BaseEntity;
import com.allog.dallog.category.domain.Category;
import com.allog.dallog.schedule.exception.InvalidScheduleException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "schedules")
@Entity
public class Schedule extends BaseEntity {

    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_MEMO_LENGTH = 255;

    private static final LocalDateTime MIN_DATE_TIME = LocalDateTime.of(1000, 1, 1, 0, 0);
    private static final LocalDateTime MAX_DATE_TIME = LocalDateTime.of(9999, 12, 31, 11, 59, 59, 999999000);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_id", nullable = false)
    private Category category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "memo", nullable = false)
    private String memo;

    protected Schedule() {
    }

    public Schedule(final Category category, final String title, final LocalDateTime startDateTime,
                    final LocalDateTime endDateTime, final String memo) {
        validateTitleLength(title);
        validatePeriod(startDateTime, endDateTime);
        validateMemoLength(memo);
        this.category = category;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.memo = memo;
    }

    public void change(final Category category, final String title, final LocalDateTime startDateTime,
                       final LocalDateTime endDateTime, final String memo) {
        validateTitleLength(title);
        validatePeriod(startDateTime, endDateTime);
        validateMemoLength(memo);
        this.category = category;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.memo = memo;
    }

    private void validateTitleLength(final String title) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new InvalidScheduleException(String.format("일정 제목의 길이는 %d을 초과할 수 없습니다.", MAX_TITLE_LENGTH));
        }
    }

    private void validatePeriod(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new InvalidScheduleException("종료일시가 시작일시보다 이전일 수 없습니다.");
        }
        if (isNotValidDateTimeRange(startDateTime) || isNotValidDateTimeRange(endDateTime)) {
            throw new InvalidScheduleException(
                    String.format("일정은 %s부터 %s까지 등록할 수 있습니다.",
                            MIN_DATE_TIME.toLocalDate(), MAX_DATE_TIME.toLocalDate())
            );
        }
    }

    private boolean isNotValidDateTimeRange(final LocalDateTime dateTime) {
        return dateTime.isBefore(MIN_DATE_TIME) || dateTime.isAfter(MAX_DATE_TIME);
    }

    private void validateMemoLength(final String memo) {
        if (memo.length() > MAX_MEMO_LENGTH) {
            throw new InvalidScheduleException(String.format("일정 메모의 길이는 %d를 초과할 수 없습니다.", MAX_MEMO_LENGTH));
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getMemo() {
        return memo;
    }

    public Category getCategory() {
        return category;
    }
}

import { useTheme } from '@emotion/react';
import { useLayoutEffect, useRef, useState } from 'react';

import { useGetSchedules } from '@/hooks/@queries/schedule';
import useCalendar from '@/hooks/useCalendar';
import useModalPosition from '@/hooks/useModalPosition';
import useRootFontSize from '@/hooks/useRootFontSize';
import useToggle from '@/hooks/useToggle';

import { ScheduleType } from '@/@types/schedule';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Responsive from '@/components/@common/Responsive/Responsive';
import Spinner from '@/components/@common/Spinner/Spinner';
import MoreScheduleModal from '@/components/MoreScheduleModal/MoreScheduleModal';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';
import ScheduleModal from '@/components/ScheduleModal/ScheduleModal';
import ScheduleModifyModal from '@/components/ScheduleModifyModal/ScheduleModifyModal';

import { CALENDAR } from '@/constants';
import { DAYS } from '@/constants/date';
import { PAGE_LAYOUT, RESPONSIVE, SCHEDULE, TRANSPARENT } from '@/constants/style';

import {
  checkAllDay,
  extractDateTime,
  getDayOffsetDateTime,
  getISODateString,
  getToday,
} from '@/utils/date';

import getSchedulePriority from '@/domains/schedule';

import { MdKeyboardArrowLeft, MdKeyboardArrowRight } from 'react-icons/md';

import {
  calendarGrid,
  calendarHeader,
  calendarPage,
  dateBorder,
  dateText,
  dayBar,
  itemWithBackgroundStyle,
  itemWithoutBackgroundStyle,
  monthPicker,
  moreStyle,
  navBarGrid,
  navButton,
  navButtonTitle,
  spinnerStyle,
  todayButton,
  waitingNavStyle,
} from './CalendarPage.styles';

function CalendarPage() {
  const theme = useTheme();
  const dateRef = useRef<HTMLDivElement>(null);

  const [maxScheduleCount, setMaxScheduleCount] = useState(0);
  const [hoveringId, setHoveringId] = useState('0');
  const [dateInfo, setDateInfo] = useState('');
  const [scheduleInfo, setScheduleInfo] = useState<ScheduleType | null>(null);
  const [moreScheduleDateTime, setMoreScheduleDateTime] = useState('');

  const {
    calendar,
    currentDateTime,
    moveToBeforeMonth,
    moveToToday,
    moveToNextMonth,
    startDateTime,
    endDateTime,
  } = useCalendar();

  const rootFontSize = useRootFontSize();

  const { state: isScheduleAddModalOpen, toggleState: toggleScheduleAddModalOpen } = useToggle();
  const { state: isScheduleModifyModalOpen, toggleState: toggleScheduleModifyModalOpen } =
    useToggle();

  const scheduleModal = useModalPosition();
  const moreScheduleModal = useModalPosition();

  const { isLoading, data } = useGetSchedules({ startDateTime, endDateTime });

  useLayoutEffect(() => {
    if (!(dateRef.current instanceof HTMLDivElement)) return;

    setMaxScheduleCount(
      Math.floor(
        (Math.floor(dateRef.current.clientHeight / 10) * 10 - SCHEDULE.HEIGHT * rootFontSize) /
          (SCHEDULE.HEIGHT_WITH_MARGIN * rootFontSize)
      )
    );
  }, [startDateTime, rootFontSize]);

  const { year: currentYear, month: currentMonth } = extractDateTime(currentDateTime);
  const rowNum = Math.ceil(calendar.length / 7);

  const handleClickScheduleAddButton = () => {
    setDateInfo(getToday());
    toggleScheduleAddModalOpen();
  };

  const handleClickDate = (e: React.MouseEvent, info: string) => {
    if (e.target !== e.currentTarget) {
      return;
    }

    setDateInfo(info);
    toggleScheduleAddModalOpen();
  };

  if (isLoading || data === undefined) {
    return (
      <PageLayout type={PAGE_LAYOUT.SIDEBAR}>
        <div css={calendarPage}>
          <div css={calendarHeader}>
            {`${currentYear}년 ${currentMonth}월`}
            <div css={waitingNavStyle}>
              <div css={spinnerStyle}>
                <Spinner size={rootFontSize} />
                <Responsive type={RESPONSIVE.LAPTOP.DEVICE}>
                  <span>일정을 가져오고 있습니다.</span>
                </Responsive>
              </div>
              <div css={monthPicker}>
                <Button cssProp={navButton} onClick={moveToBeforeMonth} aria-label="이전 달">
                  <MdKeyboardArrowLeft />
                  <span css={navButtonTitle}>전 달</span>
                </Button>
                <Button cssProp={todayButton} onClick={moveToToday} aria-label="이번 달">
                  오늘
                </Button>
                <Button cssProp={navButton} onClick={moveToNextMonth} aria-label="다음 달">
                  <MdKeyboardArrowRight />
                  <span css={navButtonTitle}>다음 달</span>
                </Button>
              </div>
            </div>
          </div>
          <div css={navBarGrid}>
            {DAYS.map((day) => (
              <span key={`day#${day}`} css={dayBar(theme, day)}>
                {day}
              </span>
            ))}
          </div>
          <div css={calendarGrid(rowNum)}>
            {calendar.map((dateTime) => {
              const { month, date, day } = extractDateTime(dateTime);

              return (
                <div key={dateTime} ref={dateRef}>
                  <div css={dateBorder(theme, day)} onClick={(e) => handleClickDate(e, dateTime)}>
                    <span
                      css={dateText(theme, day, currentMonth === month, dateTime === getToday())}
                    >
                      {date}
                    </span>
                  </div>
                </div>
              );
            })}
          </div>
          {dateInfo && (
            <ModalPortal isOpen={isScheduleAddModalOpen} closeModal={toggleScheduleAddModalOpen}>
              <ScheduleAddModal dateInfo={dateInfo} closeModal={toggleScheduleAddModalOpen} />
            </ModalPortal>
          )}
          <ScheduleAddButton onClick={handleClickScheduleAddButton} />
        </div>
      </PageLayout>
    );
  }

  const { calendarWithPriority, getLongTermSchedulesWithPriority, getSingleSchedulesWithPriority } =
    getSchedulePriority(calendar);

  const longTermSchedulesWithPriority = getLongTermSchedulesWithPriority(data.data.longTerms);
  const allDaySchedulesWithPriority = getSingleSchedulesWithPriority(data.data.allDays);
  const fewHourSchedulesWithPriority = getSingleSchedulesWithPriority(data.data.fewHours);

  const onMouseEnter = (scheduleId: string) => {
    setHoveringId(scheduleId);
  };

  const onMouseLeave = () => {
    setHoveringId('0');
  };

  return (
    <PageLayout type={PAGE_LAYOUT.SIDEBAR}>
      <div css={calendarPage}>
        <div css={calendarHeader}>
          {currentYear}년 {currentMonth}월
          <div css={monthPicker}>
            <Button cssProp={navButton} onClick={moveToBeforeMonth}>
              <MdKeyboardArrowLeft />
              <span css={navButtonTitle}>전 달</span>
            </Button>
            <Button cssProp={todayButton} onClick={moveToToday}>
              오늘
            </Button>
            <Button cssProp={navButton} onClick={moveToNextMonth}>
              <MdKeyboardArrowRight />
              <span css={navButtonTitle}>다음 달</span>
            </Button>
          </div>
        </div>
        <div css={navBarGrid}>
          {DAYS.map((day) => (
            <span key={`${day}#day`} css={dayBar(theme, day)}>
              {day}
            </span>
          ))}
        </div>
        <div css={calendarGrid(rowNum)}>
          {calendar.map((dateTime) => {
            const { month, date, day } = extractDateTime(dateTime);
            const currentDate = getISODateString(dateTime);
            const priorityPosition = calendarWithPriority[getISODateString(dateTime)].findIndex(
              (priority) => !priority
            );

            const hasMoreSchedule =
              priorityPosition === -1 || priorityPosition + 1 > maxScheduleCount;

            return (
              <div key={dateTime}>
                <div
                  css={dateBorder(theme, day)}
                  onClick={(e) => handleClickDate(e, dateTime)}
                  ref={dateRef}
                >
                  <span css={dateText(theme, day, currentMonth === month, dateTime === getToday())}>
                    {date}
                  </span>

                  {longTermSchedulesWithPriority.map(({ schedule, priority }) => {
                    const startDate = getISODateString(schedule.startDateTime);
                    const endDate = getISODateString(
                      checkAllDay(schedule.startDateTime, schedule.endDateTime)
                        ? getDayOffsetDateTime(schedule.endDateTime, -1)
                        : schedule.endDateTime
                    );
                    const { day: currentDay } = extractDateTime(dateTime);

                    if (!(startDate <= currentDate && currentDate <= endDate) || priority === null)
                      return;

                    return (
                      <div
                        key={`${currentDate}#${schedule.id}#longTerms`}
                        css={itemWithBackgroundStyle(
                          priority,
                          schedule.colorCode,
                          hoveringId === schedule.id,
                          maxScheduleCount,
                          currentDate === endDate
                        )}
                        onMouseEnter={() => onMouseEnter(schedule.id)}
                        onClick={(e) =>
                          scheduleModal.handleClickOpen(e, () => setScheduleInfo(schedule))
                        }
                        onMouseLeave={onMouseLeave}
                      >
                        {(startDate === currentDate || currentDay === 0) &&
                          (schedule.title.trim() || CALENDAR.EMPTY_TITLE)}
                      </div>
                    );
                  })}

                  {allDaySchedulesWithPriority.map(({ schedule, priority }) => {
                    const startDate = getISODateString(schedule.startDateTime);

                    if (startDate !== currentDate || priority === null) return;

                    return (
                      <div
                        key={`${currentDate}#${schedule.id}#allDays`}
                        css={itemWithBackgroundStyle(
                          priority,
                          schedule.colorCode,
                          hoveringId === schedule.id,
                          maxScheduleCount,
                          true
                        )}
                        onMouseEnter={() => onMouseEnter(schedule.id)}
                        onClick={(e) =>
                          scheduleModal.handleClickOpen(e, () => setScheduleInfo(schedule))
                        }
                        onMouseLeave={onMouseLeave}
                      >
                        {schedule.title.trim() || CALENDAR.EMPTY_TITLE}
                      </div>
                    );
                  })}

                  {fewHourSchedulesWithPriority.map(({ schedule, priority }) => {
                    const startDate = getISODateString(schedule.startDateTime);

                    if (startDate !== currentDate || priority === null) return;

                    return (
                      <div
                        key={`${currentDate}#${schedule.id}#fewHours`}
                        css={itemWithoutBackgroundStyle(
                          theme,
                          priority,
                          schedule.colorCode,
                          hoveringId === schedule.id,
                          maxScheduleCount,
                          false
                        )}
                        onMouseEnter={() => onMouseEnter(schedule.id)}
                        onClick={(e) =>
                          scheduleModal.handleClickOpen(e, () => setScheduleInfo(schedule))
                        }
                        onMouseLeave={onMouseLeave}
                      >
                        {schedule.title.trim() || CALENDAR.EMPTY_TITLE}
                      </div>
                    );
                  })}

                  {hasMoreSchedule && (
                    <span
                      css={moreStyle}
                      onClick={(e) =>
                        moreScheduleModal.handleClickOpen(e, () =>
                          setMoreScheduleDateTime(dateTime)
                        )
                      }
                    >
                      일정 더보기
                    </span>
                  )}
                </div>

                {dateTime === moreScheduleDateTime && (
                  <ModalPortal
                    isOpen={moreScheduleModal.isModalOpen}
                    closeModal={moreScheduleModal.toggleModalOpen}
                    dimmerBackground={TRANSPARENT}
                  >
                    <MoreScheduleModal
                      moreScheduleModalPos={moreScheduleModal.modalPos}
                      moreScheduleDateTime={moreScheduleDateTime}
                      longTermSchedulesWithPriority={longTermSchedulesWithPriority}
                      allDaySchedulesWithPriority={allDaySchedulesWithPriority}
                      fewHourSchedulesWithPriority={fewHourSchedulesWithPriority}
                    />
                  </ModalPortal>
                )}
              </div>
            );
          })}
        </div>
        {dateInfo && (
          <ModalPortal isOpen={isScheduleAddModalOpen} closeModal={toggleScheduleAddModalOpen}>
            <ScheduleAddModal dateInfo={dateInfo} closeModal={toggleScheduleAddModalOpen} />
          </ModalPortal>
        )}
        <ScheduleAddButton onClick={handleClickScheduleAddButton} />

        {scheduleInfo && (
          <>
            <ModalPortal
              isOpen={scheduleModal.isModalOpen}
              closeModal={scheduleModal.toggleModalOpen}
              dimmerBackground={TRANSPARENT}
            >
              <ScheduleModal
                scheduleModalPos={scheduleModal.modalPos}
                scheduleInfo={scheduleInfo}
                toggleScheduleModifyModalOpen={toggleScheduleModifyModalOpen}
                closeModal={scheduleModal.toggleModalOpen}
              />
            </ModalPortal>
            <ModalPortal
              isOpen={isScheduleModifyModalOpen}
              closeModal={toggleScheduleModifyModalOpen}
            >
              <ScheduleModifyModal
                scheduleInfo={scheduleInfo}
                closeModal={toggleScheduleModifyModalOpen}
              />
            </ModalPortal>
          </>
        )}
      </div>
    </PageLayout>
  );
}

export default CalendarPage;

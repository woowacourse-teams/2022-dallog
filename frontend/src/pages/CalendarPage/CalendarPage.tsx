import { useTheme } from '@emotion/react';
import { useLayoutEffect, useRef, useState } from 'react';

import { useGetSchedules } from '@/hooks/@queries/schedule';
import useCalendar from '@/hooks/useCalendar';
import useModalPosition from '@/hooks/useModalPosition';
import useSchedulePriority from '@/hooks/useSchedulePriority';
import useToggle from '@/hooks/useToggle';

import { ScheduleType } from '@/@types/schedule';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Spinner from '@/components/@common/Spinner/Spinner';
import MoreScheduleModal from '@/components/MoreScheduleModal/MoreScheduleModal';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';
import ScheduleModal from '@/components/ScheduleModal/ScheduleModal';
import ScheduleModifyModal from '@/components/ScheduleModifyModal/ScheduleModifyModal';

import { CALENDAR } from '@/constants';
import { DAYS } from '@/constants/date';
import { SCHEDULE, TRANSPARENT } from '@/constants/style';

import {
  checkAllDay,
  extractDateTime,
  getDayOffsetDateTime,
  getISODateString,
  getToday,
} from '@/utils/date';

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

  const { calendarWithPriority, getLongTermSchedulesWithPriority, getSingleSchedulesWithPriority } =
    useSchedulePriority(calendar);

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
        (dateRef.current.clientHeight - SCHEDULE.HEIGHT * 4) / (SCHEDULE.HEIGHT_WITH_MARGIN * 4)
      )
    );
  }, [dateRef.current]);

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
      <PageLayout>
        <div css={calendarPage}>
          <div css={calendarHeader}>
            {`${currentYear}년 ${currentMonth}월`}
            <div css={waitingNavStyle}>
              <div css={spinnerStyle}>
                <Spinner size={4} />
                일정을 가져오고 있습니다.
              </div>
              <div css={monthPicker}>
                <Button cssProp={navButton} onClick={moveToBeforeMonth} aria-label="이전 달로 이동">
                  <MdKeyboardArrowLeft />
                  <span css={navButtonTitle}>전 달</span>
                </Button>
                <Button cssProp={todayButton} onClick={moveToToday} aria-label="이번 달로 이동">
                  오늘
                </Button>
                <Button cssProp={navButton} onClick={moveToNextMonth} aria-label="다음 달로 이동">
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
                <div key={dateTime}>
                  <div
                    css={dateBorder(theme, day)}
                    onClick={(e) => handleClickDate(e, dateTime)}
                    ref={dateRef}
                  >
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
    <PageLayout>
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

                  {calendarWithPriority[getISODateString(dateTime)].findIndex((el) => !el) + 1 >
                    maxScheduleCount && (
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

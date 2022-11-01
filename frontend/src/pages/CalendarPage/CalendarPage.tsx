import { useTheme } from '@emotion/react';
import { useLayoutEffect, useRef, useState } from 'react';

import { useGetSchedules } from '@/hooks/@queries/schedule';
import useCalendar from '@/hooks/useCalendar';
import useRootFontSize from '@/hooks/useRootFontSize';
import useToggle from '@/hooks/useToggle';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Responsive from '@/components/@common/Responsive/Responsive';
import Spinner from '@/components/@common/Spinner/Spinner';
import DateCell from '@/components/DateCell/DateCell';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';

import { DAYS } from '@/constants/date';
import { PAGE_LAYOUT, RESPONSIVE, SCHEDULE } from '@/constants/style';

import { extractDateTime, getToday } from '@/utils/date';

import getSchedulePriority from '@/domains/schedule';

import { MdKeyboardArrowLeft, MdKeyboardArrowRight } from 'react-icons/md';

import {
  calendarGridStyle,
  calendarHeaderStyle,
  calendarPageStyle,
  dayGridStyle,
  dayStyle,
  monthPickerStyle,
  navButtonStyle,
  navButtonTitleStyle,
  navStyle,
  spinnerStyle,
  todayButtonStyle,
} from './CalendarPage.styles';

function CalendarPage() {
  const theme = useTheme();

  const dateCellRef = useRef<HTMLDivElement>(null);

  const [maxScheduleCount, setMaxScheduleCount] = useState(0);
  const [dateInfo, setDateInfo] = useState('');

  const rootFontSize = useRootFontSize();

  const {
    calendar,
    currentDateTime,
    moveToBeforeMonth,
    moveToToday,
    moveToNextMonth,
    startDateTime,
    endDateTime,
  } = useCalendar();

  const { state: isScheduleAddModalOpen, toggleState: toggleScheduleAddModalOpen } = useToggle();

  const { isLoading, data } = useGetSchedules({ startDateTime, endDateTime });

  useLayoutEffect(() => {
    if (!(dateCellRef.current instanceof HTMLDivElement)) return;

    setMaxScheduleCount(
      Math.floor(
        (Math.floor(dateCellRef.current.clientHeight / 10) * 10 - SCHEDULE.HEIGHT * rootFontSize) /
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

  if (isLoading || data === undefined) {
    return (
      <PageLayout type={PAGE_LAYOUT.SIDEBAR}>
        <div css={calendarPageStyle}>
          <div css={calendarHeaderStyle}>
            {`${currentYear}년 ${currentMonth}월`}
            <div css={navStyle}>
              <div css={spinnerStyle}>
                <Spinner size={rootFontSize} />
                <Responsive type={RESPONSIVE.LAPTOP.DEVICE}>
                  <span>일정을 가져오고 있습니다.</span>
                </Responsive>
              </div>
              <div css={monthPickerStyle}>
                <Button cssProp={navButtonStyle} onClick={moveToBeforeMonth} aria-label="이전 달">
                  <MdKeyboardArrowLeft />
                  <span css={navButtonTitleStyle}>전 달</span>
                </Button>
                <Button cssProp={todayButtonStyle} onClick={moveToToday} aria-label="이번 달">
                  오늘
                </Button>
                <Button cssProp={navButtonStyle} onClick={moveToNextMonth} aria-label="다음 달">
                  <MdKeyboardArrowRight />
                  <span css={navButtonTitleStyle}>다음 달</span>
                </Button>
              </div>
            </div>
          </div>
          <div css={dayGridStyle}>
            {DAYS.map((day) => (
              <span key={`day#${day}`} css={dayStyle(theme, day)}>
                {day}
              </span>
            ))}
          </div>
          <div css={calendarGridStyle(rowNum)}>
            {calendar.map((dateTime) => {
              return (
                <DateCell
                  key={dateTime}
                  dateTime={dateTime}
                  currentMonth={currentMonth}
                  dateCellRef={dateCellRef}
                  setDateInfo={setDateInfo}
                  onClick={toggleScheduleAddModalOpen}
                />
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

  const schedulesWithPriority = {
    longTermSchedulesWithPriority: getLongTermSchedulesWithPriority(data.data.longTerms),
    allDaySchedulesWithPriority: getSingleSchedulesWithPriority(data.data.allDays),
    fewHourSchedulesWithPriority: getSingleSchedulesWithPriority(data.data.fewHours),
  };

  return (
    <PageLayout type={PAGE_LAYOUT.SIDEBAR}>
      <div css={calendarPageStyle}>
        <div css={calendarHeaderStyle}>
          {`${currentYear}년 ${currentMonth}월`}
          <div css={monthPickerStyle}>
            <Button cssProp={navButtonStyle} onClick={moveToBeforeMonth}>
              <MdKeyboardArrowLeft />
              <span css={navButtonTitleStyle}>전 달</span>
            </Button>
            <Button cssProp={todayButtonStyle} onClick={moveToToday}>
              오늘
            </Button>
            <Button cssProp={navButtonStyle} onClick={moveToNextMonth}>
              <MdKeyboardArrowRight />
              <span css={navButtonTitleStyle}>다음 달</span>
            </Button>
          </div>
        </div>
        <div css={dayGridStyle}>
          {DAYS.map((day) => (
            <span key={`${day}#day`} css={dayStyle(theme, day)}>
              {day}
            </span>
          ))}
        </div>
        <div css={calendarGridStyle(rowNum)}>
          {calendar.map((dateTime) => {
            return (
              <DateCell
                key={dateTime}
                dateTime={dateTime}
                currentMonth={currentMonth}
                dateCellRef={dateCellRef}
                maxScheduleCount={maxScheduleCount}
                calendarWithPriority={calendarWithPriority}
                schedulesWithPriority={schedulesWithPriority}
                setDateInfo={setDateInfo}
                onClick={toggleScheduleAddModalOpen}
              />
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

export default CalendarPage;

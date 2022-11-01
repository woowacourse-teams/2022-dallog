import { useTheme } from '@emotion/react';
import { AxiosResponse } from 'axios';

import { CalendarControllerType } from '@/hooks/useCalendar';

import { CategoryType } from '@/@types/category';
import { ScheduleResponseType } from '@/@types/schedule';

import Button from '@/components/@common/Button/Button';
import DateCell from '@/components/DateCell/DateCell';

import { DAYS } from '@/constants/date';

import getSchedulePriority from '@/domains/schedule';

import { MdKeyboardArrowLeft, MdKeyboardArrowRight } from 'react-icons/md';

import {
  calendarGridStyle,
  calendarHeaderStyle,
  dayGridStyle,
  dayStyle,
  monthPickerStyle,
  navButtonStyle,
  navButtonTitleStyle,
  todayButtonStyle,
} from './Calendar.styles';

interface CalendarProps {
  calendarController: CalendarControllerType;
  scheduleResponse: AxiosResponse<ScheduleResponseType>;
  setDateInfo?: React.Dispatch<React.SetStateAction<string>>;
  handleClickDateCell?: () => void;
  category?: Pick<CategoryType, 'id' | 'name'>;
  readonly?: boolean;
}

function Calendar({
  calendarController,
  scheduleResponse,
  setDateInfo,
  handleClickDateCell,
  category,
  readonly,
}: CalendarProps) {
  const theme = useTheme();

  const {
    calendar,
    currentMonth,
    currentYear,
    dateCellRef,
    maxScheduleCount,
    moveToBeforeMonth,
    moveToNextMonth,
    moveToToday,
    rowCount,
  } = calendarController;

  const { calendarWithPriority, getLongTermSchedulesWithPriority, getSingleSchedulesWithPriority } =
    getSchedulePriority(calendar);

  const schedulesWithPriority = {
    longTermSchedulesWithPriority: getLongTermSchedulesWithPriority(
      scheduleResponse.data.longTerms
    ),
    allDaySchedulesWithPriority: getSingleSchedulesWithPriority(scheduleResponse.data.allDays),
    fewHourSchedulesWithPriority: getSingleSchedulesWithPriority(scheduleResponse.data.fewHours),
  };

  return (
    <>
      <div css={calendarHeaderStyle}>
        {`${currentYear}년 ${currentMonth}월${category ? ` \u00A0☾\u00A0 ${category.name}` : ''}`}
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
      <div css={dayGridStyle}>
        {DAYS.map((day) => (
          <span key={`${day}#day`} css={dayStyle(theme, day)}>
            {day}
          </span>
        ))}
      </div>
      <div css={calendarGridStyle(rowCount)}>
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
              onClick={handleClickDateCell}
              readonly={readonly}
            />
          );
        })}
      </div>
    </>
  );
}

export default Calendar;

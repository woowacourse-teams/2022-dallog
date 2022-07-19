import { useTheme } from '@emotion/react';
import { useState } from 'react';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';

import CalendarDate from '@/components/CalendarDate/CalendarDate';

import { DAYS } from '@/constants';

import {
  getBeforeYearMonth,
  getCalendarMonth,
  getNextYearMonth,
  getThisMonth,
  getThisYear,
} from '@/utils/date';

import Button from '../@common/Button/Button';
import { calendarGrid, calendarHeader, dayBar, monthPicker } from './Calendar.styles';

function Calendar() {
  const theme = useTheme();

  const [calendarMonth, setCalendarMonth] = useState(
    getCalendarMonth(getThisYear(), getThisMonth())
  );
  const [current, setCurrent] = useState({
    year: calendarMonth[15].year,
    month: calendarMonth[15].month,
  });

  const handleClickBeforeMonthButton = () => {
    const { year, month } = getBeforeYearMonth(current.year, current.month);
    setCurrent({ year, month });
    setCalendarMonth(getCalendarMonth(year, month));
  };

  const handleClickTodayButton = () => {
    const year = getThisYear();
    const month = getThisMonth();
    setCurrent({ year, month });
    setCalendarMonth(getCalendarMonth(year, month));
  };

  const handleClickNextMonthButton = () => {
    const { year, month } = getNextYearMonth(current.year, current.month);
    setCurrent({ year, month });
    setCalendarMonth(getCalendarMonth(year, month));
  };
  return (
    <>
      <div css={calendarHeader(theme)}>
        <span>
          {current.year}년 {current.month}월
        </span>
        <div css={monthPicker}>
          <Button onClick={handleClickBeforeMonthButton}>
            <AiOutlineLeft />
          </Button>
          <Button onClick={handleClickTodayButton}>Today</Button>
          <Button onClick={handleClickNextMonthButton}>
            <AiOutlineRight />
          </Button>
        </div>
      </div>

      <div css={calendarGrid}>
        {DAYS.map((day) => (
          <span key={day} css={dayBar(theme, day)}>
            {day}
          </span>
        ))}
      </div>
      <div css={calendarGrid}>
        {calendarMonth.map((info) => {
          const key = `${info.year}${info.month}${info.date}${info.day}`;
          return (
            <CalendarDate key={key} dateInfo={info} isThisMonth={current.month === info.month} />
          );
        })}
      </div>
    </>
  );
}

export default Calendar;

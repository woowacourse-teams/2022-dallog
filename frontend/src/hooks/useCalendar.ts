import { useState } from 'react';

import {
  getBeforeYearMonth,
  getCalendarMonth,
  getFormattedDate,
  getNextYearMonth,
  getThisMonth,
  getThisYear,
} from '@/utils/date';

function useCalendar() {
  const [current, setCurrent] = useState({
    year: getThisYear(),
    month: getThisMonth(),
  });

  const [calendarMonth, setCalendarMonth] = useState(
    getCalendarMonth(getThisYear(), getThisMonth())
  );

  const moveToBeforeMonth = () => {
    const { year, month } = getBeforeYearMonth(current.year, current.month);

    setCurrent({ year, month });
    setCalendarMonth(getCalendarMonth(year, month));
  };

  const moveToToday = () => {
    const year = getThisYear();
    const month = getThisMonth();

    setCurrent({ year, month });
    setCalendarMonth(getCalendarMonth(year, month));
  };

  const moveToNextMonth = () => {
    const { year, month } = getNextYearMonth(current.year, current.month);

    setCurrent({ year, month });
    setCalendarMonth(getCalendarMonth(year, month));
  };

  const startDate = getFormattedDate(
    calendarMonth[0].year,
    calendarMonth[0].month,
    calendarMonth[0].date
  );

  const endDate = getFormattedDate(
    calendarMonth[calendarMonth.length - 1].year,
    calendarMonth[calendarMonth.length - 1].month,
    calendarMonth[calendarMonth.length - 1].date
  );

  return {
    calendarMonth,
    current,
    endDate,
    moveToBeforeMonth,
    moveToNextMonth,
    moveToToday,
    startDate,
  };
}

export default useCalendar;

import { useState } from 'react';

import {
  getBeforeYearMonth,
  getCalendarMonth,
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

  return {
    calendarMonth,
    current,
    moveToBeforeMonth,
    moveToNextMonth,
    moveToToday,
  };
}

export default useCalendar;

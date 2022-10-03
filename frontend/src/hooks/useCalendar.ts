import { useState } from 'react';

import { getCurrentCalendar, getMonthOffsetDateTime, getToday } from '@/utils/date';

function useCalendar() {
  const [currentDateTime, setCurrentDateTime] = useState(getToday());
  const [calendar, setCalendar] = useState(getCurrentCalendar(currentDateTime));

  const startDateTime = calendar[0];
  const endDateTime = calendar[calendar.length - 1];

  const moveToBeforeMonth = () => {
    const beforeMonthDateTime = getMonthOffsetDateTime(currentDateTime, -1);

    setCurrentDateTime(beforeMonthDateTime);
    setCalendar(getCurrentCalendar(beforeMonthDateTime));
  };

  const moveToToday = () => {
    const today = getToday();

    setCurrentDateTime(today);
    setCalendar(getCurrentCalendar(today));
  };

  const moveToNextMonth = () => {
    const afterMonthDateTime = getMonthOffsetDateTime(currentDateTime, 1);

    setCurrentDateTime(afterMonthDateTime);
    setCalendar(getCurrentCalendar(afterMonthDateTime));
  };

  return {
    calendar,
    currentDateTime,
    endDateTime,
    moveToBeforeMonth,
    moveToNextMonth,
    moveToToday,
    startDateTime,
  };
}

export default useCalendar;

import { useLayoutEffect, useRef, useState } from 'react';

import { SCHEDULE } from '@/constants/style';

import {
  extractDateTime,
  getCurrentCalendar,
  getMonthOffsetDateTime,
  getToday,
} from '@/utils/date';

import useRootFontSize from './useRootFontSize';

interface CalendarControllerType {
  calendar: string[];
  currentDateTime: string;
  currentDay: number;
  currentMonth: number;
  currentYear: number;
  dateCellRef: React.RefObject<HTMLDivElement>;
  endDateTime: string;
  maxScheduleCount: number;
  moveToBeforeMonth: () => void;
  moveToNextMonth: () => void;
  moveToToday: () => void;
  rowCount: number;
  startDateTime: string;
}

function useCalendar() {
  const dateCellRef = useRef<HTMLDivElement>(null);

  const [currentDateTime, setCurrentDateTime] = useState(getToday());
  const [calendar, setCalendar] = useState(getCurrentCalendar(currentDateTime));
  const [maxScheduleCount, setMaxScheduleCount] = useState(0);

  const rootFontSize = useRootFontSize();

  const startDateTime = calendar[0];
  const endDateTime = calendar[calendar.length - 1];

  useLayoutEffect(() => {
    if (!(dateCellRef.current instanceof HTMLDivElement)) return;

    setMaxScheduleCount(
      Math.floor(
        (Math.floor(dateCellRef.current.clientHeight / 10) * 10 - SCHEDULE.HEIGHT * rootFontSize) /
          (SCHEDULE.HEIGHT_WITH_MARGIN * rootFontSize)
      )
    );
  }, [startDateTime, rootFontSize]);

  const {
    year: currentYear,
    month: currentMonth,
    day: currentDay,
  } = extractDateTime(currentDateTime);

  const rowCount = Math.ceil(calendar.length / 7);

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
    currentDay,
    currentMonth,
    currentYear,
    dateCellRef,
    endDateTime,
    maxScheduleCount,
    moveToBeforeMonth,
    moveToNextMonth,
    moveToToday,
    rowCount,
    startDateTime,
  };
}

export default useCalendar;

export { CalendarControllerType };

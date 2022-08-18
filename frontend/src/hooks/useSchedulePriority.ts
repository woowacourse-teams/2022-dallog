import { ScheduleType } from '@/@types/schedule';

import { CALENDAR } from '@/constants';

import { getFormattedDate, getISODateString } from '@/utils/date';

interface DateType {
  year: number;
  month: number;
  date: number;
  day: number;
}

function useSchedulePriority(calendarMonth: DateType[]) {
  const calendarInfoWithPriority = calendarMonth.reduce(
    (
      acc: {
        [key: string]: Array<boolean>;
      },
      cur: DateType
    ) => {
      const { year, month, date } = cur;

      acc[getFormattedDate(year, month, date)] = new Array(CALENDAR.MAX_VIEW).fill(false);

      return acc;
    },
    {}
  );

  const getLongTermsPriority = (longTerms: Array<ScheduleType>) =>
    longTerms.map((el) => {
      const startDate = getISODateString(el.startDateTime);
      const endDate = getISODateString(el.endDateTime);

      const scheduleRange = calendarMonth
        .filter((el) => {
          const date = getFormattedDate(el.year, el.month, el.date);

          return startDate <= date && date <= endDate;
        })
        .map((el) => getFormattedDate(el.year, el.month, el.date));

      if (!calendarInfoWithPriority.hasOwnProperty(scheduleRange[0])) {
        return {
          schedule: el,
          priority: CALENDAR.MAX_VIEW + 1,
        };
      }

      const priorityPosition = calendarInfoWithPriority[scheduleRange[0]].findIndex(
        (el) => el === false
      );

      if (priorityPosition === -1) {
        return {
          schedule: el,
          priority: CALENDAR.MAX_VIEW + 1,
        };
      }

      scheduleRange.forEach((el) => {
        if (calendarInfoWithPriority.hasOwnProperty(el)) {
          calendarInfoWithPriority[el][priorityPosition] = true;
        }
      });

      return {
        schedule: el,
        priority: priorityPosition + 1,
      };
    });

  const getAllDaysPriority = (allDays: Array<ScheduleType>) =>
    allDays.map((el) => {
      const startDate = getISODateString(el.startDateTime);
      const priorityPosition = calendarInfoWithPriority[startDate].findIndex((el) => el === false);

      if (priorityPosition === -1) {
        return {
          schedule: el,
          priority: CALENDAR.MAX_VIEW + 1,
        };
      }

      calendarInfoWithPriority[startDate][priorityPosition] = true;

      return {
        schedule: el,
        priority: priorityPosition + 1,
      };
    });

  const getFewHoursPriority = (fewHours: Array<ScheduleType>) =>
    fewHours.map((el) => {
      const startDate = getISODateString(el.startDateTime);
      const priorityPosition = calendarInfoWithPriority[startDate].findIndex((el) => el === false);

      if (priorityPosition === -1) {
        return {
          schedule: el,
          priority: CALENDAR.MAX_VIEW + 1,
        };
      }

      calendarInfoWithPriority[startDate][priorityPosition] = true;

      return {
        schedule: el,
        priority: priorityPosition + 1,
      };
    });

  return {
    getLongTermsPriority,
    getAllDaysPriority,
    getFewHoursPriority,
  };
}

export default useSchedulePriority;

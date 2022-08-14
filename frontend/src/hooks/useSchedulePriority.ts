import { ScheduleType } from '@/@types/schedule';

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

      acc[getFormattedDate(year, month, date)] = [false, false, false];

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

      if (!calendarInfoWithPriority.hasOwnProperty(startDate)) {
        return {
          schedule: el,
          priority: 4,
        };
      }

      if (calendarInfoWithPriority[startDate][0] === false) {
        scheduleRange.forEach((el) => {
          calendarInfoWithPriority[el][0] = true;
        });

        return {
          schedule: el,
          priority: 1,
        };
      }

      if (calendarInfoWithPriority[startDate][1] === false) {
        scheduleRange.forEach((el) => {
          calendarInfoWithPriority[el][1] = true;
        });

        return {
          schedule: el,
          priority: 2,
        };
      }

      if (calendarInfoWithPriority[startDate][2] === false) {
        scheduleRange.forEach((el) => {
          calendarInfoWithPriority[el][2] = true;
        });

        return {
          schedule: el,
          priority: 3,
        };
      }

      return {
        schedule: el,
        priority: 4,
      };
    });

  const getAllDaysPriority = (allDays: Array<ScheduleType>) =>
    allDays.map((el) => {
      const startDate = getISODateString(el.startDateTime);

      if (calendarInfoWithPriority[startDate][0] === false) {
        calendarInfoWithPriority[startDate][0] = true;

        return {
          schedule: el,
          priority: 1,
        };
      }

      if (calendarInfoWithPriority[startDate][1] === false) {
        calendarInfoWithPriority[startDate][1] = true;

        return {
          schedule: el,
          priority: 2,
        };
      }

      if (calendarInfoWithPriority[startDate][2] === false) {
        calendarInfoWithPriority[startDate][2] = true;

        return {
          schedule: el,
          priority: 3,
        };
      }

      return {
        schedule: el,
        priority: 4,
      };
    });

  const getFewHoursPriority = (fewHours: Array<ScheduleType>) =>
    fewHours.map((el) => {
      const startDate = getISODateString(el.startDateTime);

      if (calendarInfoWithPriority[startDate][0] === false) {
        calendarInfoWithPriority[startDate][0] = true;

        return {
          schedule: el,
          priority: 1,
        };
      }

      if (calendarInfoWithPriority[startDate][1] === false) {
        calendarInfoWithPriority[startDate][1] = true;

        return {
          schedule: el,
          priority: 2,
        };
      }

      if (calendarInfoWithPriority[startDate][2] === false) {
        calendarInfoWithPriority[startDate][2] = true;

        return {
          schedule: el,
          priority: 3,
        };
      }

      return {
        schedule: el,
        priority: 4,
      };
    });

  return {
    getLongTermsPriority,
    getAllDaysPriority,
    getFewHoursPriority,
  };
}

export default useSchedulePriority;

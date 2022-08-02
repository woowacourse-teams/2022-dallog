import { ScheduleType } from '@/@types/schedule';

import { getFormattedDate } from '@/utils/date';

interface DateType {
  year: number;
  month: number;
  date: number;
  day: number;
}

function useSchedulePriority(calendarMonth: Array<DateType>) {
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

  const getLongTermsPriority = (
    longTerms: Array<{ categoryId: number; color: string } & ScheduleType>
  ) => {
    return longTerms.map((el) => {
      const startDate = el.startDateTime.split('T')[0];
      const endDate = el.endDateTime.split('T')[0];

      const scheduleRange = calendarMonth
        .filter((el) => {
          const date = getFormattedDate(el.year, el.month, el.date);
          if (startDate <= date && date <= endDate) {
            return true;
          }
        })
        .map((el) => getFormattedDate(el.year, el.month, el.date));

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
  };

  const getAllDaysPriority = (
    allDays: Array<{ categoryId: number; color: string } & ScheduleType>
  ) => {
    return allDays.map((el) => {
      const startDate = el.startDateTime.split('T')[0];

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
  };

  const getFewHoursPriority = (
    fewHours: Array<{ categoryId: number; color: string } & ScheduleType>
  ) => {
    return fewHours.map((el) => {
      const startDate = el.startDateTime.split('T')[0];

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
  };

  return {
    getLongTermsPriority,
    getAllDaysPriority,
    getFewHoursPriority,
  };
}
export default useSchedulePriority;

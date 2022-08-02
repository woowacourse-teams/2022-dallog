import { zeroFill } from '.';

const getNextYearMonth = (targetYear: number, targetMonth: number) => {
  if (targetMonth === 12) {
    return { year: targetYear + 1, month: 1 };
  }

  return { year: targetYear, month: targetMonth + 1 };
};

const getBeforeYearMonth = (targetYear: number, targetMonth: number) => {
  if (targetMonth === 1) {
    return { year: targetYear - 1, month: 12 };
  }

  return { year: targetYear, month: targetMonth - 1 };
};

const getThisYear = () => {
  return new Date().getFullYear();
};

const getThisMonth = () => {
  return new Date().getMonth() + 1;
};

const getDate = () => {
  return new Date(+new Date() + 3240 * 10000).toISOString().split('T')[0];
};

const getDateTime = () => {
  return new Date(+new Date() + 3240 * 10000).toISOString().replace(/\..*/, '').slice(0, -3);
};

const getDayFromFormattedDate = (date: string) => {
  const newDate = new Date(`${date}T00:00:00Z`);

  return newDate.getDay();
};

const getBeforeDate = (targetDay: Date, offset: number) =>
  new Date(targetDay.setDate(targetDay.getDate() - offset));

const getNextDate = (targetDay: Date, offset: number) =>
  new Date(targetDay.setDate(targetDay.getDate() + offset));

const getCalendarMonth = (year: number, month: number) => {
  const firstDate = new Date(year, month - 1, 1);

  const calendarInfo: Date[] = [];

  while (firstDate.getMonth() === month - 1) {
    calendarInfo.push(new Date(firstDate));
    firstDate.setDate(firstDate.getDate() + 1);
  }

  const firstDay = calendarInfo[0].getDay();
  const lastDay = calendarInfo[calendarInfo.length - 1].getDay();

  if (firstDay !== 0) {
    Array(firstDay)
      .fill(0)
      .forEach((_, idx) => {
        calendarInfo.unshift(getBeforeDate(new Date(year, month - 1, 1), idx + 1));
      });
  }

  if (lastDay !== 6) {
    Array(6 - lastDay)
      .fill(0)
      .forEach((_, idx) => {
        calendarInfo.push(getNextDate(new Date(year, month, 0), idx + 1));
      });
  }

  return calendarInfo.map((el) => {
    return {
      year: el.getFullYear(),
      month: el.getMonth() + 1,
      date: el.getDate(),
      day: el.getDay(),
    };
  });
};

const getFormattedDate = (year: number | string, month: number | string, date: number | string) => {
  return `${year}-${zeroFill(month.toString())}-${zeroFill(date.toString())}`;
};

export {
  getBeforeDate,
  getBeforeYearMonth,
  getCalendarMonth,
  getDate,
  getDateTime,
  getDayFromFormattedDate,
  getFormattedDate,
  getThisYear,
  getThisMonth,
  getNextDate,
  getNextYearMonth,
};

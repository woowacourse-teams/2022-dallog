import { CalendarType } from '@/@types/calendar';

import { DATE_TIME } from '@/constants/date';

import { zeroFill } from '.';

const checkAllDay = (startDateTime?: string, endDateTime?: string) => {
  if (startDateTime === undefined || endDateTime === undefined) {
    return null;
  }

  return startDateTime.includes(DATE_TIME.START) && endDateTime.includes(DATE_TIME.END);
};

const getBeforeDate = (targetDay: Date, offset: number) =>
  new Date(targetDay.setDate(targetDay.getDate() - offset));

const getBeforeYearMonth = (targetYear: number, targetMonth: number) => {
  if (targetMonth === 1) {
    return { year: targetYear - 1, month: 12 };
  }

  return { year: targetYear, month: targetMonth - 1 };
};

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

const getDate = (dateInfo?: Omit<CalendarType, 'day'>) => {
  if (!dateInfo) {
    return getISODateString(new Date(+new Date() + 3240 * 10000).toISOString());
  }

  const { year, month, date } = dateInfo;

  return getISODateString(new Date(+new Date(year, month - 1, date) + 3240 * 10000).toISOString());
};

const getDateTime = (dateInfo?: Omit<CalendarType, 'day'>) => {
  if (!dateInfo) {
    return new Date(+new Date() + 3240 * 10000).toISOString().replace(/\..*/, '').slice(0, -3);
  }

  const { year, month, date } = dateInfo;

  return new Date(+new Date(year, month - 1, date) + 3240 * 10000)
    .toISOString()
    .replace(/\..*/, '')
    .slice(0, -3);
};

const getStartTime = () => {
  const [nowHour, nowMinute] = getDateTime().split('T')[1].split(':');

  if (nowMinute === '00' || nowMinute === '30') return `${zeroFill(nowHour)}:${nowMinute}`;

  if (nowMinute < '30') return `${zeroFill(nowHour)}:30`;

  if (nowHour >= '23') return '00:00';

  return `${zeroFill(+nowHour + 1)}:00`;
};

const getEndTime = (startTime?: string) => {
  const [nowHour, nowMinute] =
    startTime === undefined ? getStartTime().split(':') : startTime.split(':');

  return nowHour < '23' ? `${zeroFill(+nowHour + 1)}:${nowMinute}` : `00:${nowMinute}`;
};

const getDayFromFormattedDate = (date: string) => {
  return new Date(date).getDay();
};

const getFormattedDate = (year: number | string, month: number | string, date: number | string) => {
  return `${year}-${zeroFill(month.toString())}-${zeroFill(date.toString())}`;
};

const getISODateString = (ISOString: string) => {
  return ISOString.split('T')[0];
};

const getISOTimeString = (ISOString: string) => {
  return ISOString.split('T')[1];
};

const getKoreaISOString = (time: number) => {
  return new Date(time - new Date().getTimezoneOffset() * 60000).toISOString();
};

const getNextDate = (targetDay: Date, offset: number) =>
  new Date(targetDay.setDate(targetDay.getDate() + offset));

const getNextYearMonth = (targetYear: number, targetMonth: number) => {
  if (targetMonth === 12) {
    return { year: targetYear + 1, month: 1 };
  }

  return { year: targetYear, month: targetMonth + 1 };
};

const getOneHourEarlierISOString = (ISOString: string) => {
  const hour = getISOTimeString(ISOString).split(':')[0];

  const oneHourEarlierISOString = getKoreaISOString(new Date(ISOString).setHours(Number(hour) - 1))
    .replace(/\..*/, '')
    .slice(0, -3);

  return oneHourEarlierISOString;
};

const getOneHourLaterISOString = (ISOString: string) => {
  const hour = getISOTimeString(ISOString).split(':')[0];

  const oneHourEarlierISOString = getKoreaISOString(new Date(ISOString).setHours(Number(hour) + 1))
    .replace(/\..*/, '')
    .slice(0, -3);

  return oneHourEarlierISOString;
};

const getThisDate = () => {
  return new Date().getDate();
};

const getThisMonth = () => {
  return new Date().getMonth() + 1;
};

const getThisYear = () => {
  return new Date().getFullYear();
};

export {
  checkAllDay,
  getBeforeDate,
  getBeforeYearMonth,
  getCalendarMonth,
  getDate,
  getDateTime,
  getDayFromFormattedDate,
  getEndTime,
  getFormattedDate,
  getISODateString,
  getISOTimeString,
  getKoreaISOString,
  getNextDate,
  getNextYearMonth,
  getOneHourEarlierISOString,
  getOneHourLaterISOString,
  getStartTime,
  getThisDate,
  getThisMonth,
  getThisYear,
};

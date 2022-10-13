import { DATE_TIME } from '@/constants/date';

import { zeroFill } from '.';

const checkAllDay = (startDateTime: string, endDateTime: string) =>
  startDateTime < endDateTime && getISOTimeString(endDateTime).startsWith(DATE_TIME.END);

const getStartTime = () => {
  const nowDateTime = new Date(+new Date() + 3240 * 10000);
  const nowHour = nowDateTime.getUTCHours();
  const nowMinute = nowDateTime.getUTCMinutes();

  if (nowMinute === 0 || nowMinute === 30) return `${zeroFill(nowHour)}:${nowMinute}`;

  if (nowMinute < 30) return `${zeroFill(nowHour)}:30`;

  if (nowHour >= 23) return '00:00';

  return `${zeroFill(+nowHour + 1)}:00`;
};

const getEndTime = (startTime?: string) => {
  const [nowHour, nowMinute] =
    startTime === undefined ? getStartTime().split(':') : startTime.split(':');

  return nowHour < '23' ? `${zeroFill(+nowHour + 1)}:${nowMinute}` : `00:${nowMinute}`;
};

const getNextDate = (targetDay: Date, offset: number) =>
  new Date(targetDay.setDate(targetDay.getDate() + offset));

const getThisDate = () => {
  return new Date().getDate();
};

const getThisMonth = () => {
  return new Date().getMonth() + 1;
};

const extractDateTime = (dateTime: string) => {
  const dateTimeObject = new Date(dateTime);

  return {
    year: dateTimeObject.getFullYear(),
    month: dateTimeObject.getMonth() + 1,
    date: dateTimeObject.getDate(),
    day: dateTimeObject.getDay(),
  };
};

const getCurrentCalendar = (currentDateTime: string) => {
  const firstDateTime = new Date(new Date(currentDateTime).setDate(1));
  const dateTime = new Date(new Date(currentDateTime).setDate(1));
  const calendarInfo: string[] = [];

  while (extractDateTime(getISOString(dateTime)).month === extractDateTime(currentDateTime).month) {
    calendarInfo.push(getISOString(dateTime));
    dateTime.setDate(extractDateTime(getISOString(dateTime)).date + 1);
  }

  const firstDay = extractDateTime(calendarInfo[0]).day;
  const lastDay = extractDateTime(calendarInfo[calendarInfo.length - 1]).day;

  if (firstDay !== 0) {
    Array(firstDay)
      .fill(0)
      .forEach((_, idx) => {
        calendarInfo.unshift(getDayOffsetDateTime(getISOString(firstDateTime), -(idx + 1)));
      });
  }

  if (lastDay !== 6) {
    Array(6 - lastDay)
      .fill(0)
      .forEach((_, idx) => {
        calendarInfo.push(getDayOffsetDateTime(getISOString(dateTime), idx));
      });
  }

  return calendarInfo;
};

const getDayOffsetDateTime = (dateTime: string, offset: number) =>
  getISOString(new Date(new Date(dateTime).setDate(extractDateTime(dateTime).date + offset)));

const getISODateString = (ISOString: string) => ISOString.split('T')[0];

const getISOString = (date: Date) => {
  const offset = 1000 * 60 * 60 * 9;
  const koreaDateTime = new Date(date.getTime() + offset);

  return koreaDateTime.toISOString().split('.')[0].slice(0, -3);
};

const getISOTimeString = (ISOString: string) => ISOString.split('T')[1];

const getMonthOffsetDateTime = (dateTime: string, offset: number) =>
  getISOString(new Date(new Date(dateTime).setMonth(extractDateTime(dateTime).month + offset - 1)));

const getToday = () => {
  const offset = new Date().getTimezoneOffset() * 60 * 1000;

  return `${getISODateString(getISOString(new Date(Date.now() - offset)))}T${DATE_TIME.START}`;
};

export {
  checkAllDay,
  extractDateTime,
  getCurrentCalendar,
  getDayOffsetDateTime,
  getEndTime,
  getISODateString,
  getISOString,
  getISOTimeString,
  getMonthOffsetDateTime,
  getNextDate,
  getStartTime,
  getThisDate,
  getThisMonth,
  getToday,
};

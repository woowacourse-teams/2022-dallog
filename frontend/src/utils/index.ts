import { InputRef } from '@/@types';

const createPostBody = (inputRef: InputRef) => {
  const inputElements = Object.values(inputRef).map((el) => el.current);
  const isValidInputRefs = inputElements.every((el) => el instanceof HTMLInputElement);

  if (!isValidInputRefs) {
    return;
  }

  const inputRefKeys = Object.keys(inputRef);

  const body = inputRefKeys.reduce((acc: any, cur: number | string) => {
    acc[cur] = (inputRef[cur].current as HTMLInputElement).value;

    return acc;
  }, {});

  return body;
};

const getDate = () => {
  return new Date(+new Date() + 3240 * 10000).toISOString().split('T')[0];
};

const getDateTime = () => {
  return new Date(+new Date() + 3240 * 10000).toISOString().replace(/\..*/, '').slice(0, -3);
};

const getBeforeDate = (targetDay: Date, offset: number) =>
  new Date(targetDay.setDate(targetDay.getDate() - offset));

const getNextDate = (targetDay: Date, offset: number) =>
  new Date(targetDay.setDate(targetDay.getDate() + offset));

const getCalendarMonth = (month: number, year: number) => {
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

export { createPostBody, getDate, getDateTime, getCalendarMonth, getBeforeDate, getNextDate };

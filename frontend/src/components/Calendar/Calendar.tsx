import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useEffect } from 'react';
import { useQuery } from 'react-query';

import useCalendar from '@/hooks/useCalendar';

import { ScheduleType } from '@/@types/schedule';

import Button from '@/components/@common/Button/Button';

import { CACHE_KEY, DAYS } from '@/constants';

import scheduleApi from '@/api/schedule';

import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';

import {
  calendarGrid,
  calendarHeader,
  dateBorder,
  dateText,
  dayBar,
  monthPicker,
  navBarGrid,
  navButton,
  navButtonTitle,
  todayButton,
} from './Calendar.styles';

function Calendar() {
  const theme = useTheme();

  const { calendarMonth, current, moveToBeforeMonth, moveToToday, moveToNextMonth } = useCalendar();

  const { isLoading, error, data, refetch } = useQuery<
    AxiosResponse<{ schedules: ScheduleType[] }>,
    AxiosError
  >(CACHE_KEY.SCHEDULES, () => scheduleApi.get(current.year, current.month));

  const rowNum = Math.ceil(calendarMonth.length / 7);

  useEffect(() => {
    refetch();
  }, [current]);

  if (isLoading || data === undefined) {
    return <>Loading</>;
  }

  if (error) {
    return <>Error</>;
  }

  return (
    <>
      <div css={calendarHeader(theme)}>
        <span>
          {current.year}년 {current.month}월
        </span>
        <div css={monthPicker}>
          <Button cssProp={navButton} onClick={moveToBeforeMonth}>
            <AiOutlineLeft />
            <span css={navButtonTitle}>전 달</span>
          </Button>
          <Button cssProp={todayButton} onClick={moveToToday}>
            오늘
          </Button>
          <Button cssProp={navButton} onClick={moveToNextMonth}>
            <AiOutlineRight />
            <span css={navButtonTitle}>다음 달</span>
          </Button>
        </div>
      </div>
      <div css={navBarGrid}>
        {DAYS.map((day) => (
          <span key={day} css={dayBar(theme, day)}>
            {day}
          </span>
        ))}
      </div>
      <div css={calendarGrid(rowNum)}>
        {calendarMonth.map((info) => {
          const key = `${info.year}${info.month}${info.date}${info.day}`;

          return (
            <div key={key} css={dateBorder(theme, info.day)}>
              <span css={dateText(theme, info.day, current.month === info.month)}>{info.date}</span>
            </div>
          );
        })}
      </div>
    </>
  );
}

export default Calendar;

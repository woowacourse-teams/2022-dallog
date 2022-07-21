import { AxiosError, AxiosResponse } from 'axios';
import { useEffect, useState } from 'react';
import { useQuery } from 'react-query';

import useToggle from '@/hooks/useToggle';

import { Schedule } from '@/@types';

import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import Calendar from '@/components/Calendar/Calendar';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';

import { CACHE_KEY } from '@/constants';

import { getThisMonth, getThisYear } from '@/utils/date';

import scheduleApi from '@/api/schedule';

import { calendarPage } from './CalendarPage.styles';

function CalendarPage() {
  const [current, setCurrent] = useState({
    year: getThisYear(),
    month: getThisMonth(),
  });

  const {
    isLoading,
    error,
    data: schedulesGetResponse,
    refetch: refetchSchedules,
  } = useQuery<AxiosResponse<{ schedules: Schedule[] }>, AxiosError>(CACHE_KEY.SCHEDULES, () =>
    scheduleApi.get(current.year, current.month)
  );

  const { state: isCalendarAddModalOpen, toggleState: toggleCalendarAddModalOpen } = useToggle();

  useEffect(() => {
    refetchSchedules();
  }, [current]);

  if (isLoading || schedulesGetResponse === undefined) {
    return <>Loading</>;
  }

  if (error) {
    return <>Error</>;
  }

  return (
    <div css={calendarPage}>
      <Calendar current={current} setCurrent={setCurrent} />
      <ModalPortal isOpen={isCalendarAddModalOpen} closeModal={toggleCalendarAddModalOpen}>
        <ScheduleAddModal refetch={refetchSchedules} closeModal={toggleCalendarAddModalOpen} />
      </ModalPortal>
      <ScheduleAddButton onClick={toggleCalendarAddModalOpen} />
    </div>
  );
}

export default CalendarPage;

import { useState } from 'react';

import { useQuery } from 'react-query';
import { AxiosError, AxiosResponse } from 'axios';

import useModal from '@/hooks/useModal';

import { Schedule } from '@/@types';

import PageLayout from '@/components/PageLayout/PageLayout';
import Calendar from '@/components/Calendar/Calendar';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';

import { CACHE_KEY } from '@/constants';

import scheduleApi from '@/api/schedule';

function CalendarPage() {
  const [schedules, setSchedules] = useState<Schedule[]>([]);
  const { isLoading, error } = useQuery<AxiosResponse<{ schedules: Schedule[] }>, AxiosError>(
    CACHE_KEY.SCHEDULES,
    scheduleApi.get,
    {
      onSuccess: (data) => onSuccessGetSchedules(data),
    }
  );

  const { isOpen, openModal, closeModal } = useModal();

  const onSuccessGetSchedules = (data: AxiosResponse<{ schedules: Schedule[] }>) => {
    if (typeof data !== 'undefined') {
      setSchedules(data.data.schedules);
    }
  };

  if (isLoading) return <>Loading</>;

  if (error) return <>Error</>;

  return (
    <PageLayout>
      <Calendar schedules={schedules} />
      <ModalPortal isOpen={isOpen} closeModal={closeModal}>
        <ScheduleAddModal setSchedule={setSchedules} closeModal={closeModal} />
      </ModalPortal>
      <ScheduleAddButton onClick={openModal} />
    </PageLayout>
  );
}

export default CalendarPage;

import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';

import useModal from '@/hooks/useModal';

import { Schedule } from '@/@types';

import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import Calendar from '@/components/Calendar/Calendar';
import PageLayout from '@/components/PageLayout/PageLayout';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';

import { CACHE_KEY } from '@/constants';

import scheduleApi from '@/api/schedule';

function CalendarPage() {
  const {
    isLoading,
    error,
    data: schedulesGetResponse,
    refetch: refetchSchedules,
  } = useQuery<AxiosResponse<{ schedules: Schedule[] }>, AxiosError>(
    CACHE_KEY.SCHEDULES,
    scheduleApi.get
  );

  const { isOpen, openModal, closeModal } = useModal();

  if (isLoading || schedulesGetResponse === undefined) {
    return <>Loading</>;
  }

  if (error) {
    return <>Error</>;
  }

  return (
    <PageLayout>
      <Calendar schedules={schedulesGetResponse.data.schedules} />
      <ModalPortal isOpen={isOpen} closeModal={closeModal}>
        <ScheduleAddModal refetch={refetchSchedules} closeModal={closeModal} />
      </ModalPortal>
      <ScheduleAddButton onClick={openModal} />
    </PageLayout>
  );
}

export default CalendarPage;

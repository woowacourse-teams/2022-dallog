import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useNavigate } from 'react-router-dom';

import useModal from '@/hooks/useModal';

import { Schedule } from '@/@types';

import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import Calendar from '@/components/Calendar/Calendar';
import PageLayout from '@/components/PageLayout/PageLayout';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';

import { CACHE_KEY, PATH, STORAGE_KEY } from '@/constants';

import loginApi from '@/api/login';
import scheduleApi from '@/api/schedule';

function CalendarPage() {
  const navigate = useNavigate();

  const { data: accessToken } = useQuery<string>(CACHE_KEY.AUTH, loginApi.auth, {
    retry: false,
    onSuccess: (data) => onSuccessAuth(data),
  });

  const {
    isLoading,
    error,
    data: schedulesGetResponse,
    refetch: refetchSchedules,
  } = useQuery<AxiosResponse<{ schedules: Schedule[] }>, AxiosError>(
    CACHE_KEY.SCHEDULES,
    scheduleApi.get,
    {
      enabled: !!accessToken,
    }
  );

  const { isOpen, openModal, closeModal } = useModal();

  const onSuccessAuth = (accessToken: string) => {
    localStorage.setItem(STORAGE_KEY.ACCESS_TOKEN, accessToken);
    navigate(PATH.CALENDAR_PAGE);
  };

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

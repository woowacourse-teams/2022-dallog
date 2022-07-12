import { useEffect, useState } from 'react';

import useModal from '@/hooks/useModal';

import { Schedule } from '@/@types';

import PageLayout from '@/components/PageLayout/PageLayout';
import Calendar from '@/components/Calendar/Calendar';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';

function CalendarPage() {
  const [schedules, setSchedules] = useState<Schedule[]>([]);

  const { isOpen, openModal, closeModal } = useModal();

  useEffect(() => {
    const fetchSchedules = async () => {
      const response = await fetch('/api/schedules', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
      });
      const { data } = await response.json();

      setSchedules(data);
    };

    fetchSchedules();
  }, []);

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

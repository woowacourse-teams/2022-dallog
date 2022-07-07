import useModal from '@/hooks/useModal';

import PageLayout from '@/components/PageLayout/PageLayout';
import Calendar from '@/components/Calendar/Calendar';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';

function CalendarPage() {
  const { isOpen, openModal, closeModal } = useModal();

  return (
    <PageLayout>
      <Calendar />
      <ModalPortal isOpen={isOpen} closeModal={closeModal}>
        <ScheduleAddModal closeModal={closeModal} />
      </ModalPortal>
      <ScheduleAddButton onClick={openModal} />
    </PageLayout>
  );
}

export default CalendarPage;

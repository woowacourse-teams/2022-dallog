import useToggle from '@/hooks/useToggle';

import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Calendar from '@/components/Calendar/Calendar';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';

import { calendarPage } from './CalendarPage.styles';

function CalendarPage() {
  const { state: isCalendarAddModalOpen, toggleState: toggleCalendarAddModalOpen } = useToggle();

  return (
    <PageLayout>
      <div css={calendarPage}>
        <Calendar />
        <ModalPortal isOpen={isCalendarAddModalOpen} closeModal={toggleCalendarAddModalOpen}>
          <ScheduleAddModal closeModal={toggleCalendarAddModalOpen} />
        </ModalPortal>
        <ScheduleAddButton onClick={toggleCalendarAddModalOpen} />
      </div>
    </PageLayout>
  );
}

export default CalendarPage;

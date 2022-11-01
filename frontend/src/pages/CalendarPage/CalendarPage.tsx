import { useState } from 'react';

import { useGetSchedules } from '@/hooks/@queries/schedule';
import useCalendar from '@/hooks/useCalendar';
import useToggle from '@/hooks/useToggle';

import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Calendar from '@/components/Calendar/Calendar';
import CalendarFallback from '@/components/Calendar/Calendar.fallback';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';

import { PAGE_LAYOUT } from '@/constants/style';

import { getToday } from '@/utils/date';

import { calendarPageStyle } from './CalendarPage.styles';

function CalendarPage() {
  const [dateInfo, setDateInfo] = useState('');

  const calendarController = useCalendar();
  const { startDateTime, endDateTime } = calendarController;

  const { state: isScheduleAddModalOpen, toggleState: toggleScheduleAddModalOpen } = useToggle();

  const { isLoading, data } = useGetSchedules({ startDateTime, endDateTime });

  const handleClickScheduleAddButton = () => {
    setDateInfo(getToday());
    toggleScheduleAddModalOpen();
  };

  return (
    <PageLayout type={PAGE_LAYOUT.SIDEBAR}>
      <div css={calendarPageStyle}>
        {(isLoading || data === undefined) && (
          <CalendarFallback
            calendarController={calendarController}
            setDateInfo={setDateInfo}
            handleClickDateCell={toggleScheduleAddModalOpen}
          />
        )}
        {data && (
          <Calendar
            calendarController={calendarController}
            scheduleResponse={data}
            setDateInfo={setDateInfo}
            handleClickDateCell={toggleScheduleAddModalOpen}
          />
        )}
        {dateInfo && (
          <ModalPortal isOpen={isScheduleAddModalOpen} closeModal={toggleScheduleAddModalOpen}>
            <ScheduleAddModal dateInfo={dateInfo} closeModal={toggleScheduleAddModalOpen} />
          </ModalPortal>
        )}
        <ScheduleAddButton onClick={handleClickScheduleAddButton} />
      </div>
    </PageLayout>
  );
}

export default CalendarPage;

import { useState } from 'react';

import useModalPosition from '@/hooks/useModalPosition';
import useToggle from '@/hooks/useToggle';

import { ScheduleType } from '@/@types/schedule';

import theme from '@/styles/theme';

import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import MoreScheduleModal from '@/components/MoreScheduleModal/MoreScheduleModal';
import ScheduleModal from '@/components/ScheduleModal/ScheduleModal';
import ScheduleModifyModal from '@/components/ScheduleModifyModal/ScheduleModifyModal';

import { CALENDAR } from '@/constants';
import { TRANSPARENT } from '@/constants/style';

import {
  checkAllDay,
  extractDateTime,
  getDayOffsetDateTime,
  getISODateString,
  getToday,
} from '@/utils/date';

import {
  dateCellStyle,
  dateTextStyle,
  itemWithBackgroundStyle,
  itemWithoutBackgroundStyle,
  moreStyle,
} from './DateCell.styles';

interface DateCellProps {
  dateTime: string;
  currentMonth: number;
  dateCellRef: React.RefObject<HTMLDivElement>;
  setDateInfo: React.Dispatch<React.SetStateAction<string>>;
  toggleScheduleAddModalOpen: () => void;
  hoveringScheduleId?: string;
  setHoveringScheduleId?: React.Dispatch<React.SetStateAction<string>>;
  calendarWithPriority?: Record<string, boolean[]>;
  schedulesWithPriority?: Record<
    | 'longTermSchedulesWithPriority'
    | 'allDaySchedulesWithPriority'
    | 'fewHourSchedulesWithPriority',
    {
      schedule: ScheduleType;
      priority: null | number;
    }[]
  >;
  maxScheduleCount?: number;
}

function DateCell({
  dateTime,
  currentMonth,
  dateCellRef,
  setDateInfo,
  toggleScheduleAddModalOpen,
  hoveringScheduleId,
  setHoveringScheduleId,
  calendarWithPriority,
  schedulesWithPriority,
  maxScheduleCount,
}: DateCellProps) {
  const [scheduleInfo, setScheduleInfo] = useState<ScheduleType | null>(null);
  const [moreScheduleDateTime, setMoreScheduleDateTime] = useState('');

  const { state: isScheduleModifyModalOpen, toggleState: toggleScheduleModifyModalOpen } =
    useToggle();

  const scheduleModal = useModalPosition();
  const moreScheduleModal = useModalPosition();

  const { month, date, day } = extractDateTime(dateTime);

  const handleClickDate = (e: React.MouseEvent, info: string) => {
    if (e.target !== e.currentTarget) {
      return;
    }

    setDateInfo(info);
    toggleScheduleAddModalOpen();
  };

  if (
    !setHoveringScheduleId ||
    !calendarWithPriority ||
    !schedulesWithPriority ||
    !maxScheduleCount
  ) {
    return (
      <div
        css={dateCellStyle(theme, day)}
        onClick={(e) => handleClickDate(e, dateTime)}
        ref={dateCellRef}
      >
        <span css={dateTextStyle(theme, day, currentMonth === month, dateTime === getToday())}>
          {date}
        </span>
      </div>
    );
  }

  const {
    longTermSchedulesWithPriority,
    allDaySchedulesWithPriority,
    fewHourSchedulesWithPriority,
  } = schedulesWithPriority;

  const currentDate = getISODateString(dateTime);

  const priorityPosition = calendarWithPriority[getISODateString(dateTime)].findIndex(
    (priority) => !priority
  );
  const hasMoreSchedule = priorityPosition === -1 || priorityPosition + 1 > maxScheduleCount;

  const onMouseEnter = (scheduleId: string) => {
    setHoveringScheduleId(scheduleId);
  };

  const onMouseLeave = () => {
    setHoveringScheduleId('0');
  };

  return (
    <div css={dateCellStyle(theme, day)} onClick={(e) => handleClickDate(e, dateTime)}>
      <span css={dateTextStyle(theme, day, currentMonth === month, dateTime === getToday())}>
        {date}
      </span>

      {longTermSchedulesWithPriority.map(({ schedule, priority }) => {
        const startDate = getISODateString(schedule.startDateTime);
        const endDate = getISODateString(
          checkAllDay(schedule.startDateTime, schedule.endDateTime)
            ? getDayOffsetDateTime(schedule.endDateTime, -1)
            : schedule.endDateTime
        );
        const { day: currentDay } = extractDateTime(dateTime);

        if (!(startDate <= currentDate && currentDate <= endDate) || priority === null) return;

        return (
          <div
            key={`${currentDate}#${schedule.id}#longTerms`}
            css={itemWithBackgroundStyle(
              priority,
              schedule.colorCode,
              hoveringScheduleId === schedule.id,
              maxScheduleCount,
              currentDate === endDate
            )}
            onMouseEnter={() => onMouseEnter(schedule.id)}
            onClick={(e) => scheduleModal.handleClickOpen(e, () => setScheduleInfo(schedule))}
            onMouseLeave={onMouseLeave}
          >
            {(startDate === currentDate || currentDay === 0) &&
              (schedule.title.trim() || CALENDAR.EMPTY_TITLE)}
          </div>
        );
      })}

      {allDaySchedulesWithPriority.map(({ schedule, priority }) => {
        const startDate = getISODateString(schedule.startDateTime);

        if (startDate !== currentDate || priority === null) return;

        return (
          <div
            key={`${currentDate}#${schedule.id}#allDays`}
            css={itemWithBackgroundStyle(
              priority,
              schedule.colorCode,
              hoveringScheduleId === schedule.id,
              maxScheduleCount,
              true
            )}
            onMouseEnter={() => onMouseEnter(schedule.id)}
            onClick={(e) => scheduleModal.handleClickOpen(e, () => setScheduleInfo(schedule))}
            onMouseLeave={onMouseLeave}
          >
            {schedule.title.trim() || CALENDAR.EMPTY_TITLE}
          </div>
        );
      })}

      {fewHourSchedulesWithPriority.map(({ schedule, priority }) => {
        const startDate = getISODateString(schedule.startDateTime);

        if (startDate !== currentDate || priority === null) return;

        return (
          <div
            key={`${currentDate}#${schedule.id}#fewHours`}
            css={itemWithoutBackgroundStyle(
              theme,
              priority,
              schedule.colorCode,
              hoveringScheduleId === schedule.id,
              maxScheduleCount,
              false
            )}
            onMouseEnter={() => onMouseEnter(schedule.id)}
            onClick={(e) => scheduleModal.handleClickOpen(e, () => setScheduleInfo(schedule))}
            onMouseLeave={onMouseLeave}
          >
            {schedule.title.trim() || CALENDAR.EMPTY_TITLE}
          </div>
        );
      })}

      {hasMoreSchedule && (
        <span
          css={moreStyle}
          onClick={(e) =>
            moreScheduleModal.handleClickOpen(e, () => setMoreScheduleDateTime(dateTime))
          }
        >
          일정 더보기
        </span>
      )}

      {dateTime === moreScheduleDateTime && (
        <ModalPortal
          isOpen={moreScheduleModal.isModalOpen}
          closeModal={moreScheduleModal.toggleModalOpen}
          dimmerBackground={TRANSPARENT}
        >
          <MoreScheduleModal
            moreScheduleModalPos={moreScheduleModal.modalPos}
            moreScheduleDateTime={moreScheduleDateTime}
            longTermSchedulesWithPriority={longTermSchedulesWithPriority}
            allDaySchedulesWithPriority={allDaySchedulesWithPriority}
            fewHourSchedulesWithPriority={fewHourSchedulesWithPriority}
          />
        </ModalPortal>
      )}

      {scheduleInfo && (
        <>
          <ModalPortal
            isOpen={scheduleModal.isModalOpen}
            closeModal={scheduleModal.toggleModalOpen}
            dimmerBackground={TRANSPARENT}
          >
            <ScheduleModal
              scheduleModalPos={scheduleModal.modalPos}
              scheduleInfo={scheduleInfo}
              toggleScheduleModifyModalOpen={toggleScheduleModifyModalOpen}
              closeModal={scheduleModal.toggleModalOpen}
            />
          </ModalPortal>
          <ModalPortal
            isOpen={isScheduleModifyModalOpen}
            closeModal={toggleScheduleModifyModalOpen}
          >
            <ScheduleModifyModal
              scheduleInfo={scheduleInfo}
              closeModal={toggleScheduleModifyModalOpen}
            />
          </ModalPortal>
        </>
      )}
    </div>
  );
}

export default DateCell;

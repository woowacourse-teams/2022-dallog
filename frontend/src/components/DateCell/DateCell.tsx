import useModalPosition from '@/hooks/useModalPosition';

import { ScheduleType } from '@/@types/schedule';

import theme from '@/styles/theme';

import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import MoreScheduleModal from '@/components/MoreScheduleModal/MoreScheduleModal';
import Schedule from '@/components/Schedule/Schedule';

import { SCHEDULE } from '@/constants/schedule';
import { TRANSPARENT } from '@/constants/style';

import {
  checkAllDay,
  extractDateTime,
  getDayOffsetDateTime,
  getISODateString,
  getToday,
} from '@/utils/date';

import { dateCellStyle, dateTextStyle, moreStyle } from './DateCell.styles';

interface DateCellProps {
  dateTime: string;
  currentMonth: number;
  dateCellRef: React.RefObject<HTMLDivElement>;
  maxScheduleCount?: number;
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
  setDateInfo?: React.Dispatch<React.SetStateAction<string>>;
  onClick?: () => void;
  readonly?: boolean;
}

function DateCell({
  dateTime,
  currentMonth,
  dateCellRef,
  maxScheduleCount,
  calendarWithPriority,
  schedulesWithPriority,
  setDateInfo,
  onClick,
  readonly = false,
}: DateCellProps) {
  const moreScheduleModal = useModalPosition();

  const { month, date, day } = extractDateTime(dateTime);

  const isSchedulesLoaded = calendarWithPriority && schedulesWithPriority && maxScheduleCount;

  const handleClickDateCell = (e: React.MouseEvent, info: string) => {
    if (e.target !== e.currentTarget) {
      return;
    }

    setDateInfo && setDateInfo(info);
    onClick && onClick();
  };

  if (!isSchedulesLoaded) {
    return (
      <div
        css={dateCellStyle(theme, day, readonly)}
        ref={dateCellRef}
        {...(!readonly && { onClick: (e) => handleClickDateCell(e, dateTime) })}
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

  return (
    <div
      css={dateCellStyle(theme, day, readonly)}
      ref={dateCellRef}
      {...(!readonly && { onClick: (e) => handleClickDateCell(e, dateTime) })}
    >
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
          <Schedule
            key={`${SCHEDULE.RESPONSE_TYPE.LONG_TERMS}#${currentDate}#${schedule.id}`}
            type={SCHEDULE.RESPONSE_TYPE.LONG_TERMS}
            schedule={schedule}
            priority={priority}
            maxScheduleCount={maxScheduleCount}
            isEndDate={currentDate === endDate}
            isTitleVisible={startDate === currentDate || currentDay === 0}
            readonly={readonly}
          />
        );
      })}

      {allDaySchedulesWithPriority.map(({ schedule, priority }) => {
        const startDate = getISODateString(schedule.startDateTime);

        if (startDate !== currentDate || priority === null) return;

        return (
          <Schedule
            key={`${SCHEDULE.RESPONSE_TYPE.ALL_DAYS}#${currentDate}#${schedule.id}`}
            type={SCHEDULE.RESPONSE_TYPE.ALL_DAYS}
            schedule={schedule}
            priority={priority}
            maxScheduleCount={maxScheduleCount}
            isEndDate={true}
          />
        );
      })}

      {fewHourSchedulesWithPriority.map(({ schedule, priority }) => {
        const startDate = getISODateString(schedule.startDateTime);

        if (startDate !== currentDate || priority === null) return;

        return (
          <Schedule
            key={`${SCHEDULE.RESPONSE_TYPE.FEW_HOURS}#${currentDate}#${schedule.id}`}
            type={SCHEDULE.RESPONSE_TYPE.FEW_HOURS}
            schedule={schedule}
            priority={priority}
            maxScheduleCount={maxScheduleCount}
            isEndDate={false}
          />
        );
      })}

      {hasMoreSchedule && (
        <span css={moreStyle} onClick={moreScheduleModal.handleClickOpen}>
          일정 더보기
        </span>
      )}

      <ModalPortal
        isOpen={moreScheduleModal.isModalOpen}
        closeModal={moreScheduleModal.toggleModalOpen}
        dimmerBackground={TRANSPARENT}
      >
        <MoreScheduleModal
          moreScheduleModalPos={moreScheduleModal.modalPos}
          moreScheduleDateTime={dateTime}
          longTermSchedulesWithPriority={longTermSchedulesWithPriority}
          allDaySchedulesWithPriority={allDaySchedulesWithPriority}
          fewHourSchedulesWithPriority={fewHourSchedulesWithPriority}
          readonly={readonly}
        />
      </ModalPortal>
    </div>
  );
}

export default DateCell;

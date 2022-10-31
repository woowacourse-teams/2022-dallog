import { useTheme } from '@emotion/react';
import { useState } from 'react';

import useModalPosition from '@/hooks/useModalPosition';
import useToggle from '@/hooks/useToggle';

import { ModalPosType } from '@/@types';
import { ScheduleType } from '@/@types/schedule';

import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import ScheduleModal from '@/components/ScheduleModal/ScheduleModal';
import ScheduleModifyModal from '@/components/ScheduleModifyModal/ScheduleModifyModal';

import { CALENDAR } from '@/constants';
import { DAYS } from '@/constants/date';
import { TRANSPARENT } from '@/constants/style';

import {
  checkAllDay,
  extractDateTime,
  getDayOffsetDateTime,
  getISODateString,
  getThisDate,
  getThisMonth,
} from '@/utils/date';

import {
  dateTextStyle,
  dayTextStyle,
  headerStyle,
  itemWithBackgroundStyle,
  itemWithoutBackgroundStyle,
  moreScheduleModalStyle,
} from './MoreScheduleModal.styles';

interface MoreScheduleModalProps {
  moreScheduleModalPos: ModalPosType;
  moreScheduleDateTime: string;
  longTermSchedulesWithPriority: { schedule: ScheduleType; priority: number | null }[];
  allDaySchedulesWithPriority: { schedule: ScheduleType; priority: number | null }[];
  fewHourSchedulesWithPriority: { schedule: ScheduleType; priority: number | null }[];
}

function MoreScheduleModal({
  moreScheduleModalPos,
  moreScheduleDateTime,
  longTermSchedulesWithPriority,
  allDaySchedulesWithPriority,
  fewHourSchedulesWithPriority,
}: MoreScheduleModalProps) {
  const theme = useTheme();

  const [scheduleInfo, setScheduleInfo] = useState<ScheduleType | null>(null);

  const { state: isScheduleModifyModalOpen, toggleState: toggleScheduleModifyModalOpen } =
    useToggle();

  const scheduleModal = useModalPosition();

  const { month, date, day } = extractDateTime(moreScheduleDateTime);
  const nowDate = getISODateString(moreScheduleDateTime);

  return (
    <div css={moreScheduleModalStyle(theme, moreScheduleModalPos)}>
      <div css={headerStyle}>
        <span css={dayTextStyle(theme, day)}>{DAYS[day]}</span>
        <span css={dateTextStyle(theme, day, getThisMonth() === month && getThisDate() === date)}>
          {date}
        </span>
      </div>

      {longTermSchedulesWithPriority.map(({ schedule }) => {
        const startDate = getISODateString(schedule.startDateTime);
        const endDate = getISODateString(
          checkAllDay(schedule.startDateTime, schedule.endDateTime)
            ? getDayOffsetDateTime(schedule.endDateTime, -1)
            : schedule.endDateTime
        );

        return (
          startDate <= nowDate &&
          nowDate <= endDate && (
            <div
              key={`modal-${nowDate}#${schedule.id}`}
              css={itemWithBackgroundStyle(schedule.colorCode)}
              onClick={(e) => scheduleModal.handleClickOpen(e, () => setScheduleInfo(schedule))}
            >
              {schedule.title.trim() || CALENDAR.EMPTY_TITLE}
            </div>
          )
        );
      })}

      {allDaySchedulesWithPriority.map(({ schedule }) => {
        const startDate = getISODateString(schedule.startDateTime);

        return (
          startDate === nowDate && (
            <div
              key={`modal-${nowDate}#${schedule.id}`}
              css={itemWithBackgroundStyle(schedule.colorCode)}
              onClick={(e) => scheduleModal.handleClickOpen(e, () => setScheduleInfo(schedule))}
            >
              {schedule.title.trim() || CALENDAR.EMPTY_TITLE}
            </div>
          )
        );
      })}

      {fewHourSchedulesWithPriority.map(({ schedule }) => {
        const startDate = getISODateString(schedule.startDateTime);

        return (
          startDate === nowDate && (
            <div
              key={`modal-${nowDate}#${schedule.id}`}
              css={itemWithoutBackgroundStyle(theme, schedule.colorCode)}
              onClick={(e) => scheduleModal.handleClickOpen(e, () => setScheduleInfo(schedule))}
            >
              {schedule.title.trim() || CALENDAR.EMPTY_TITLE}
            </div>
          )
        );
      })}

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

export default MoreScheduleModal;

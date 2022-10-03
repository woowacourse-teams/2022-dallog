import { useTheme } from '@emotion/react';
import { useState } from 'react';

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
  extractDateTime,
  getFormattedDate,
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

  const [scheduleModalPos, setScheduleModalPos] = useState<ModalPosType>({});
  const [scheduleInfo, setScheduleInfo] = useState<ScheduleType | null>(null);

  const { state: isScheduleModalOpen, toggleState: toggleScheduleModalOpen } = useToggle();
  const { state: isScheduleModifyModalOpen, toggleState: toggleScheduleModifyModalOpen } =
    useToggle();

  const handleClickSchedule = (e: React.MouseEvent, info: ScheduleType) => {
    if (e.target !== e.currentTarget) {
      return;
    }

    setScheduleModalPos(calculateModalPos(e.clientX, e.clientY));
    setScheduleInfo(info);
    toggleScheduleModalOpen();
  };

  const calculateModalPos = (clickX: number, clickY: number) => {
    const position = { top: clickY, right: 0, bottom: 0, left: clickX };

    if (clickX > innerWidth / 2) {
      position.right = innerWidth - clickX;
      position.left = 0;
    }

    if (clickY > innerHeight / 2) {
      position.bottom = innerHeight - clickY;
      position.top = 0;
    }

    return position;
  };

  const { year, month, date, day } = extractDateTime(moreScheduleDateTime);

  return (
    <div css={moreScheduleModalStyle(theme, moreScheduleModalPos)}>
      <div css={headerStyle}>
        <span css={dayTextStyle(theme, day)}>{DAYS[day]}</span>
        <span css={dateTextStyle(theme, day, getThisMonth() === month && getThisDate() === date)}>
          {date}
        </span>
      </div>

      {longTermSchedulesWithPriority.map((el) => {
        const startDate = getISODateString(el.schedule.startDateTime);
        const endDate = getISODateString(el.schedule.endDateTime);
        const nowDate = getFormattedDate(year, month, date);

        return (
          startDate <= nowDate &&
          nowDate <= endDate && (
            <div
              key={`modal-${nowDate}#${el.schedule.id}`}
              css={itemWithBackgroundStyle(el.schedule.colorCode)}
              onClick={(e) => handleClickSchedule(e, el.schedule)}
            >
              {el.schedule.title || CALENDAR.EMPTY_TITLE}
            </div>
          )
        );
      })}

      {allDaySchedulesWithPriority.map((el) => {
        const startDate = getISODateString(el.schedule.startDateTime);
        const nowDate = getFormattedDate(year, month, date);

        return (
          startDate === nowDate && (
            <div
              key={`modal-${nowDate}#${el.schedule.id}`}
              css={itemWithBackgroundStyle(el.schedule.colorCode)}
              onClick={(e) => handleClickSchedule(e, el.schedule)}
            >
              {el.schedule.title || CALENDAR.EMPTY_TITLE}
            </div>
          )
        );
      })}

      {fewHourSchedulesWithPriority.map((el) => {
        const startDate = getISODateString(el.schedule.startDateTime);
        const nowDate = getFormattedDate(year, month, date);

        return (
          startDate === nowDate && (
            <div
              key={`modal-${nowDate}#${el.schedule.id}`}
              css={itemWithoutBackgroundStyle(theme, el.schedule.colorCode)}
              onClick={(e) => handleClickSchedule(e, el.schedule)}
            >
              {el.schedule.title || CALENDAR.EMPTY_TITLE}
            </div>
          )
        );
      })}

      {scheduleInfo ? (
        <>
          <ModalPortal
            isOpen={isScheduleModalOpen}
            closeModal={toggleScheduleModalOpen}
            dimmerBackground={TRANSPARENT}
          >
            <ScheduleModal
              scheduleModalPos={scheduleModalPos}
              scheduleInfo={scheduleInfo}
              toggleScheduleModifyModalOpen={toggleScheduleModifyModalOpen}
              closeModal={toggleScheduleModalOpen}
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
      ) : (
        <></>
      )}
    </div>
  );
}

export default MoreScheduleModal;

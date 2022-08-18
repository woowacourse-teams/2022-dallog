import { useTheme } from '@emotion/react';

import { ModalPosType } from '@/@types';
import { CalendarType } from '@/@types/calendar';
import { ScheduleType } from '@/@types/schedule';

import { CALENDAR } from '@/constants';
import { DAYS } from '@/constants/date';

import { getFormattedDate, getISODateString, getThisDate, getThisMonth } from '@/utils/date';

import {
  dateModalStyle,
  dateTextStyle,
  dayTextStyle,
  headerStyle,
  itemWithBackgroundStyle,
  itemWithoutBackgroundStyle,
} from './DateModal.styles';

interface DateModalProps {
  dateModalPos: ModalPosType;
  moreDateInfo: CalendarType;
  longTermsWithPriority: { schedule: ScheduleType; priority: number }[];
  allDaysWithPriority: { schedule: ScheduleType; priority: number }[];
  fewHoursWithPriority: { schedule: ScheduleType; priority: number }[];
}

function DateModal({
  dateModalPos,
  moreDateInfo,
  longTermsWithPriority,
  allDaysWithPriority,
  fewHoursWithPriority,
}: DateModalProps) {
  const theme = useTheme();

  return (
    <div css={dateModalStyle(theme, dateModalPos)}>
      <div css={headerStyle}>
        <span css={dayTextStyle(theme, moreDateInfo.day)}>{DAYS[moreDateInfo.day]}</span>
        <span
          css={dateTextStyle(
            theme,
            moreDateInfo.day,
            getThisMonth() === moreDateInfo.month && getThisDate() === moreDateInfo.date
          )}
        >
          {moreDateInfo.date}
        </span>
      </div>

      {longTermsWithPriority.map((el) => {
        const startDate = getISODateString(el.schedule.startDateTime);
        const endDate = getISODateString(el.schedule.endDateTime);
        const nowDate = getFormattedDate(moreDateInfo.year, moreDateInfo.month, moreDateInfo.date);

        return (
          startDate <= nowDate &&
          nowDate <= endDate && (
            <div
              key={`modal-${nowDate}#${el.schedule.id}`}
              css={itemWithBackgroundStyle(el.schedule.colorCode)}
            >
              {el.schedule.title || CALENDAR.EMPTY_TITLE}
            </div>
          )
        );
      })}

      {allDaysWithPriority.map((el) => {
        const startDate = getISODateString(el.schedule.startDateTime);
        const nowDate = getFormattedDate(moreDateInfo.year, moreDateInfo.month, moreDateInfo.date);

        return (
          startDate === nowDate && (
            <div
              key={`modal-${nowDate}#${el.schedule.id}`}
              css={itemWithBackgroundStyle(el.schedule.colorCode)}
            >
              {el.schedule.title || CALENDAR.EMPTY_TITLE}
            </div>
          )
        );
      })}

      {fewHoursWithPriority.map((el) => {
        const startDate = getISODateString(el.schedule.startDateTime);
        const nowDate = getFormattedDate(moreDateInfo.year, moreDateInfo.month, moreDateInfo.date);

        return (
          startDate === nowDate && (
            <div
              key={`modal-${nowDate}#${el.schedule.id}`}
              css={itemWithoutBackgroundStyle(theme, el.schedule.colorCode)}
            >
              {el.schedule.title || CALENDAR.EMPTY_TITLE}
            </div>
          )
        );
      })}
    </div>
  );
}

export default DateModal;

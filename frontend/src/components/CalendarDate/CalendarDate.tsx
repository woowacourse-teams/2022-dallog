import { useTheme } from '@emotion/react';

import { CalendarType } from '@/@types/calendar';

import { calendarDate, dateBorder, dateText } from './CalendarDate.style';

interface CalendarDateProps {
  dateInfo: CalendarType;
  isThisMonth: boolean;
}

function CalendarDate({ dateInfo, isThisMonth }: CalendarDateProps) {
  const theme = useTheme();

  return (
    <div css={calendarDate(theme, dateInfo.day, isThisMonth)}>
      <div css={dateBorder}>
        <span css={dateText}> {dateInfo.date}</span>
      </div>
    </div>
  );
}

export default CalendarDate;

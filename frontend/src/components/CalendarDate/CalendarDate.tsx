import { useTheme } from '@emotion/react';

import { CalendarType } from '@/@types/calendar';

import { dateBorder, dateText } from './CalendarDate.style';

interface CalendarDateProps {
  dateInfo: CalendarType;
  isThisMonth: boolean;
}

function CalendarDate({ dateInfo, isThisMonth }: CalendarDateProps) {
  const theme = useTheme();

  return (
    <div css={dateBorder(theme, dateInfo.day)}>
      <span css={dateText(theme, dateInfo.day, isThisMonth)}> {dateInfo.date}</span>
    </div>
  );
}

export default CalendarDate;

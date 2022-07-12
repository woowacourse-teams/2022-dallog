import { Schedule } from '@/@types';

import { calendar, schedule } from './Calendar.styles';

interface CalendarProps {
  schedules: Schedule[];
}

function Calendar({ schedules }: CalendarProps) {
  return (
    <div css={calendar}>
      {schedules.map(({ id, title, startDateTime, endDateTime, memo }) => {
        return (
          <div key={id} css={schedule}>
            📅 {startDateTime.split('T').join(' ')} ~ {endDateTime.split('T').join(' ')}
            <p>&lt; {title} &gt;</p>
            {memo}
          </div>
        );
      })}
    </div>
  );
}

export default Calendar;

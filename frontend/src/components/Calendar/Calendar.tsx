interface CalendarProps {
  children?: string;
}

function Calendar({ children }: CalendarProps) {
  return <div>{children}</div>;
}

export default Calendar;

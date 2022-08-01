interface InputRef {
  [index: string]: React.RefObject<HTMLInputElement>;
}

interface ScheduleType {
  id: number;
  title: string;
  startDateTime: string;
  endDateTime: string;
  memo: string;
}

export { InputRef, ScheduleType };

interface ScheduleType {
  id: number;
  title: string;
  startDateTime: string;
  endDateTime: string;
  memo: string;
}

interface ScheduleResponseType {
  longTerms: Array<{ categoryId: number; color: string } & ScheduleType>;

  allDays: Array<{ categoryId: number; color: string } & ScheduleType>;

  fewHours: Array<{ categoryId: number; color: string } & ScheduleType>;
}

export { ScheduleResponseType, ScheduleType };

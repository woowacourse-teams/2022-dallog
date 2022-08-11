interface ScheduleModalPosType {
  top?: number;
  right?: number;
  bottom?: number;
  left?: number;
}

interface ScheduleType {
  id: number;
  categoryId: number;
  title: string;
  startDateTime: string;
  endDateTime: string;
  memo: string;
  colorCode: string;
}

interface ScheduleResponseType {
  longTerms: Array<ScheduleType>;
  allDays: Array<ScheduleType>;
  fewHours: Array<ScheduleType>;
}

export { ScheduleModalPosType, ScheduleResponseType, ScheduleType };

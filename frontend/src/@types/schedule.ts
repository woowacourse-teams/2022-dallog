import { CATEGORY_TYPE } from '@/constants/category';

interface ScheduleType {
  id: string;
  categoryId: number;
  title: string;
  startDateTime: string;
  endDateTime: string;
  memo: string;
  colorCode: string;
  categoryType: typeof CATEGORY_TYPE[keyof typeof CATEGORY_TYPE];
}

interface ScheduleResponseType {
  longTerms: Array<ScheduleType>;
  allDays: Array<ScheduleType>;
  fewHours: Array<ScheduleType>;
}

export { ScheduleResponseType, ScheduleType };

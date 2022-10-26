import { ValueOf } from '@/@types/util';

import { CATEGORY_TYPE } from '@/constants/category';

interface ScheduleType {
  id: string;
  categoryId: number;
  title: string;
  startDateTime: string;
  endDateTime: string;
  memo: string;
  colorCode: string;
  categoryType: ValueOf<typeof CATEGORY_TYPE>;
}

type ScheduleResponseType = Record<'longTerms' | 'allDays' | 'fewHours', Array<ScheduleType>>;

export { ScheduleResponseType, ScheduleType };

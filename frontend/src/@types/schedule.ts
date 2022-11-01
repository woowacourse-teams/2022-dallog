import { ValueOf } from '@/@types/util';

import { CATEGORY_TYPE } from '@/constants/category';
import { SCHEDULE } from '@/constants/schedule';

type ScheduleResponseKeyType =
  | typeof SCHEDULE.RESPONSE_TYPE.LONG_TERMS
  | typeof SCHEDULE.RESPONSE_TYPE.ALL_DAYS
  | typeof SCHEDULE.RESPONSE_TYPE.FEW_HOURS;

type ScheduleResponseType = Record<ScheduleResponseKeyType, Array<ScheduleType>>;

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

export { ScheduleResponseKeyType, ScheduleResponseType, ScheduleType };

import { zeroFill } from '@/utils';

const DATE_TIME = {
  START: '00:00',
  END: '00:00',
};

const DAYS = ['일', '월', '화', '수', '목', '금', '토'];

const TIMES = new Array(48)
  .fill(0)
  .map((_, arrIdx) => Math.floor(arrIdx / 2).toString())
  .map((hour, hourIdx) => (hourIdx % 2 === 0 ? `${zeroFill(hour)}:00` : `${zeroFill(hour)}:30`));

export { DATE_TIME, DAYS, TIMES };

import { validateLength, validateNotEmpty, validateStartEndDateTime } from '@/validation';
import { useEffect } from 'react';

import { DATE_TIME } from '@/constants/date';
import { VALIDATION_SIZE } from '@/constants/validate';

import { getEndTime, getISODateString, getISOString, getNextDate } from '@/utils/date';

import useControlledInput from './useControlledInput';

interface useValidateScheduleParametersType {
  initialTitle?: string;
  initialStartDate?: string;
  initialStartTime?: string;
  initialEndDate?: string;
  initialEndTime?: string;
  initialMemo?: string;
}

function useValidateSchedule({
  initialTitle,
  initialStartDate,
  initialStartTime,
  initialEndDate,
  initialEndTime,
  initialMemo,
}: useValidateScheduleParametersType) {
  const title = useControlledInput(initialTitle);
  const startDate = useControlledInput(initialStartDate);
  const startTime = useControlledInput(initialStartTime || DATE_TIME.START);
  const endDate = useControlledInput(initialEndDate);
  const endTime = useControlledInput(initialEndTime || DATE_TIME.END);
  const memo = useControlledInput(initialMemo);

  const isValidSchedule =
    validateLength(
      title.inputValue,
      VALIDATION_SIZE.MIN_LENGTH,
      VALIDATION_SIZE.SCHEDULE_TITLE_MAX_LENGTH
    ) &&
    validateStartEndDateTime(
      `${startDate.inputValue}T${startTime}`,
      `${endDate.inputValue}T${endTime}`
    ) &&
    validateLength(memo.inputValue, 0, VALIDATION_SIZE.SCHEDULE_MEMO_MAX_LENGTH) &&
    validateNotEmpty(startDate.inputValue) &&
    validateNotEmpty(endDate.inputValue);

  useEffect(() => {
    if (startDate.inputValue + startTime.inputValue <= endDate.inputValue + endTime.inputValue)
      return;

    if (startDate.inputValue > endDate.inputValue) {
      endDate.setInputValue(startDate.inputValue);
    }

    if (startTime.inputValue >= '23:00') {
      const [year, month, day] = startDate.inputValue.split('-');
      const nextDate = getISODateString(
        getISOString(getNextDate(new Date(+year, +month - 1, +day + 1), 1))
      );

      endDate.setInputValue(nextDate);
    }

    endTime.setInputValue(getEndTime(startTime.inputValue));
  }, [startDate, startTime]);

  return {
    title,
    startDate,
    startTime,
    endDate,
    endTime,
    memo,
    isValidSchedule,
  };
}

export default useValidateSchedule;

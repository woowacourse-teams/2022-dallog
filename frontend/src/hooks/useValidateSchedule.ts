import { validateLength, validateNotEmpty, validateStartEndDateTime } from '@/validation';
import { useEffect } from 'react';

import { VALIDATION_SIZE } from '@/constants/validate';

import { getOneHourEarlierISOString, getOneHourLaterISOString } from '@/utils/date';

import useControlledInput from './useControlledInput';

interface useValidateScheduleParametersType {
  initialTitle?: string;
  initialStartDateTime?: string;
  initialEndDateTime?: string;
  initialMemo?: string;
}

function useValidateSchedule({
  initialTitle,
  initialStartDateTime,
  initialEndDateTime,
  initialMemo,
}: useValidateScheduleParametersType) {
  const title = useControlledInput(initialTitle);
  const startDateTime = useControlledInput(initialStartDateTime);
  const endDateTime = useControlledInput(initialEndDateTime);
  const memo = useControlledInput(initialMemo);

  const isValidSchedule =
    validateLength(
      title.inputValue,
      VALIDATION_SIZE.MIN_LENGTH,
      VALIDATION_SIZE.SCHEDULE_TITLE_MAX_LENGTH
    ) &&
    validateStartEndDateTime(startDateTime.inputValue, endDateTime.inputValue) &&
    validateLength(memo.inputValue, 0, VALIDATION_SIZE.SCHEDULE_MEMO_MAX_LENGTH) &&
    validateNotEmpty(startDateTime.inputValue) &&
    validateNotEmpty(endDateTime.inputValue);

  return {
    title,
    startDateTime,
    endDateTime,
    memo,
    isValidSchedule,
  };
}

export default useValidateSchedule;

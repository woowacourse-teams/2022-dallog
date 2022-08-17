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

  useEffect(() => {
    const resetEndDateTimeValue = () => {
      if (startDateTime.inputValue <= endDateTime.inputValue) {
        return;
      }

      if (!startDateTime.inputValue.includes('T')) {
        endDateTime.setInputValue(startDateTime.inputValue);

        return;
      }

      endDateTime.setInputValue(getOneHourLaterISOString(startDateTime.inputValue));
    };

    resetEndDateTimeValue();
  }, [startDateTime.inputValue]);

  useEffect(() => {
    const resetStartDateTimeValue = () => {
      if (endDateTime.inputValue >= startDateTime.inputValue) {
        return;
      }

      if (!endDateTime.inputValue.includes('T')) {
        startDateTime.setInputValue(endDateTime.inputValue);

        return;
      }

      startDateTime.setInputValue(getOneHourEarlierISOString(endDateTime.inputValue));
    };

    resetStartDateTimeValue();
  }, [endDateTime.inputValue]);

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

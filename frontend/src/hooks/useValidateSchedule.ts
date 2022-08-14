import { validateLength, validateNotEmpty, validateStartEndDateTime } from '@/validation';

import { VALIDATION_SIZE } from '@/constants';

import useControlledInput from './useControlledInput';

interface useValidateScheduleParametersType {
  defaultTitle?: string;
  defaultStartDateTime?: string;
  defaultEndDateTime?: string;
  defaultMemo?: string;
}

function useValidateSchedule({
  defaultTitle,
  defaultStartDateTime,
  defaultEndDateTime,
  defaultMemo,
}: useValidateScheduleParametersType) {
  const title = useControlledInput(defaultTitle);
  const startDateTime = useControlledInput(defaultStartDateTime);
  const endDateTime = useControlledInput(defaultEndDateTime);
  const memo = useControlledInput(defaultMemo);

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

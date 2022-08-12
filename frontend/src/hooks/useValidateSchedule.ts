import { validateLength, validateNotEmpty, validateStartEndDateTime } from '@/validation';

import useControlledInput from './useControlledInput';

interface useValidationParameterType {
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
}: useValidationParameterType) {
  const title = useControlledInput(defaultTitle);
  const startDateTime = useControlledInput(defaultStartDateTime);
  const endDateTime = useControlledInput(defaultEndDateTime);
  const memo = useControlledInput(defaultMemo);

  const isValidSchedule =
    validateLength(title.inputValue, 1, 20) &&
    validateStartEndDateTime(startDateTime.inputValue, endDateTime.inputValue) &&
    validateLength(memo.inputValue, 1, 255) &&
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

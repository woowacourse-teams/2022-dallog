import { validateLength, validateNotEqualString } from '@/validation';

import { VALIDATION_MESSAGE, VALIDATION_SIZE, VALIDATION_STRING } from '@/constants/validate';

import useControlledInput from './useControlledInput';

function useValidateCategory(initialCategory?: string) {
  const categoryValue = useControlledInput(initialCategory);

  const getCategoryErrorMessage = () => {
    if (
      !validateLength(
        categoryValue.inputValue,
        VALIDATION_SIZE.MIN_LENGTH,
        VALIDATION_SIZE.CATEGORY_NAME_MAX_LENGTH
      )
    ) {
      return VALIDATION_MESSAGE.STRING_LENGTH(
        VALIDATION_SIZE.MIN_LENGTH,
        VALIDATION_SIZE.CATEGORY_NAME_MAX_LENGTH
      );
    }

    if (!validateNotEqualString(categoryValue.inputValue, VALIDATION_STRING.CATEGORY)) {
      return VALIDATION_MESSAGE.INVALID_CATEGORY_NAME;
    }

    return undefined;
  };

  const isValidCategory =
    validateLength(
      categoryValue.inputValue,
      VALIDATION_SIZE.MIN_LENGTH,
      VALIDATION_SIZE.CATEGORY_NAME_MAX_LENGTH
    ) && validateNotEqualString(categoryValue.inputValue, VALIDATION_STRING.CATEGORY);

  return { categoryValue, getCategoryErrorMessage, isValidCategory };
}

export default useValidateCategory;

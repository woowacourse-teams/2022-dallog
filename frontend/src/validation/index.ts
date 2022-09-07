import { VALIDATION_STRING } from '@/constants/validate';

const validateLength = (target: string, min: number, max: number) => {
  return min <= target.length && target.length <= max;
};

const validateNotEmpty = (target: string) => {
  return target.length > 0;
};

const validateNotEqualString = (target: string, comparisonTarget: string) => {
  return target.trim() !== comparisonTarget;
};

const validateStartEndDateTime = (startDate: string, endDate: string) => {
  return startDate <= endDate;
};

const validateWithdrawalCondition = (value: string) => {
  return value === VALIDATION_STRING.WITHDRAWAL;
};
export {
  validateLength,
  validateNotEmpty,
  validateNotEqualString,
  validateStartEndDateTime,
  validateWithdrawalCondition,
};

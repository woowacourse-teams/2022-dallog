import { VALIDATION_STRING } from '@/constants/validate';

const validateLength = (target: string, min: number, max: number) =>
  min <= target.length && target.length <= max;

const validateNotEmpty = (target: string) => target.length > 0;

const validateNotEqualString = (target: string, comparisonTarget: string) =>
  target.trim() !== comparisonTarget;

const validateStartEndDateTime = (startDate: string, endDate: string) => startDate <= endDate;

const validateWithdrawalCondition = (value: string) => value === VALIDATION_STRING.WITHDRAWAL;

export {
  validateLength,
  validateNotEmpty,
  validateNotEqualString,
  validateStartEndDateTime,
  validateWithdrawalCondition,
};
